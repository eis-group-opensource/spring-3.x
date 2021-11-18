/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.interceptor;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.NamedBean;

import test.beans.ITestBean;
import test.beans.TestBean;

/**
 * @author Rod Johnson
 * @author Chris Beams
 */
public final class ExposeBeanNameAdvisorsTests {
	
	private class RequiresBeanNameBoundTestBean extends TestBean {
		private final String beanName;
	
		public RequiresBeanNameBoundTestBean(String beanName) {
			this.beanName = beanName;
		}
		
		public int getAge() {
			assertEquals(beanName, ExposeBeanNameAdvisors.getBeanName());
			return super.getAge();
		}
	}
	
	@Test
	public void testNoIntroduction() {
		String beanName = "foo";
		TestBean target = new RequiresBeanNameBoundTestBean(beanName);
		ProxyFactory pf = new ProxyFactory(target);
		pf.addAdvisor(ExposeInvocationInterceptor.ADVISOR);
		pf.addAdvisor(ExposeBeanNameAdvisors.createAdvisorWithoutIntroduction(beanName));
		ITestBean proxy = (ITestBean) pf.getProxy();
		
		assertFalse("No introduction", proxy instanceof NamedBean);
		// Requires binding
		proxy.getAge();
	}
	
	@Test
	public void testWithIntroduction() {
		String beanName = "foo";
		TestBean target = new RequiresBeanNameBoundTestBean(beanName);
		ProxyFactory pf = new ProxyFactory(target);
		pf.addAdvisor(ExposeInvocationInterceptor.ADVISOR);
		pf.addAdvisor(ExposeBeanNameAdvisors.createAdvisorIntroducingNamedBean(beanName));
		ITestBean proxy = (ITestBean) pf.getProxy();
		
		assertTrue("Introduction was made", proxy instanceof NamedBean);
		// Requires binding
		proxy.getAge();
		
		NamedBean nb = (NamedBean) proxy;
		assertEquals("Name returned correctly", beanName, nb.getBeanName());
	}

}
