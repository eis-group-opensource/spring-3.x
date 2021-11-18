/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.config;

import static org.junit.Assert.*;
import org.junit.Test;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * @author Arjen Poutsma
 */
public class DeprecatedBeanWarnerTests {

	private DefaultListableBeanFactory beanFactory;

	private String beanName;

	private BeanDefinition beanDefinition;

	private DeprecatedBeanWarner warner;

	@Test
	public void postProcess() {
		beanFactory = new DefaultListableBeanFactory();
		BeanDefinition def = BeanDefinitionBuilder
			.genericBeanDefinition(MyDeprecatedBean.class)
			.getBeanDefinition();
		String beanName = "deprecated";
		beanFactory.registerBeanDefinition(beanName, def);

		warner = new MyDeprecatedBeanWarner();
		warner.postProcessBeanFactory(beanFactory);
		assertEquals(beanName, this.beanName);
		assertEquals(def, this.beanDefinition);

	}

	private class MyDeprecatedBeanWarner extends DeprecatedBeanWarner {


		@Override
		protected void logDeprecatedBean(String beanName, BeanDefinition beanDefinition) {
			DeprecatedBeanWarnerTests.this.beanName = beanName;
			DeprecatedBeanWarnerTests.this.beanDefinition = beanDefinition;
		}
	}


}
