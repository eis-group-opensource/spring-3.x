/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.access;

import javax.management.JMRuntimeException;

/**
 * Thrown when trying to invoke an operation on a proxy that is not exposed
 * by the proxied MBean resource's management interface.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 1.2
 * @see MBeanClientInterceptor
 */
public class InvalidInvocationException extends JMRuntimeException {

	/**
	 * Create a new <code>InvalidInvocationException</code> with the supplied
	 * error message.
	 * @param msg the detail message
	 */
	public InvalidInvocationException(String msg) {
		super(msg);
	}

}
