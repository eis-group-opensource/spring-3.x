/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.config.PlaceholderConfigurerSupport;
import org.springframework.beans.factory.config.PropertyOverrideConfigurer;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.env.MockEnvironment;

/**
 * @author Arjen Poutsma
 * @author Dave Syer
 * @author Chris Beams
 * @since 2.5.6
 */
public class ContextNamespaceHandlerTests {

	@Test
	public void propertyPlaceholder() throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"contextNamespaceHandlerTests-replace.xml", getClass());
		Map<String, PlaceholderConfigurerSupport> beans = applicationContext
				.getBeansOfType(PlaceholderConfigurerSupport.class);
		assertFalse("No PropertyPlaceholderConfigurer found", beans.isEmpty());
		String s = (String) applicationContext.getBean("string");
		assertEquals("No properties replaced", "bar", s);
	}

	@Test
	public void propertyPlaceholderSystemProperties() throws Exception {
		String value = System.setProperty("foo", "spam");
		try {
			ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
					"contextNamespaceHandlerTests-system.xml", getClass());
			Map<String, PropertyPlaceholderConfigurer> beans = applicationContext
					.getBeansOfType(PropertyPlaceholderConfigurer.class);
			assertFalse("No PropertyPlaceholderConfigurer found", beans.isEmpty());
			String s = (String) applicationContext.getBean("string");
			assertEquals("No properties replaced", "spam", s);
		} finally {
			if (value!=null) {
				System.setProperty("foo", value);
			}
		}
	}

	@Test
	public void propertyPlaceholderEnvironmentProperties() throws Exception {
		MockEnvironment env = new MockEnvironment().withProperty("foo", "spam");
		GenericXmlApplicationContext applicationContext = new GenericXmlApplicationContext();
		applicationContext.setEnvironment(env);
		applicationContext.load(new ClassPathResource("contextNamespaceHandlerTests-simple.xml", getClass()));
		applicationContext.refresh();
		Map<String, PlaceholderConfigurerSupport> beans = applicationContext
				.getBeansOfType(PlaceholderConfigurerSupport.class);
		assertFalse("No PropertyPlaceholderConfigurer found", beans.isEmpty());
		String s = (String) applicationContext.getBean("string");
		assertEquals("No properties replaced", "spam", s);
	}

	@Test
	public void propertyPlaceholderLocation() throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"contextNamespaceHandlerTests-location.xml", getClass());
		Map<String, PropertyPlaceholderConfigurer> beans = applicationContext
				.getBeansOfType(PropertyPlaceholderConfigurer.class);
		assertFalse("No PropertyPlaceholderConfigurer found", beans.isEmpty());
		String s = (String) applicationContext.getBean("foo");
		assertEquals("No properties replaced", "bar", s);
		s = (String) applicationContext.getBean("bar");
		assertEquals("No properties replaced", "foo", s);
		s = (String) applicationContext.getBean("spam");
		assertEquals("No properties replaced", "maps", s);
	}

	@Test
	public void propertyPlaceholderIgnored() throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"contextNamespaceHandlerTests-replace-ignore.xml", getClass());
		Map<String, PlaceholderConfigurerSupport> beans = applicationContext
				.getBeansOfType(PlaceholderConfigurerSupport.class);
		assertFalse("No PropertyPlaceholderConfigurer found", beans.isEmpty());
		String s = (String) applicationContext.getBean("string");
		assertEquals("Properties replaced", "${bar}", s);
	}

	@Test
	public void propertyOverride() throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"contextNamespaceHandlerTests-override.xml", getClass());
		Map<String, PropertyOverrideConfigurer> beans = applicationContext
				.getBeansOfType(PropertyOverrideConfigurer.class);
		assertFalse("No PropertyOverrideConfigurer found", beans.isEmpty());
		Date date = (Date) applicationContext.getBean("date");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		assertEquals("No properties overriden", 42, calendar.get(Calendar.MINUTE));
	}
}
