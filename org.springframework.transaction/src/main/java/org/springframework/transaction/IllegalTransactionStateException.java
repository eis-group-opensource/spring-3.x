/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.transaction;

/**
 * Exception thrown when the existence or non-existence of a transaction
 * amounts to an illegal state according to the transaction propagation
 * behavior that applies.
 *
 * @author Juergen Hoeller
 * @since 21.01.2004
 */
public class IllegalTransactionStateException extends TransactionUsageException {

	/**
	 * Constructor for IllegalTransactionStateException.
	 * @param msg the detail message
	 */
	public IllegalTransactionStateException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for IllegalTransactionStateException.
	 * @param msg the detail message
	 * @param cause the root cause from the transaction API in use
	 */
	public IllegalTransactionStateException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
