/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.transaction;

/**
 * Exception thrown when a transaction can't be created using an
 * underlying transaction API such as JTA.
 *
 * @author Rod Johnson
 * @since 17.03.2003
 */
public class CannotCreateTransactionException extends TransactionException {

	/**
	 * Constructor for CannotCreateTransactionException.
	 * @param msg the detail message
	 */
	public CannotCreateTransactionException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for CannotCreateTransactionException.
	 * @param msg the detail message
	 * @param cause the root cause from the transaction API in use
	 */
	public CannotCreateTransactionException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
