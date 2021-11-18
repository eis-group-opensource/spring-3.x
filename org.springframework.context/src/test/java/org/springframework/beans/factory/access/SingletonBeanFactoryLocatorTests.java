/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.access;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.util.ClassUtils;

/**
 * @author Colin Sampaleanu
 * @author Chris Beams
 */
public class SingletonBeanFactoryLocatorTests {

	@Test
	public void testBasicFunctionality() {
		SingletonBeanFactoryLocator facLoc = new SingletonBeanFactoryLocator(
				"classpath*:" + ClassUtils.addResourcePathToPackagePath(getClass(), "ref1.xml"));
		
		basicFunctionalityTest(facLoc);
	}

	/**
	 * Worker method so subclass can use it too.
	 */
	protected void basicFunctionalityTest(SingletonBeanFactoryLocator facLoc) {
		BeanFactoryReference bfr = facLoc.useBeanFactory("a.qualified.name.of.some.sort");
		BeanFactory fac = bfr.getFactory();
		BeanFactoryReference bfr2 = facLoc.useBeanFactory("another.qualified.name");
		fac = bfr2.getFactory();
		// verify that the same instance is returned
		TestBean tb = (TestBean) fac.getBean("beans1.bean1");
		assertTrue(tb.getName().equals("beans1.bean1"));
		tb.setName("was beans1.bean1");
		BeanFactoryReference bfr3 = facLoc.useBeanFactory("another.qualified.name");
		fac = bfr3.getFactory();
		tb = (TestBean) fac.getBean("beans1.bean1");
		assertTrue(tb.getName().equals("was beans1.bean1"));
		BeanFactoryReference bfr4 = facLoc.useBeanFactory("a.qualified.name.which.is.an.alias");
		fac = bfr4.getFactory();
		tb = (TestBean) fac.getBean("beans1.bean1");
		assertTrue(tb.getName().equals("was beans1.bean1"));
		// Now verify that we can call release in any order.
		// Unfortunately this doesn't validate complete release after the last one.
		bfr2.release();
		bfr3.release();
		bfr.release();
		bfr4.release();
	}

	/**
	 * This test can run multiple times, but due to static keyed lookup of the locators,
	 * 2nd and subsequent calls will actuall get back same locator instance. This is not
	 * an issue really, since the contained beanfactories will still be loaded and released.
	 */
	@Test
	public void testGetInstance() {
		// Try with and without 'classpath*:' prefix, and with 'classpath:' prefix.
		BeanFactoryLocator facLoc = SingletonBeanFactoryLocator.getInstance(
				ClassUtils.addResourcePathToPackagePath(getClass(), "ref1.xml"));
		getInstanceTest1(facLoc);
		
		facLoc = SingletonBeanFactoryLocator.getInstance(
				"classpath*:/" + ClassUtils.addResourcePathToPackagePath(getClass(), "ref1.xml"));
		getInstanceTest2(facLoc);

		// This will actually get another locator instance, as the key is the resource name.
		facLoc = SingletonBeanFactoryLocator.getInstance(
				"classpath:" + ClassUtils.addResourcePathToPackagePath(getClass(), "ref1.xml"));
		getInstanceTest3(facLoc);
		
	}

	/**
	 * Worker method so subclass can use it too
	 */
	protected void getInstanceTest1(BeanFactoryLocator facLoc) {
		BeanFactoryReference bfr = facLoc.useBeanFactory("a.qualified.name.of.some.sort");
		BeanFactory fac = bfr.getFactory();
		BeanFactoryReference bfr2 = facLoc.useBeanFactory("another.qualified.name");
		fac = bfr2.getFactory();
		// verify that the same instance is returned
		TestBean tb = (TestBean) fac.getBean("beans1.bean1");
		assertTrue(tb.getName().equals("beans1.bean1"));
		tb.setName("was beans1.bean1");
		BeanFactoryReference bfr3 = facLoc.useBeanFactory("another.qualified.name");
		fac = bfr3.getFactory();
		tb = (TestBean) fac.getBean("beans1.bean1");
		assertTrue(tb.getName().equals("was beans1.bean1"));
		
		BeanFactoryReference bfr4 = facLoc.useBeanFactory("a.qualified.name.which.is.an.alias");
		fac = bfr4.getFactory();
		tb = (TestBean) fac.getBean("beans1.bean1");
		assertTrue(tb.getName().equals("was beans1.bean1"));
		
		bfr.release();
		bfr3.release();
		bfr2.release();
		bfr4.release();
	}

	/**
	 * Worker method so subclass can use it too
	 */
	protected void getInstanceTest2(BeanFactoryLocator facLoc) {
		BeanFactoryReference bfr;
		BeanFactory fac;
		BeanFactoryReference bfr2;
		TestBean tb;
		BeanFactoryReference bfr3;
		BeanFactoryReference bfr4;
		bfr = facLoc.useBeanFactory("a.qualified.name.of.some.sort");
		fac = bfr.getFactory();
		bfr2 = facLoc.useBeanFactory("another.qualified.name");
		fac = bfr2.getFactory();
		// verify that the same instance is returned
		tb = (TestBean) fac.getBean("beans1.bean1");
		assertTrue(tb.getName().equals("beans1.bean1"));
		tb.setName("was beans1.bean1");
		bfr3 = facLoc.useBeanFactory("another.qualified.name");
		fac = bfr3.getFactory();
		tb = (TestBean) fac.getBean("beans1.bean1");
		assertTrue(tb.getName().equals("was beans1.bean1"));
		bfr4 = facLoc.useBeanFactory("a.qualified.name.which.is.an.alias");
		fac = bfr4.getFactory();
		tb = (TestBean) fac.getBean("beans1.bean1");
		assertTrue(tb.getName().equals("was beans1.bean1"));
		bfr.release();
		bfr2.release();
		bfr4.release();
		bfr3.release();
	}
	
	/**
	 * Worker method so subclass can use it too
	 */
	protected void getInstanceTest3(BeanFactoryLocator facLoc) {
		BeanFactoryReference bfr;
		BeanFactory fac;
		BeanFactoryReference bfr2;
		TestBean tb;
		BeanFactoryReference bfr3;
		BeanFactoryReference bfr4;
		bfr = facLoc.useBeanFactory("a.qualified.name.of.some.sort");
		fac = bfr.getFactory();
		bfr2 = facLoc.useBeanFactory("another.qualified.name");
		fac = bfr2.getFactory();
		// verify that the same instance is returned
		tb = (TestBean) fac.getBean("beans1.bean1");
		assertTrue(tb.getName().equals("beans1.bean1"));
		tb.setName("was beans1.bean1");
		bfr3 = facLoc.useBeanFactory("another.qualified.name");
		fac = bfr3.getFactory();
		tb = (TestBean) fac.getBean("beans1.bean1");
		assertTrue(tb.getName().equals("was beans1.bean1"));
		bfr4 = facLoc.useBeanFactory("a.qualified.name.which.is.an.alias");
		fac = bfr4.getFactory();
		tb = (TestBean) fac.getBean("beans1.bean1");
		assertTrue(tb.getName().equals("was beans1.bean1"));
		bfr4.release();
		bfr3.release();
		bfr2.release();
		bfr.release();
	}

}
