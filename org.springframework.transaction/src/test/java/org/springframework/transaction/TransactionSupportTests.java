/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.transaction;

import junit.framework.TestCase;

import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author Juergen Hoeller
 * @since 29.04.2003
 */
public class TransactionSupportTests extends TestCase {

	public void testNoExistingTransaction() {
		PlatformTransactionManager tm = new TestTransactionManager(false, true);
		DefaultTransactionStatus status1 = (DefaultTransactionStatus)
				tm.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_SUPPORTS));
		assertTrue("Must not have transaction", status1.getTransaction() == null);

		DefaultTransactionStatus status2 = (DefaultTransactionStatus)
				tm.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED));
		assertTrue("Must have transaction", status2.getTransaction() != null);
		assertTrue("Must be new transaction", status2.isNewTransaction());

		try {
			tm.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_MANDATORY));
			fail("Should not have thrown NoTransactionException");
		}
		catch (IllegalTransactionStateException ex) {
			// expected
		}
	}

	public void testExistingTransaction() {
		PlatformTransactionManager tm = new TestTransactionManager(true, true);
		DefaultTransactionStatus status1 = (DefaultTransactionStatus)
				tm.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_SUPPORTS));
		assertTrue("Must have transaction", status1.getTransaction() != null);
		assertTrue("Must not be new transaction", !status1.isNewTransaction());

		DefaultTransactionStatus status2 = (DefaultTransactionStatus)
				tm.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED));
		assertTrue("Must have transaction", status2.getTransaction() != null);
		assertTrue("Must not be new transaction", !status2.isNewTransaction());

		try {
			DefaultTransactionStatus status3 = (DefaultTransactionStatus)
					tm.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_MANDATORY));
			assertTrue("Must have transaction", status3.getTransaction() != null);
			assertTrue("Must not be new transaction", !status3.isNewTransaction());
		}
		catch (NoTransactionException ex) {
			fail("Should not have thrown NoTransactionException");
		}
	}

	public void testCommitWithoutExistingTransaction() {
		TestTransactionManager tm = new TestTransactionManager(false, true);
		TransactionStatus status = tm.getTransaction(null);
		tm.commit(status);
		assertTrue("triggered begin", tm.begin);
		assertTrue("triggered commit", tm.commit);
		assertTrue("no rollback", !tm.rollback);
		assertTrue("no rollbackOnly", !tm.rollbackOnly);
	}

	public void testRollbackWithoutExistingTransaction() {
		TestTransactionManager tm = new TestTransactionManager(false, true);
		TransactionStatus status = tm.getTransaction(null);
		tm.rollback(status);
		assertTrue("triggered begin", tm.begin);
		assertTrue("no commit", !tm.commit);
		assertTrue("triggered rollback", tm.rollback);
		assertTrue("no rollbackOnly", !tm.rollbackOnly);
	}

	public void testRollbackOnlyWithoutExistingTransaction() {
		TestTransactionManager tm = new TestTransactionManager(false, true);
		TransactionStatus status = tm.getTransaction(null);
		status.setRollbackOnly();
		tm.commit(status);
		assertTrue("triggered begin", tm.begin);
		assertTrue("no commit", !tm.commit);
		assertTrue("triggered rollback", tm.rollback);
		assertTrue("no rollbackOnly", !tm.rollbackOnly);
	}

	public void testCommitWithExistingTransaction() {
		TestTransactionManager tm = new TestTransactionManager(true, true);
		TransactionStatus status = tm.getTransaction(null);
		tm.commit(status);
		assertTrue("no begin", !tm.begin);
		assertTrue("no commit", !tm.commit);
		assertTrue("no rollback", !tm.rollback);
		assertTrue("no rollbackOnly", !tm.rollbackOnly);
	}

	public void testRollbackWithExistingTransaction() {
		TestTransactionManager tm = new TestTransactionManager(true, true);
		TransactionStatus status = tm.getTransaction(null);
		tm.rollback(status);
		assertTrue("no begin", !tm.begin);
		assertTrue("no commit", !tm.commit);
		assertTrue("no rollback", !tm.rollback);
		assertTrue("triggered rollbackOnly", tm.rollbackOnly);
	}

	public void testRollbackOnlyWithExistingTransaction() {
		TestTransactionManager tm = new TestTransactionManager(true, true);
		TransactionStatus status = tm.getTransaction(null);
		status.setRollbackOnly();
		tm.commit(status);
		assertTrue("no begin", !tm.begin);
		assertTrue("no commit", !tm.commit);
		assertTrue("no rollback", !tm.rollback);
		assertTrue("triggered rollbackOnly", tm.rollbackOnly);
	}

	public void testTransactionTemplate() {
		TestTransactionManager tm = new TestTransactionManager(false, true);
		TransactionTemplate template = new TransactionTemplate(tm);
		template.execute(new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(TransactionStatus status) {
			}
		});
		assertTrue("triggered begin", tm.begin);
		assertTrue("triggered commit", tm.commit);
		assertTrue("no rollback", !tm.rollback);
		assertTrue("no rollbackOnly", !tm.rollbackOnly);
	}

	public void testTransactionTemplateWithCallbackPreference() {
		MockCallbackPreferringTransactionManager ptm = new MockCallbackPreferringTransactionManager();
		TransactionTemplate template = new TransactionTemplate(ptm);
		template.execute(new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(TransactionStatus status) {
			}
		});
		assertSame(template, ptm.getDefinition());
		assertFalse(ptm.getStatus().isRollbackOnly());
	}

	public void testTransactionTemplateWithException() {
		TestTransactionManager tm = new TestTransactionManager(false, true);
		TransactionTemplate template = new TransactionTemplate(tm);
		final RuntimeException ex = new RuntimeException("Some application exception");
		try {
			template.execute(new TransactionCallbackWithoutResult() {
				protected void doInTransactionWithoutResult(TransactionStatus status) {
					throw ex;
				}
			});
			fail("Should have propagated RuntimeException");
		}
		catch (RuntimeException caught) {
			// expected
			assertTrue("Correct exception", caught == ex);
			assertTrue("triggered begin", tm.begin);
			assertTrue("no commit", !tm.commit);
			assertTrue("triggered rollback", tm.rollback);
			assertTrue("no rollbackOnly", !tm.rollbackOnly);
		}
	}

	public void testTransactionTemplateWithRollbackException() {
		final TransactionSystemException tex = new TransactionSystemException("system exception");
		TestTransactionManager tm = new TestTransactionManager(false, true) {
			protected void doRollback(DefaultTransactionStatus status) {
				super.doRollback(status);
				throw tex;
			}
		};
		TransactionTemplate template = new TransactionTemplate(tm);
		final RuntimeException ex = new RuntimeException("Some application exception");
		try {
			template.execute(new TransactionCallbackWithoutResult() {
				protected void doInTransactionWithoutResult(TransactionStatus status) {
					throw ex;
				}
			});
			fail("Should have propagated RuntimeException");
		}
		catch (RuntimeException caught) {
			// expected
			assertTrue("Correct exception", caught == tex);
			assertTrue("triggered begin", tm.begin);
			assertTrue("no commit", !tm.commit);
			assertTrue("triggered rollback", tm.rollback);
			assertTrue("no rollbackOnly", !tm.rollbackOnly);
		}
	}

	public void testTransactionTemplateWithError() {
		TestTransactionManager tm = new TestTransactionManager(false, true);
		TransactionTemplate template = new TransactionTemplate(tm);
		try {
			template.execute(new TransactionCallbackWithoutResult() {
				protected void doInTransactionWithoutResult(TransactionStatus status) {
					throw new Error("Some application error");
				}
			});
			fail("Should have propagated Error");
		}
		catch (Error err) {
			// expected
			assertTrue("triggered begin", tm.begin);
			assertTrue("no commit", !tm.commit);
			assertTrue("triggered rollback", tm.rollback);
			assertTrue("no rollbackOnly", !tm.rollbackOnly);
		}
	}

	public void testTransactionTemplateInitialization() {
		TestTransactionManager tm = new TestTransactionManager(false, true);
		TransactionTemplate template = new TransactionTemplate();
		template.setTransactionManager(tm);
		assertTrue("correct transaction manager set", template.getTransactionManager() == tm);

		try {
			template.setPropagationBehaviorName("TIMEOUT_DEFAULT");
			fail("Should have thrown IllegalArgumentException");
		}
		catch (IllegalArgumentException ex) {
			// expected
		}
		template.setPropagationBehaviorName("PROPAGATION_SUPPORTS");
		assertTrue("Correct propagation behavior set", template.getPropagationBehavior() == TransactionDefinition.PROPAGATION_SUPPORTS);

		try {
			template.setPropagationBehavior(999);
			fail("Should have thrown IllegalArgumentException");
		}
		catch (IllegalArgumentException ex) {
			// expected
		}
		template.setPropagationBehavior(TransactionDefinition.PROPAGATION_MANDATORY);
		assertTrue("Correct propagation behavior set", template.getPropagationBehavior() == TransactionDefinition.PROPAGATION_MANDATORY);

		try {
			template.setIsolationLevelName("TIMEOUT_DEFAULT");
			fail("Should have thrown IllegalArgumentException");
		}
		catch (IllegalArgumentException ex) {
			// expected
		}
		template.setIsolationLevelName("ISOLATION_SERIALIZABLE");
		assertTrue("Correct isolation level set", template.getIsolationLevel() == TransactionDefinition.ISOLATION_SERIALIZABLE);

		try {
			template.setIsolationLevel(999);
			fail("Should have thrown IllegalArgumentException");
		}
		catch (IllegalArgumentException ex) {
			// expected
		}
		template.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		assertTrue("Correct isolation level set", template.getIsolationLevel() == TransactionDefinition.ISOLATION_REPEATABLE_READ);
	}

	protected void tearDown() {
		assertTrue(TransactionSynchronizationManager.getResourceMap().isEmpty());
		assertFalse(TransactionSynchronizationManager.isSynchronizationActive());
	}

}
