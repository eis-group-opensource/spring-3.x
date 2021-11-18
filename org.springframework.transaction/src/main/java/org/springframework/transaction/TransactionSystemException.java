/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.transaction;

import org.springframework.util.Assert;

/**
 * Exception thrown when a general transaction system error is encountered,
 * like on commit or rollback.
 *
 * @author Juergen Hoeller
 * @since 24.03.2003
 */
public class TransactionSystemException extends TransactionException {

	private Throwable applicationException;


	/**
	 * Constructor for TransactionSystemException.
	 * @param msg the detail message
	 */
	public TransactionSystemException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for TransactionSystemException.
	 * @param msg the detail message
	 * @param cause the root cause from the transaction API in use
	 */
	public TransactionSystemException(String msg, Throwable cause) {
		super(msg, cause);
	}


	/**
	 * Set an application exception that was thrown before this transaction exception,
	 * preserving the original exception despite the overriding TransactionSystemException.
	 * @param ex the application exception
	 * @throws IllegalStateException if this TransactionSystemException already holds an
	 * application exception
	 */
	public void initApplicationException(Throwable ex) {
		Assert.notNull(ex, "Application exception must not be null");
		if (this.applicationException != null) {
			throw new IllegalStateException("Already holding an application exception: " + this.applicationException);
		}
		this.applicationException = ex;
	}

	/**
	 * Return the application exception that was thrown before this transaction exception,
	 * if any.
	 * @return the application exception, or <code>null</code> if none set
	 */
	public final Throwable getApplicationException() {
		return this.applicationException;
	}

	/**
	 * Return the exception that was the first to be thrown within the failed transaction:
	 * i.e. the application exception, if any, or the TransactionSystemException's own cause.
	 * @return the original exception, or <code>null</code> if there was none
	 */
	public Throwable getOriginalException() {
		return (this.applicationException != null ? this.applicationException : getCause());
	}

	@Override
	public boolean contains(Class exType) {
		return super.contains(exType) || (exType != null && exType.isInstance(this.applicationException));
	}

}
