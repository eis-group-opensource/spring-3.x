/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.transaction;

/**
 * Exception thrown when attempting to work with a nested transaction
 * but nested transactions are not supported by the underlying backend.
 *
 * @author Juergen Hoeller
 * @since 1.1
 */
public class NestedTransactionNotSupportedException extends CannotCreateTransactionException {

	/**
	 * Constructor for NestedTransactionNotSupportedException.
	 * @param msg the detail message
	 */
	public NestedTransactionNotSupportedException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for NestedTransactionNotSupportedException.
	 * @param msg the detail message
	 * @param cause the root cause from the transaction API in use
	 */
	public NestedTransactionNotSupportedException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
