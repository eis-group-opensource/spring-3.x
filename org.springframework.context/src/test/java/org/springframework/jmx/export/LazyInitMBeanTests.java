/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import junit.framework.TestCase;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jmx.support.ObjectNameManager;

/**
 * @author Rob Harrop
 * @author Juergen Hoeller
 */
public class LazyInitMBeanTests extends TestCase {

	public void testLazyInit() {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(getApplicationContextPath());
		ctx.close();
	}

	public void testInvokeOnLazyInitBean() throws Exception {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(getApplicationContextPath());
		assertFalse(ctx.getBeanFactory().containsSingleton("testBean"));
		assertFalse(ctx.getBeanFactory().containsSingleton("testBean2"));
		try {
			MBeanServer server = (MBeanServer) ctx.getBean("server");
			ObjectName oname = ObjectNameManager.getInstance("bean:name=testBean2");
			String name = (String) server.getAttribute(oname, "Name");
			assertEquals("Invalid name returned", "foo", name);
		}
		finally {
			ctx.close();
		}
	}

	private String getApplicationContextPath() {
		return "org/springframework/jmx/export/lazyInit.xml";
	}

}
