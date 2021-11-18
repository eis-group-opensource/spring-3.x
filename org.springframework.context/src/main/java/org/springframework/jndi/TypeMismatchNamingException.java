/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jndi;

import javax.naming.NamingException;

/**
 * Exception thrown if a type mismatch is encountered for an object
 * located in a JNDI environment. Thrown by JndiTemplate.
 *
 * @author Juergen Hoeller
 * @since 1.2.8
 * @see JndiTemplate#lookup(String, Class)
 */
public class TypeMismatchNamingException extends NamingException {

	private Class requiredType;

	private Class actualType;


	/**
	 * Construct a new TypeMismatchNamingException,
	 * building an explanation text from the given arguments.
	 * @param jndiName the JNDI name
	 * @param requiredType the required type for the lookup
	 * @param actualType the actual type that the lookup returned
	 */
	public TypeMismatchNamingException(String jndiName, Class requiredType, Class actualType) {
		super("Object of type [" + actualType + "] available at JNDI location [" +
				jndiName + "] is not assignable to [" + requiredType.getName() + "]");
		this.requiredType = requiredType;
		this.actualType = actualType;
	}

	/**
	 * Construct a new TypeMismatchNamingException.
	 * @param explanation the explanation text
	 */
	public TypeMismatchNamingException(String explanation) {
		super(explanation);
	}


	/**
	 * Return the required type for the lookup, if available.
	 */
	public final Class getRequiredType() {
		return this.requiredType;
	}

	/**
	 * Return the actual type that the lookup returned, if available.
	 */
	public final Class getActualType() {
		return this.actualType;
	}

}
