/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.transaction;

import org.springframework.transaction.support.CallbackPreferringPlatformTransactionManager;
import org.springframework.transaction.support.SimpleTransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

/**
 * @author Juergen Hoeller
 */
public class MockCallbackPreferringTransactionManager implements CallbackPreferringPlatformTransactionManager {

	private TransactionDefinition definition;

	private TransactionStatus status;


	public Object execute(TransactionDefinition definition, TransactionCallback callback) throws TransactionException {
		this.definition = definition;
		this.status = new SimpleTransactionStatus();
		return callback.doInTransaction(this.status);
	}

	public TransactionDefinition getDefinition() {
		return this.definition;
	}

	public TransactionStatus getStatus() {
		return this.status;
	}


	public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
		throw new UnsupportedOperationException();
	}

	public void commit(TransactionStatus status) throws TransactionException {
		throw new UnsupportedOperationException();
	}

	public void rollback(TransactionStatus status) throws TransactionException {
		throw new UnsupportedOperationException();
	}

}
