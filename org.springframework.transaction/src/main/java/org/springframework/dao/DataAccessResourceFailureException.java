/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.dao;

/**
 * Data access exception thrown when a resource fails completely:
 * for example, if we can't connect to a database using JDBC.
 *
 * @author Rod Johnson
 * @author Thomas Risberg
 */
public class DataAccessResourceFailureException extends NonTransientDataAccessResourceException {

	/**
	 * Constructor for DataAccessResourceFailureException.
	 * @param msg the detail message
	 */
	public DataAccessResourceFailureException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for DataAccessResourceFailureException.
	 * @param msg the detail message
	 * @param cause the root cause from the data access API in use
	 */
	public DataAccessResourceFailureException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
