/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.AbstractComponentDefinition;
import org.springframework.util.Assert;

/**
 * {@link org.springframework.beans.factory.parsing.ComponentDefinition}
 * implementation that holds a pointcut definition.
 *
 * @author Rob Harrop
 * @since 2.0
 */
public class PointcutComponentDefinition extends AbstractComponentDefinition {

	private final String pointcutBeanName;

	private final BeanDefinition pointcutDefinition;

	private final String description;


	public PointcutComponentDefinition(String pointcutBeanName, BeanDefinition pointcutDefinition, String expression) {
		Assert.notNull(pointcutBeanName, "Bean name must not be null");
		Assert.notNull(pointcutDefinition, "Pointcut definition must not be null");
		Assert.notNull(expression, "Expression must not be null");
		this.pointcutBeanName = pointcutBeanName;
		this.pointcutDefinition = pointcutDefinition;
		this.description = "Pointcut <name='" + pointcutBeanName + "', expression=[" + expression + "]>";
	}


	public String getName() {
		return this.pointcutBeanName;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public BeanDefinition[] getBeanDefinitions() {
		return new BeanDefinition[] {this.pointcutDefinition};
	}

	public Object getSource() {
		return this.pointcutDefinition.getSource();
	}

}
