/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.support;

import java.util.Arrays;

import junit.framework.TestCase;

import test.beans.TestBean;

/**
 * @author Rod Johnson
 * @author Juergen Hoeller
 */
public class BeanDefinitionBuilderTests extends TestCase {

	public void testBeanClassWithSimpleProperty() {
		String[] dependsOn = new String[] { "A", "B", "C" };
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition(TestBean.class);
		bdb.setSingleton(false).addPropertyReference("age", "15");
		for (int i = 0; i < dependsOn.length; i++) {
			bdb.addDependsOn(dependsOn[i]);
		}

		RootBeanDefinition rbd = (RootBeanDefinition) bdb.getBeanDefinition();
		assertFalse(rbd.isSingleton());
		assertEquals(TestBean.class, rbd.getBeanClass());
		assertTrue("Depends on was added", Arrays.equals(dependsOn, rbd.getDependsOn()));
		assertTrue(rbd.getPropertyValues().contains("age"));
	}

	public void testBeanClassWithFactoryMethod() {
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition(TestBean.class, "create");
		RootBeanDefinition rbd = (RootBeanDefinition) bdb.getBeanDefinition();
		assertTrue(rbd.hasBeanClass());
		assertEquals(TestBean.class, rbd.getBeanClass());
		assertEquals("create", rbd.getFactoryMethodName());
	}

	public void testBeanClassName() {
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition(TestBean.class.getName());
		RootBeanDefinition rbd = (RootBeanDefinition) bdb.getBeanDefinition();
		assertFalse(rbd.hasBeanClass());
		assertEquals(TestBean.class.getName(), rbd.getBeanClassName());
	}

	public void testBeanClassNameWithFactoryMethod() {
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition(TestBean.class.getName(), "create");
		RootBeanDefinition rbd = (RootBeanDefinition) bdb.getBeanDefinition();
		assertFalse(rbd.hasBeanClass());
		assertEquals(TestBean.class.getName(), rbd.getBeanClassName());
		assertEquals("create", rbd.getFactoryMethodName());
	}

}
