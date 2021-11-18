/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.support;

import org.springframework.beans.FatalBeanException;

/**
 * Exception thrown when the validation of a bean definition failed.
 *
 * @author Juergen Hoeller
 * @since 21.11.2003
 * @see AbstractBeanDefinition#validate()
 */
public class BeanDefinitionValidationException extends FatalBeanException {

	/**
	 * Create a new BeanDefinitionValidationException with the specified message.
	 * @param msg the detail message
	 */
	public BeanDefinitionValidationException(String msg) {
		super(msg);
	}

	/**
	 * Create a new BeanDefinitionValidationException with the specified message
	 * and root cause.
	 * @param msg the detail message
	 * @param cause the root cause
	 */
	public BeanDefinitionValidationException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
