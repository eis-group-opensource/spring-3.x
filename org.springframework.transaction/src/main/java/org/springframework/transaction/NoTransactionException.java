/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.transaction;

/**
 * Exception thrown when an operation is attempted that
 * relies on an existing transaction (such as setting
 * rollback status) and there is no existing transaction.
 * This represents an illegal usage of the transaction API.
 *
 * @author Rod Johnson
 * @since 17.03.2003
 */
public class NoTransactionException extends TransactionUsageException {

	/**
	 * Constructor for NoTransactionException.
	 * @param msg the detail message
	 */
	public NoTransactionException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for NoTransactionException.
	 * @param msg the detail message
	 * @param cause the root cause from the transaction API in use
	 */
	public NoTransactionException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
