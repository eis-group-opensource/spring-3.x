/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBeanNotInitializedException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.SmartFactoryBean;

/**
 * FactoryBean that exposes an arbitrary target bean under a different name.
 *
 * <p>Usually, the target bean will reside in a different bean definition file,
 * using this FactoryBean to link it in and expose it under a different name.
 * Effectively, this corresponds to an alias for the target bean.
 *
 * <p><b>NOTE:</b> For XML bean definition files, an <code>&lt;alias&gt;</code>
 * tag is available that effectively achieves the same.
 *
 * <p>A special capability of this FactoryBean is enabled through its configuration
 * as bean definition: The "targetBeanName" can be substituted through a placeholder,
 * in combination with Spring's {@link PropertyPlaceholderConfigurer}.
 * Thanks to Marcus Bristav for pointing this out!
 *
 * @author Juergen Hoeller
 * @since 1.2
 * @see #setTargetBeanName
 * @see PropertyPlaceholderConfigurer
 */
public class BeanReferenceFactoryBean implements SmartFactoryBean, BeanFactoryAware {

	private String targetBeanName;

	private BeanFactory beanFactory;


	/**
	 * Set the name of the target bean.
	 * <p>This property is required. The value for this property can be
	 * substituted through a placeholder, in combination with Spring's
	 * PropertyPlaceholderConfigurer.
	 * @param targetBeanName the name of the target bean
	 * @see PropertyPlaceholderConfigurer
	 */
	public void setTargetBeanName(String targetBeanName) {
		this.targetBeanName = targetBeanName;
	}

	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
		if (this.targetBeanName == null) {
			throw new IllegalArgumentException("'targetBeanName' is required");
		}
		if (!this.beanFactory.containsBean(this.targetBeanName)) {
			throw new NoSuchBeanDefinitionException(this.targetBeanName, this.beanFactory.toString());
		}
	}


	public Object getObject() throws BeansException {
		if (this.beanFactory == null) {
			throw new FactoryBeanNotInitializedException();
		}
		return this.beanFactory.getBean(this.targetBeanName);
	}

	public Class getObjectType() {
		if (this.beanFactory == null) {
			return null;
		}
		return this.beanFactory.getType(this.targetBeanName);
	}

	public boolean isSingleton() {
		if (this.beanFactory == null) {
			throw new FactoryBeanNotInitializedException();
		}
		return this.beanFactory.isSingleton(this.targetBeanName);
	}

	public boolean isPrototype() {
		if (this.beanFactory == null) {
			throw new FactoryBeanNotInitializedException();
		}
		return this.beanFactory.isPrototype(this.targetBeanName);
	}

	public boolean isEagerInit() {
		return false;
	}

}
