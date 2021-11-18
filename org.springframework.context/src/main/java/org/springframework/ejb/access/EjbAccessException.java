/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.ejb.access;

import org.springframework.core.NestedRuntimeException;

/**
 * Exception that gets thrown when an EJB stub cannot be accessed properly.
 *
 * @author Juergen Hoeller
 * @since 2.0
 */
public class EjbAccessException extends NestedRuntimeException {

	/**
	 * Constructor for EjbAccessException.
	 * @param msg the detail message
	 */
	public EjbAccessException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for EjbAccessException.
	 * @param msg the detail message
	 * @param cause the root cause
	 */
	public EjbAccessException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
