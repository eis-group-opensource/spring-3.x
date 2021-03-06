/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.dao;

import org.springframework.core.NestedRuntimeException;

/**
 * Root of the hierarchy of data access exceptions discussed in
 * <a href="http://www.amazon.com/exec/obidos/tg/detail/-/0764543857/">Expert One-On-One J2EE Design and Development</a>.
 * Please see Chapter 9 of this book for detailed discussion of the
 * motivation for this package.
 *
 * <p>This exception hierarchy aims to let user code find and handle the
 * kind of error encountered without knowing the details of the particular
 * data access API in use (e.g. JDBC). Thus it is possible to react to an
 * optimistic locking failure without knowing that JDBC is being used.
 *
 * <p>As this class is a runtime exception, there is no need for user code
 * to catch it or subclasses if any error is to be considered fatal
 * (the usual case).
 *
 * @author Rod Johnson
 */
public abstract class DataAccessException extends NestedRuntimeException {

	/**
	 * Constructor for DataAccessException.
	 * @param msg the detail message
	 */
	public DataAccessException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for DataAccessException.
	 * @param msg the detail message
	 * @param cause the root cause (usually from using a underlying
	 * data access API such as JDBC)
	 */
	public DataAccessException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
