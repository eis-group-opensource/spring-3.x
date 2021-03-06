/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.transaction.jta;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.xa.XAResource;

import org.springframework.util.Assert;

/**
 * Adapter for a managed JTA Transaction handle, taking a JTA
 * {@link javax.transaction.TransactionManager} reference and creating
 * a JTA {@link javax.transaction.Transaction} handle for it.
 *
 * @author Juergen Hoeller
 * @since 3.0.2
 */
public class ManagedTransactionAdapter implements Transaction {

	private final TransactionManager transactionManager;


	/**
	 * Create a new ManagedTransactionAdapter for the given TransactionManager.
	 * @param transactionManager the JTA TransactionManager to wrap
	 */
	public ManagedTransactionAdapter(TransactionManager transactionManager) throws SystemException {
		Assert.notNull(transactionManager, "TransactionManager must not be null");
		this.transactionManager = transactionManager;
	}

	/**
	 * Return the JTA TransactionManager that this adapter delegates to.
	 */
	public final TransactionManager getTransactionManager() {
		return this.transactionManager;
	}


	public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException,
			SecurityException, SystemException {
		this.transactionManager.commit();
	}

	public void rollback() throws SystemException {
		this.transactionManager.rollback();
	}

	public void setRollbackOnly() throws SystemException {
		this.transactionManager.setRollbackOnly();
	}

	public int getStatus() throws SystemException {
		return this.transactionManager.getStatus();
	}

	public boolean enlistResource(XAResource xaRes) throws RollbackException, SystemException {
		return this.transactionManager.getTransaction().enlistResource(xaRes);
	}

	public boolean delistResource(XAResource xaRes, int flag) throws SystemException {
		return this.transactionManager.getTransaction().delistResource(xaRes, flag);
	}

	public void registerSynchronization(Synchronization sync) throws RollbackException, SystemException {
		this.transactionManager.getTransaction().registerSynchronization(sync);
	}

}
