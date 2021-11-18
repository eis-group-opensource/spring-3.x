/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.access;

import static org.junit.Assert.*;
import org.junit.Test;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.beans.factory.access.SingletonBeanFactoryLocatorTests;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ClassUtils;

/**
 * @author Colin Sampaleanu
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public class ContextSingletonBeanFactoryLocatorTests extends SingletonBeanFactoryLocatorTests {
	
	private static final Class<?> CLASS = ContextSingletonBeanFactoryLocatorTests.class;
	private static final String CONTEXT = CLASS.getSimpleName() + "-context.xml";
	

	@Test
	public void testBaseBeanFactoryDefs() {
		// Just test the base BeanFactory/AppContext defs we are going to work
		// with in other tests.
		new XmlBeanFactory(new ClassPathResource("/org/springframework/beans/factory/access/beans1.xml"));
		new XmlBeanFactory(new ClassPathResource("/org/springframework/beans/factory/access/beans2.xml"));
	}

	@Test
	public void testBasicFunctionality() {
		ContextSingletonBeanFactoryLocator facLoc = new ContextSingletonBeanFactoryLocator(
				"classpath*:" + ClassUtils.addResourcePathToPackagePath(CLASS, CONTEXT));
		
		basicFunctionalityTest(facLoc);

		BeanFactoryReference bfr = facLoc.useBeanFactory("a.qualified.name.of.some.sort");
		BeanFactory fac = bfr.getFactory();
		assertTrue(fac instanceof ApplicationContext);
		assertEquals("a.qualified.name.of.some.sort", ((ApplicationContext) fac).getId());
		assertTrue(((ApplicationContext) fac).getDisplayName().contains("a.qualified.name.of.some.sort"));
		BeanFactoryReference bfr2 = facLoc.useBeanFactory("another.qualified.name");
		BeanFactory fac2 = bfr2.getFactory();
		assertEquals("another.qualified.name", ((ApplicationContext) fac2).getId());
		assertTrue(((ApplicationContext) fac2).getDisplayName().contains("another.qualified.name"));
		assertTrue(fac2 instanceof ApplicationContext);
	}

	/**
	 * This test can run multiple times, but due to static keyed lookup of the locators,
	 * 2nd and subsequent calls will actually get back same locator instance. This is not
	 * really an issue, since the contained bean factories will still be loaded and released.
	 */
	@Test
	public void testGetInstance() {
		// Try with and without 'classpath*:' prefix, and with 'classpath:' prefix.
		BeanFactoryLocator facLoc = ContextSingletonBeanFactoryLocator.getInstance(
				ClassUtils.addResourcePathToPackagePath(CLASS, CONTEXT));
		getInstanceTest1(facLoc);
		
		facLoc = ContextSingletonBeanFactoryLocator.getInstance(
				"classpath*:" + ClassUtils.addResourcePathToPackagePath(CLASS, CONTEXT));
		getInstanceTest2(facLoc);

		// This will actually get another locator instance, as the key is the resource name.
		facLoc = ContextSingletonBeanFactoryLocator.getInstance(
				"classpath:" + ClassUtils.addResourcePathToPackagePath(CLASS, CONTEXT));
		getInstanceTest3(facLoc);
	}

}
