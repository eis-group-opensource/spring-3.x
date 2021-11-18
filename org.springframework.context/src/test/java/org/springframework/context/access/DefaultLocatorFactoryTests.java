/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.access;

import junit.framework.TestCase;

import org.springframework.beans.factory.access.BeanFactoryLocator;

/**
 * @author Colin Sampaleanu
 */
public class DefaultLocatorFactoryTests extends TestCase {

	/*
	 * Class to test for BeanFactoryLocator getInstance()
	 */
	public void testGetInstance() {
		BeanFactoryLocator bf = DefaultLocatorFactory.getInstance();
		BeanFactoryLocator bf2 = DefaultLocatorFactory.getInstance();
		assertTrue(bf.equals(bf2));
	}

	/*
	 * Class to test for BeanFactoryLocator getInstance(String)
	 */
	public void testGetInstanceString() {
		BeanFactoryLocator bf = DefaultLocatorFactory.getInstance("my-bean-refs.xml");
		BeanFactoryLocator bf2 = DefaultLocatorFactory.getInstance("my-bean-refs.xml");
		assertTrue(bf.equals(bf2));
	}
}
