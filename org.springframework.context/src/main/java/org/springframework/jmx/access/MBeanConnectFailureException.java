/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.access;

import org.springframework.jmx.JmxException;

/**
 * Thrown when an invocation failed because of an I/O problem on the
 * MBeanServerConnection.
 *
 * @author Juergen Hoeller
 * @since 2.5.6
 * @see MBeanClientInterceptor
 */
public class MBeanConnectFailureException extends JmxException {

	/**
	 * Create a new <code>MBeanConnectFailureException</code>
	 * with the specified error message and root cause.
	 * @param msg the detail message
	 * @param cause the root cause
	 */
	public MBeanConnectFailureException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
