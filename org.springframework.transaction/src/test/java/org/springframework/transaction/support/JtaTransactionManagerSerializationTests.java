/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.transaction.support;

import static org.easymock.EasyMock.createMock;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import junit.framework.TestCase;

import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.util.SerializationTestUtils;

/**
 * @author Rod Johnson
 */
public class JtaTransactionManagerSerializationTests extends TestCase {

	public void testSerializable() throws Exception {
		UserTransaction ut1 = createMock(UserTransaction.class);
		UserTransaction ut2 = createMock(UserTransaction.class);
		TransactionManager tm = createMock(TransactionManager.class);

		JtaTransactionManager jtam = new JtaTransactionManager();
		jtam.setUserTransaction(ut1);
		jtam.setTransactionManager(tm);
		jtam.setRollbackOnCommitFailure(true);
		jtam.afterPropertiesSet();

		SimpleNamingContextBuilder jndiEnv = SimpleNamingContextBuilder
				.emptyActivatedContextBuilder();
		jndiEnv.bind(JtaTransactionManager.DEFAULT_USER_TRANSACTION_NAME, ut2);
		JtaTransactionManager serializedJtatm = (JtaTransactionManager) SerializationTestUtils
				.serializeAndDeserialize(jtam);

		// should do client-side lookup
		assertNotNull("Logger must survive serialization",
				serializedJtatm.logger);
		assertTrue("UserTransaction looked up on client", serializedJtatm
				.getUserTransaction() == ut2);
		assertNull("TransactionManager didn't survive", serializedJtatm
				.getTransactionManager());
		assertEquals(true, serializedJtatm.isRollbackOnCommitFailure());
	}

}
