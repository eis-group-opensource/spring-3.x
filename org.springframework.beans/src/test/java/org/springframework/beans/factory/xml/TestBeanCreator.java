/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.xml;

import test.beans.TestBean;

/**
 * Test class for Spring's ability to create
 * objects using static factory methods, rather
 * than constructors. 
 * @author Rod Johnson
 */
public class TestBeanCreator {
	
	public static TestBean createTestBean(String name, int age) {
		TestBean tb = new TestBean();
		tb.setName(name);
		tb.setAge(age);
		return tb;
	}
	
	public static TestBean createTestBean() {
		TestBean tb = new TestBean();
		tb.setName("Tristan");
		tb.setAge(2);
		return tb;
	}

}
