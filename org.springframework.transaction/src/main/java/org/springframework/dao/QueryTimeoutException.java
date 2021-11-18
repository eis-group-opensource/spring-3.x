/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.dao;

/**
 * Exception to be thrown on a query timeout. This could have different causes depending on
 * the database API in use but most likely thrown after the database interrupts or stops
 * the processing of a query before it has completed.
 *
 * <p>This exception can be thrown by user code trapping the native database exception or
 * by exception translation.
 *
 * @author Thomas Risberg
 * @since 3.1
 */
public class QueryTimeoutException extends TransientDataAccessException {

	/**
	 * Constructor for QueryTimeoutException.
	 * @param msg the detail message
	 */
	public QueryTimeoutException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for QueryTimeoutException.
	 * @param msg the detail message
	 * @param cause the root cause from the data access API in use
	 */
	public QueryTimeoutException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
