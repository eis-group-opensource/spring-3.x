/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.transaction;

/**
 * Exception thrown when attempting to suspend an existing transaction
 * but transaction suspension is not supported by the underlying backend.
 *
 * @author Juergen Hoeller
 * @since 1.1
 */
public class TransactionSuspensionNotSupportedException extends CannotCreateTransactionException {

	/**
	 * Constructor for TransactionSuspensionNotSupportedException.
	 * @param msg the detail message
	 */
	public TransactionSuspensionNotSupportedException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for TransactionSuspensionNotSupportedException.
	 * @param msg the detail message
	 * @param cause the root cause from the transaction API in use
	 */
	public TransactionSuspensionNotSupportedException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
