/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.dao;

/**
 * Exception thrown on failure to aquire a lock during an update,
 * for example during a "select for update" statement.
 *
 * @author Rod Johnson
 */
public class CannotAcquireLockException extends PessimisticLockingFailureException {

	/**
	 * Constructor for CannotAcquireLockException.
	 * @param msg the detail message
	 */
	public CannotAcquireLockException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for CannotAcquireLockException.
	 * @param msg the detail message
	 * @param cause the root cause from the data access API in use
	 */
	public CannotAcquireLockException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
