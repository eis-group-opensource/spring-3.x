/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.scope;

import java.io.Serializable;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.util.Assert;

/**
 * Default implementation of the {@link ScopedObject} interface.
 * 
 * <p>Simply delegates the calls to the underlying
 * {@link ConfigurableBeanFactory bean factory}
 * ({@link ConfigurableBeanFactory#getBean(String)}/
 * {@link ConfigurableBeanFactory#destroyScopedBean(String)}).
 *
 * @author Juergen Hoeller
 * @since 2.0
 * @see org.springframework.beans.factory.BeanFactory#getBean
 * @see org.springframework.beans.factory.config.ConfigurableBeanFactory#destroyScopedBean
 */
public class DefaultScopedObject implements ScopedObject, Serializable {

	private final ConfigurableBeanFactory beanFactory;

	private final String targetBeanName;


	/**
	 * Creates a new instance of the {@link DefaultScopedObject} class.
	 * @param beanFactory the {@link ConfigurableBeanFactory} that holds the scoped target object
	 * @param targetBeanName the name of the target bean
	 */
	public DefaultScopedObject(ConfigurableBeanFactory beanFactory, String targetBeanName) {
		Assert.notNull(beanFactory, "BeanFactory must not be null");
		Assert.hasText(targetBeanName, "'targetBeanName' must not be empty");
		this.beanFactory = beanFactory;
		this.targetBeanName = targetBeanName;
	}


	public Object getTargetObject() {
		return this.beanFactory.getBean(this.targetBeanName);
	}

	public void removeFromScope() {
		this.beanFactory.destroyScopedBean(this.targetBeanName);
	}

}
