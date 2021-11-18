/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.access;

import org.springframework.beans.FatalBeanException;

/**
 * Exception thrown if a bean factory could not be loaded by a bootstrap class.
 *
 * @author Rod Johnson
 * @since 02.12.2002
 */
public class BootstrapException extends FatalBeanException {

	/**
	 * Create a new BootstrapException with the specified message.
	 * @param msg the detail message
	 */
	public BootstrapException(String msg) {
		super(msg);
	}

	/**
	 * Create a new BootstrapException with the specified message
	 * and root cause.
	 * @param msg the detail message
	 * @param cause the root cause
	 */
	public BootstrapException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
