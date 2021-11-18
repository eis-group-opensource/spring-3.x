/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.transaction.support;

/**
 * A simple {@link org.springframework.transaction.TransactionStatus}
 * implementation.
 * 
 * <p>Derives from {@link AbstractTransactionStatus} and adds an explicit
 * {@link #isNewTransaction() "newTransaction"} flag.
 *
 * <p>This class is not used by any of Spring's pre-built
 * {@link org.springframework.transaction.PlatformTransactionManager}
 * implementations. It is mainly provided as a start for custom transaction
 * manager implementations and as a static mock for testing transactional
 * code (either as part of a mock <code>PlatformTransactionManager</code> or
 * as argument passed into a {@link TransactionCallback} to be tested).
 *
 * @author Juergen Hoeller
 * @since 1.2.3
 * @see #SimpleTransactionStatus(boolean)
 * @see TransactionCallback
 */
public class SimpleTransactionStatus extends AbstractTransactionStatus {

	private final boolean newTransaction;


	/**
	 * Create a new instance of the {@link SimpleTransactionStatus} class,
	 * indicating a new transaction.
	 */
	public SimpleTransactionStatus() {
		this(true);
	}

	/**
	 * Create a new instance of the {@link SimpleTransactionStatus} class.
	 * @param newTransaction whether to indicate a new transaction
	 */
	public SimpleTransactionStatus(boolean newTransaction) {
		this.newTransaction = newTransaction;
	}


	public boolean isNewTransaction() {
		return this.newTransaction;
	}

}
