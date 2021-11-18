/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.interceptor;

import static org.junit.Assert.*;
import static test.util.TestResourceUtils.qualifiedResource;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.Resource;

import test.beans.ITestBean;
import test.beans.TestBean;

/**
 * Non-XML tests are in AbstractAopProxyTests
 * 
 * @author Rod Johnson
 * @author Chris Beams
 */
public final class ExposeInvocationInterceptorTests {
	
	private static final Resource CONTEXT =
		qualifiedResource(ExposeInvocationInterceptorTests.class, "context.xml");

	@Test
	public void testXmlConfig() {
		XmlBeanFactory bf = new XmlBeanFactory(CONTEXT);
		ITestBean tb = (ITestBean) bf.getBean("proxy");
		String name= "tony";
		tb.setName(name);
		// Fires context checks
		assertEquals(name, tb.getName());
	}

}


abstract class ExposedInvocationTestBean extends TestBean {

	public String getName() {
		MethodInvocation invocation = ExposeInvocationInterceptor.currentInvocation();
		assertions(invocation);
		return super.getName();
	}

	public void absquatulate() {
		MethodInvocation invocation = ExposeInvocationInterceptor.currentInvocation();
		assertions(invocation);
		super.absquatulate();
	}
	
	protected abstract void assertions(MethodInvocation invocation);
}


class InvocationCheckExposedInvocationTestBean extends ExposedInvocationTestBean {
	protected void assertions(MethodInvocation invocation) {
		assertTrue(invocation.getThis() == this);
		assertTrue("Invocation should be on ITestBean: " + invocation.getMethod(), 
				ITestBean.class.isAssignableFrom(invocation.getMethod().getDeclaringClass()));
	}
}
