/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx;

/**
 * Exception thrown when we cannot locate an instance of an <code>MBeanServer</code>,
 * or when more than one instance is found.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 1.2
 * @see org.springframework.jmx.support.JmxUtils#locateMBeanServer
 */
public class MBeanServerNotFoundException extends JmxException {

	/**
	 * Create a new <code>MBeanServerNotFoundException</code> with the
	 * supplied error message.
	 * @param msg the error message
	 */
	public MBeanServerNotFoundException(String msg) {
		super(msg);
	}

	/**
	 * Create a new <code>MBeanServerNotFoundException</code> with the
	 * specified error message and root cause.
	 * @param msg the error message
	 * @param cause the root cause
	 */
	public MBeanServerNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
