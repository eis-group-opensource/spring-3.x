/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.dao;

/**
 * Root of the hierarchy of data access exceptions that are considered transient -
 * where a previously failed operation might be able to succeed when the operation
 * is retried without any intervention by application-level functionality.
 *
 * @author Thomas Risberg
 * @since 2.5
 * @see java.sql.SQLTransientException
 */
public abstract class TransientDataAccessException extends DataAccessException {

	/**
	 * Constructor for TransientDataAccessException.
	 * @param msg the detail message
	 */
	public TransientDataAccessException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for TransientDataAccessException.
	 * @param msg the detail message
	 * @param cause the root cause (usually from using a underlying
	 * data access API such as JDBC)
	 */
	public TransientDataAccessException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
