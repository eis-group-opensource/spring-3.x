/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory;

import org.springframework.beans.BeansException;
import org.springframework.util.StringUtils;

/**
 * Exception thrown when a BeanFactory is asked for a bean
 * instance name for which it cannot find a definition.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 */
public class NoSuchBeanDefinitionException extends BeansException {

	/** Name of the missing bean */
	private String beanName;

	/** Required bean type */
	private Class beanType;


	/**
	 * Create a new NoSuchBeanDefinitionException.
	 * @param name the name of the missing bean
	 */
	public NoSuchBeanDefinitionException(String name) {
		super("No bean named '" + name + "' is defined");
		this.beanName = name;
	}

	/**
	 * Create a new NoSuchBeanDefinitionException.
	 * @param name the name of the missing bean
	 * @param message detailed message describing the problem
	 */
	public NoSuchBeanDefinitionException(String name, String message) {
		super("No bean named '" + name + "' is defined: " + message);
		this.beanName = name;
	}

	/**
	 * Create a new NoSuchBeanDefinitionException.
	 * @param type required type of bean
	 */
	public NoSuchBeanDefinitionException(Class type) {
		super("No unique bean of type [" + type.getName() + "] is defined");
		this.beanType = type;
	}

	/**
	 * Create a new NoSuchBeanDefinitionException.
	 * @param type required type of bean
	 * @param message detailed message describing the problem
	 */
	public NoSuchBeanDefinitionException(Class type, String message) {
		super("No unique bean of type [" + type.getName() + "] is defined: " + message);
		this.beanType = type;
	}
	
	/**
	 * Create a new NoSuchBeanDefinitionException.
	 * @param type required type of bean
	 * @param dependencyDescription a description of the originating dependency
	 * @param message detailed message describing the problem
	 */
	public NoSuchBeanDefinitionException(Class type, String dependencyDescription, String message) {
		super("No matching bean of type [" + type.getName() + "] found for dependency" +
				(StringUtils.hasLength(dependencyDescription) ? " [" + dependencyDescription + "]" : "") +
				": " + message);
		this.beanType = type;
	}


	/**
	 * Return the name of the missing bean,
	 * if it was a lookup by name that failed.
	 */
	public String getBeanName() {
		return this.beanName;
	}

	/**
	 * Return the required type of bean,
	 * if it was a lookup by type that failed.
	 */
	public Class getBeanType() {
		return this.beanType;
	}

}
