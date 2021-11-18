/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory;

import org.springframework.beans.FatalBeanException;

/**
 * Exception that indicates an expression evaluation attempt having failed.
 *
 * @author Juergen Hoeller
 * @since 3.0
 */
public class BeanExpressionException extends FatalBeanException {

	/**
	 * Create a new BeanExpressionException with the specified message.
	 * @param msg the detail message
	 */
	public BeanExpressionException(String msg) {
		super(msg);
	}

	/**
	 * Create a new BeanExpressionException with the specified message
	 * and root cause.
	 * @param msg the detail message
	 * @param cause the root cause
	 */
	public BeanExpressionException(String msg, Throwable cause) {
		super(msg, cause);
	}

}