/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.access;

import org.springframework.jmx.JmxException;

/**
 * Thrown when an invocation on an MBean resource failed with an exception (either
 * a reflection exception or an exception thrown by the target method itself).
 *
 * @author Juergen Hoeller
 * @since 1.2
 * @see MBeanClientInterceptor
 */
public class InvocationFailureException extends JmxException {

	/**
	 * Create a new <code>InvocationFailureException</code> with the supplied
	 * error message.
	 * @param msg the detail message
	 */
	public InvocationFailureException(String msg) {
		super(msg);
	}

	/**
	 * Create a new <code>InvocationFailureException</code> with the
	 * specified error message and root cause.
	 * @param msg the detail message
	 * @param cause the root cause
	 */
	public InvocationFailureException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
