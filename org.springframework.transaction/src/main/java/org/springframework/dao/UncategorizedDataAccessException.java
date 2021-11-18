/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.dao;

/**
 * Normal superclass when we can't distinguish anything more specific
 * than "something went wrong with the underlying resource": for example,
 * a SQLException from JDBC we can't pinpoint more precisely.
 *
 * @author Rod Johnson
 */
public abstract class UncategorizedDataAccessException extends NonTransientDataAccessException {

	/**
	 * Constructor for UncategorizedDataAccessException.
	 * @param msg the detail message
	 * @param cause the exception thrown by underlying data access API
	 */
	public UncategorizedDataAccessException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
