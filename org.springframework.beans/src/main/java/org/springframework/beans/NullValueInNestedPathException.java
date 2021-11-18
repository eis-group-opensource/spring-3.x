/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans;

/**
 * Exception thrown when navigation of a valid nested property
 * path encounters a NullPointerException.
 *
 * <p>For example, navigating "spouse.age" could fail because the
 * spouse property of the target object has a null value.
 *
 * @author Rod Johnson
 */
public class NullValueInNestedPathException extends InvalidPropertyException {

	/**
	 * Create a new NullValueInNestedPathException.
	 * @param beanClass the offending bean class
	 * @param propertyName the offending property
	 */
	public NullValueInNestedPathException(Class beanClass, String propertyName) {
		super(beanClass, propertyName, "Value of nested property '" + propertyName + "' is null");
	}

	/**
	 * Create a new NullValueInNestedPathException.
	 * @param beanClass the offending bean class
	 * @param propertyName the offending property
	 * @param msg the detail message
	 */
	public NullValueInNestedPathException(Class beanClass, String propertyName, String msg) {
		super(beanClass, propertyName, msg);
	}

}
