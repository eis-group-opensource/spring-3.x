/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export;

import org.springframework.jmx.JmxException;

/**
 * Exception thrown in case of failure when exporting an MBean.
 *
 * @author Rob Harrop
 * @since 2.0
 * @see MBeanExportOperations
 */
public class MBeanExportException extends JmxException {

	/**
	 * Create a new <code>MBeanExportException</code> with the
	 * specified error message.
	 * @param msg the detail message
	 */
	public MBeanExportException(String msg) {
		super(msg);
	}

	/**
	 * Create a new <code>MBeanExportException</code> with the
	 * specified error message and root cause.
	 * @param msg the detail message
	 * @param cause the root cause
	 */
	public MBeanExportException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
