/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.xml;

import test.beans.TestBean;

/**
 * Test class for Spring's ability to create objects using
 * static factory methods, rather than constructors.
 *
 * @author Rod Johnson
 */
public class InstanceFactory {

	protected static int count = 0;

	private String factoryBeanProperty;

	public InstanceFactory() {
		count++;
	}

	public void setFactoryBeanProperty(String s) {
		this.factoryBeanProperty = s;
	}
	
	public String getFactoryBeanProperty() {
		return this.factoryBeanProperty;
	}
	
	public FactoryMethods defaultInstance() {
		TestBean tb = new TestBean();
		tb.setName(this.factoryBeanProperty);
		return FactoryMethods.newInstance(tb);
	}
	
	/**
	 * Note that overloaded methods are supported.
	 */
	public FactoryMethods newInstance(TestBean tb) {
		return FactoryMethods.newInstance(tb);
	}
	
	public FactoryMethods newInstance(TestBean tb, int num, String name) {
		return FactoryMethods.newInstance(tb, num, name);
	}
	
}
