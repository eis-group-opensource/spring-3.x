/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Mark Fisher
 */
public class ComponentScanParserWithUserDefinedStrategiesTests {

	@Test
	public void testCustomBeanNameGenerator() {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"org/springframework/context/annotation/customNameGeneratorTests.xml");
		assertTrue(context.containsBean("testing.fooServiceImpl"));
	}

	@Test
	public void testCustomScopeMetadataResolver() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"org/springframework/context/annotation/customScopeResolverTests.xml");
		BeanDefinition bd = context.getBeanFactory().getBeanDefinition("fooServiceImpl");
		assertEquals("myCustomScope", bd.getScope());
		assertFalse(bd.isSingleton());
	}

	@Test
	public void testInvalidConstructorBeanNameGenerator() {
		try {
			new ClassPathXmlApplicationContext(
					"org/springframework/context/annotation/invalidConstructorNameGeneratorTests.xml");
			fail("should have failed: no-arg constructor is required");
		}
		catch (BeansException e) {
			// expected
		}
	}

	@Test
	public void testInvalidClassNameScopeMetadataResolver() {
		try {
			new ClassPathXmlApplicationContext(
					"org/springframework/context/annotation/invalidClassNameScopeResolverTests.xml");
			fail("should have failed: no such class");
		}
		catch (BeansException e) {
			// expected
		}
	}
	
}