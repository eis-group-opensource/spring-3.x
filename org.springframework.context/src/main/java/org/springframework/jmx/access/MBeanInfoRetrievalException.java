/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.access;

import org.springframework.jmx.JmxException;

/**
 * Thrown if an exception is encountered when trying to retrieve
 * MBean metadata.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 1.2
 * @see MBeanClientInterceptor
 * @see MBeanProxyFactoryBean
 */
public class MBeanInfoRetrievalException extends JmxException {

	/**
	 * Create a new <code>MBeanInfoRetrievalException</code> with the
	 * specified error message.
	 * @param msg the detail message
	 */
	public MBeanInfoRetrievalException(String msg) {
		super(msg);
	}

	/**
	 * Create a new <code>MBeanInfoRetrievalException</code> with the
	 * specified error message and root cause.
	 * @param msg the detail message
	 * @param cause the root cause
	 */
	public MBeanInfoRetrievalException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
