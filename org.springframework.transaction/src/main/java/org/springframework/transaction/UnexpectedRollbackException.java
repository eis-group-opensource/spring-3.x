/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.transaction;

/**
 * Thrown when an attempt to commit a transaction resulted
 * in an unexpected rollback.
 *
 * @author Rod Johnson
 * @since 17.03.2003
 */
public class UnexpectedRollbackException extends TransactionException {

	/**
	 * Constructor for UnexpectedRollbackException.
	 * @param msg the detail message
	 */
	public UnexpectedRollbackException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for UnexpectedRollbackException.
	 * @param msg the detail message
	 * @param cause the root cause from the transaction API in use
	 */
	public UnexpectedRollbackException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
