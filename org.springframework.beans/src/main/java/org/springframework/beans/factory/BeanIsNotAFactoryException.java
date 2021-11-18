/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory;

/**
 * Exception thrown when a bean is not a factory, but a user tries to get
 * at the factory for the given bean name. Whether a bean is a factory is
 * determined by whether it implements the FactoryBean interface.
 *
 * @author Rod Johnson
 * @since 10.03.2003
 * @see org.springframework.beans.factory.FactoryBean
 */
public class BeanIsNotAFactoryException extends BeanNotOfRequiredTypeException {

	/**
	 * Create a new BeanIsNotAFactoryException.
	 * @param name the name of the bean requested
	 * @param actualType the actual type returned, which did not match
	 * the expected type
	 */
	public BeanIsNotAFactoryException(String name, Class actualType) {
		super(name, FactoryBean.class, actualType);
	}

}
