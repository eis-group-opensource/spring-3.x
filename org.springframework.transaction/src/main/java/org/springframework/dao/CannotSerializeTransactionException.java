/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.dao;

/**
 * Exception thrown on failure to complete a transaction in serialized mode
 * due to update conflicts.
 *
 * @author Rod Johnson
 */
public class CannotSerializeTransactionException extends PessimisticLockingFailureException {

	/**
	 * Constructor for CannotSerializeTransactionException.
	 * @param msg the detail message
	 */
	public CannotSerializeTransactionException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for CannotSerializeTransactionException.
	 * @param msg the detail message
	 * @param cause the root cause from the data access API in use
	 */
	public CannotSerializeTransactionException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
