/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop;

import org.springframework.core.NestedRuntimeException;

/**
 * Exception that gets thrown when an AOP invocation failed
 * because of misconfiguration or unexpected runtime issues.
 *
 * @author Juergen Hoeller
 * @since 2.0
 */
public class AopInvocationException extends NestedRuntimeException {

	/**
	 * Constructor for AopInvocationException.
	 * @param msg the detail message
	 */
	public AopInvocationException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for AopInvocationException.
	 * @param msg the detail message
	 * @param cause the root cause
	 */
	public AopInvocationException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
