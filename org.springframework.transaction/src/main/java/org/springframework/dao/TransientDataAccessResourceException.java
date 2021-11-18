/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.dao;

/**
 * Data access exception thrown when a resource fails temporarily
 * and the operation can be retried.
 *
 * @author Thomas Risberg
 * @since 2.5
 * @see java.sql.SQLTransientConnectionException
 */
public class TransientDataAccessResourceException extends TransientDataAccessException {

	/**
	 * Constructor for TransientDataAccessResourceException.
	 * @param msg the detail message
	 */
	public TransientDataAccessResourceException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for TransientDataAccessResourceException.
	 * @param msg the detail message
	 * @param cause the root cause from the data access API in use
	 */
	public TransientDataAccessResourceException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
