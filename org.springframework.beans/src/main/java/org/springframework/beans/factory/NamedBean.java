/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory;

/**
 * Counterpart of BeanNameAware. Returns the bean name of an object.
 *
 * <p>This interface can be introduced to avoid a brittle dependence
 * on bean name in objects used with Spring IoC and Spring AOP.
 *
 * @author Rod Johnson
 * @since 2.0
 * @see BeanNameAware
 */
public interface NamedBean {

	/**
	 * Return the name of this bean in a Spring bean factory.
	 */
	String getBeanName();

}
