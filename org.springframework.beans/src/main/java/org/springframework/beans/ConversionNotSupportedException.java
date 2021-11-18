/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans;

import java.beans.PropertyChangeEvent;

/**
 * Exception thrown when no suitable editor or converter can be found for a bean property.
 *
 * @author Arjen Poutsma
 * @author Juergen Hoeller
 * @since 3.0
 */
public class ConversionNotSupportedException extends TypeMismatchException {

	/**
	 * Create a new ConversionNotSupportedException.
	 * @param propertyChangeEvent the PropertyChangeEvent that resulted in the problem
	 * @param requiredType the required target type (or <code>null</code> if not known)
	 * @param cause the root cause (may be <code>null</code>)
	 */
	public ConversionNotSupportedException(PropertyChangeEvent propertyChangeEvent, Class requiredType, Throwable cause) {
		super(propertyChangeEvent, requiredType, cause);
	}

	/**
	 * Create a new ConversionNotSupportedException.
	 * @param value the offending value that couldn't be converted (may be <code>null</code>)
	 * @param requiredType the required target type (or <code>null</code> if not known)
	 * @param cause the root cause (may be <code>null</code>)
	 */
	public ConversionNotSupportedException(Object value, Class requiredType, Throwable cause) {
		super(value, requiredType, cause);
	}

}
