/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.dao;

/**
 * Exception thrown when an attempt to insert or update data
 * results in violation of an primary key or unique constraint.
 * Note that this is not necessarily a purely relational concept;
 * unique primary keys are required by most database types.
 *
 * @author Thomas Risberg
 */
public class DuplicateKeyException extends DataIntegrityViolationException {

	/**
	 * Constructor for DuplicateKeyException.
	 * @param msg the detail message
	 */
	public DuplicateKeyException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for DuplicateKeyException.
	 * @param msg the detail message
	 * @param cause the root cause from the data access API in use
	 */
	public DuplicateKeyException(String msg, Throwable cause) {
		super(msg, cause);
	}

}