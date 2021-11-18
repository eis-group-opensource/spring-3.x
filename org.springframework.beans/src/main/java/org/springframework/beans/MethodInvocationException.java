/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans;

import java.beans.PropertyChangeEvent;

/**
 * Thrown when a bean property getter or setter method throws an exception,
 * analogous to an InvocationTargetException.
 *
 * @author Rod Johnson
 */
public class MethodInvocationException extends PropertyAccessException {

	/**
	 * Error code that a method invocation error will be registered with.
	 */
	public static final String ERROR_CODE = "methodInvocation";
	

	/**
	 * Create a new MethodInvocationException.
	 * @param propertyChangeEvent PropertyChangeEvent that resulted in an exception
	 * @param cause the Throwable raised by the invoked method
	 */
	public MethodInvocationException(PropertyChangeEvent propertyChangeEvent, Throwable cause) {
		super(propertyChangeEvent, "Property '" + propertyChangeEvent.getPropertyName() + "' threw exception", cause);
	}

	public String getErrorCode() {
		return ERROR_CODE;
	}

}
