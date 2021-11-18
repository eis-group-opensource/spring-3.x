/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.dao;

/**
 * Exception thrown on concurrency failure.
 *
 * <p>This exception should be subclassed to indicate the type of failure:
 * optimistic locking, failure to acquire lock, etc.
 *
 * @author Thomas Risberg
 * @since 1.1
 * @see OptimisticLockingFailureException
 * @see PessimisticLockingFailureException
 * @see CannotAcquireLockException
 * @see DeadlockLoserDataAccessException
 */
public class ConcurrencyFailureException extends TransientDataAccessException {

	/**
	 * Constructor for ConcurrencyFailureException.
	 * @param msg the detail message
	 */
	public ConcurrencyFailureException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for ConcurrencyFailureException.
	 * @param msg the detail message
	 * @param cause the root cause from the data access API in use
	 */
	public ConcurrencyFailureException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
