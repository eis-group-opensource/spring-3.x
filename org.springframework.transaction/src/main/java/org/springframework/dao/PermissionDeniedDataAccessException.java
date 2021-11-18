/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.dao;

/**
 * Exception thrown when the underlying resource denied a permission
 * to access a specific element, such as a specific database table.
 *
 * @author Juergen Hoeller
 * @since 2.0
 */
public class PermissionDeniedDataAccessException extends NonTransientDataAccessException {

	/**
	 * Constructor for PermissionDeniedDataAccessException.
	 * @param msg the detail message
	 * @param cause the root cause from the underlying data access API,
	 * such as JDBC
	 */
	public PermissionDeniedDataAccessException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
