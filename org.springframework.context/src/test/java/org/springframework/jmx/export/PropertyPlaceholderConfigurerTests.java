/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export;

import javax.management.ObjectName;

import org.springframework.jmx.AbstractJmxTests;
import org.springframework.jmx.IJmxTestBean;

/**
 * @author Rob Harrop
 */
public class PropertyPlaceholderConfigurerTests extends AbstractJmxTests {

	protected String getApplicationContextPath() {
		return "org/springframework/jmx/export/propertyPlaceholderConfigurer.xml";
	}

	public void testPropertiesReplaced() {
		IJmxTestBean bean = (IJmxTestBean) getContext().getBean("testBean");

		assertEquals("Name is incorrect", "Rob Harrop", bean.getName());
		assertEquals("Age is incorrect", 100, bean.getAge());
	}

	public void testPropertiesCorrectInJmx() throws Exception {
		ObjectName oname = new ObjectName("bean:name=proxyTestBean1");
		Object name = getServer().getAttribute(oname, "Name");
		Integer age = (Integer) getServer().getAttribute(oname, "Age");

		assertEquals("Name is incorrect in JMX", "Rob Harrop", name);
		assertEquals("Age is incorrect in JMX", 100, age.intValue());
	}

}

