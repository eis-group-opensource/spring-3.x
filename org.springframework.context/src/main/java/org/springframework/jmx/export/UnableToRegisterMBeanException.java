/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export;

/**
 * Exception thrown when we are unable to register an MBean,
 * for example because of a naming conflict.
 *
 * @author Rob Harrop
 * @since 2.0
 */
public class UnableToRegisterMBeanException extends MBeanExportException {

	/**
	 * Create a new <code>UnableToRegisterMBeanException</code> with the
	 * specified error message.
	 * @param msg the detail message
	 */
	public UnableToRegisterMBeanException(String msg) {
		super(msg);
	}

	/**
	 * Create a new <code>UnableToRegisterMBeanException</code> with the
	 * specified error message and root cause.
	 * @param msg the detail message
	 * @param cause the root caus
	 */
	public UnableToRegisterMBeanException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
