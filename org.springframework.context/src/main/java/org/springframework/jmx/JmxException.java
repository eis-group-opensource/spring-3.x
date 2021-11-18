/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx;

import org.springframework.core.NestedRuntimeException;

/**
 * General base exception to be thrown on JMX errors.
 * Unchecked since JMX failures are usually fatal.
 *
 * @author Juergen Hoeller
 * @since 2.0
 */
public class JmxException extends NestedRuntimeException {

	/**
	 * Constructor for JmxException.
	 * @param msg the detail message
	 */
	public JmxException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for JmxException.
	 * @param msg the detail message
	 * @param cause the root cause (usually a raw JMX API exception)
	 */
	public JmxException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
