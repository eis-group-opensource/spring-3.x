/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jca.cci;

import javax.resource.ResourceException;

import org.springframework.dao.DataAccessResourceFailureException;

/**
 * Exception thrown when the creating of a CCI Record failed
 * for connector-internal reasons.
 *
 * @author Juergen Hoeller
 * @since 1.2
 */
public class CannotCreateRecordException extends DataAccessResourceFailureException {

	/**
	 * Constructor for CannotCreateRecordException.
	 * @param msg message
	 * @param ex ResourceException root cause
	 */
	public CannotCreateRecordException(String msg, ResourceException ex) {
		super(msg, ex);
	}

}
