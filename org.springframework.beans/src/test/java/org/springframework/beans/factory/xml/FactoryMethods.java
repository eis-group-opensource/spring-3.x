/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.xml;

import java.util.Collections;
import java.util.List;

import test.beans.TestBean;

/**
 * Test class for Spring's ability to create objects using static
 * factory methods, rather than constructors.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 */
public class FactoryMethods {
	
	public static FactoryMethods nullInstance() {
		return null;
	}

	public static FactoryMethods defaultInstance() {
		TestBean tb = new TestBean();
		tb.setName("defaultInstance");
		return new FactoryMethods(tb, "default", 0);
	}
	
	/**
	 * Note that overloaded methods are supported.
	 */
	public static FactoryMethods newInstance(TestBean tb) {
		return new FactoryMethods(tb, "default", 0);
	}
	
	protected static FactoryMethods newInstance(TestBean tb, int num, String name) {
		if (name == null) {
			throw new IllegalStateException("Should never be called with null value");
		}
		return new FactoryMethods(tb, name, num);
	}
	
	static FactoryMethods newInstance(TestBean tb, int num, Integer something) {
		if (something != null) {
			throw new IllegalStateException("Should never be called with non-null value");
		}
		return new FactoryMethods(tb, null, num);
	}

	private static List listInstance() {
		return Collections.EMPTY_LIST;
	}


	private int num = 0;
	private String name = "default";
	private TestBean tb;
	private String stringValue;


	/**
	 * Constructor is private: not for use outside this class,
	 * even by IoC container.
	 */
	private FactoryMethods(TestBean tb, String name, int num) {
		this.tb = tb;
		this.name = name;
		this.num = num;
	}
	
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}
	
	public String getStringValue() {
		return this.stringValue;
	}
	
	public TestBean getTestBean() {
		return this.tb;
	}
	
	protected TestBean protectedGetTestBean() {
		return this.tb;
	}
	
	private TestBean privateGetTestBean() {
		return this.tb;
	}
	
	public int getNum() {
		return num;
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * Set via Setter Injection once instance is created.
	 */
	public void setName(String name) {
		this.name = name;
	}

}
