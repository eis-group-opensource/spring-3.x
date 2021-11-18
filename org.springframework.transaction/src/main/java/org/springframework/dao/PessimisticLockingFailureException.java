/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.dao;

/**
 * Exception thrown on a pessimistic locking violation.
 * Thrown by Spring's SQLException translation mechanism
 * if a corresponding database error is encountered.
 *
 * <p>Serves as superclass for more specific exceptions, like
 * CannotAcquireLockException and DeadlockLoserDataAccessException.
 *
 * @author Thomas Risberg
 * @since 1.2
 * @see CannotAcquireLockException
 * @see DeadlockLoserDataAccessException
 * @see OptimisticLockingFailureException
 */
public class PessimisticLockingFailureException extends ConcurrencyFailureException {

	/**
	 * Constructor for PessimisticLockingFailureException.
	 * @param msg the detail message
	 */
	public PessimisticLockingFailureException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for PessimisticLockingFailureException.
	 * @param msg the detail message
	 * @param cause the root cause from the data access API in use
	 */
	public PessimisticLockingFailureException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
