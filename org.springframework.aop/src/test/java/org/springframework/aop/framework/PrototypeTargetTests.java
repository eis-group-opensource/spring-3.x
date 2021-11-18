/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.framework;

import static org.junit.Assert.assertEquals;
import static test.util.TestResourceUtils.qualifiedResource;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.Resource;

/**
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 03.09.2004
 */
public final class PrototypeTargetTests {
	
	private static final Resource CONTEXT = qualifiedResource(PrototypeTargetTests.class, "context.xml");

	@Test
	public void testPrototypeProxyWithPrototypeTarget() {
		TestBeanImpl.constructionCount = 0;
		XmlBeanFactory xbf = new XmlBeanFactory(CONTEXT);
		for (int i = 0; i < 10; i++) {
			TestBean tb = (TestBean) xbf.getBean("testBeanPrototype");
			tb.doSomething();
		}
		TestInterceptor interceptor = (TestInterceptor) xbf.getBean("testInterceptor");
		assertEquals(10, TestBeanImpl.constructionCount);
		assertEquals(10, interceptor.invocationCount);
	}

	@Test
	public void testSingletonProxyWithPrototypeTarget() {
		TestBeanImpl.constructionCount = 0;
		XmlBeanFactory xbf = new XmlBeanFactory(CONTEXT);
		for (int i = 0; i < 10; i++) {
			TestBean tb = (TestBean) xbf.getBean("testBeanSingleton");
			tb.doSomething();
		}
		TestInterceptor interceptor = (TestInterceptor) xbf.getBean("testInterceptor");
		assertEquals(1, TestBeanImpl.constructionCount);
		assertEquals(10, interceptor.invocationCount);
	}


	public static interface TestBean {
		public void doSomething();
	}


	public static class TestBeanImpl implements TestBean {
		private static int constructionCount = 0;

		public TestBeanImpl() {
			constructionCount++;
		}

		public void doSomething() {
		}
	}


	public static class TestInterceptor implements MethodInterceptor {
		private int invocationCount = 0;

		public Object invoke(MethodInvocation methodInvocation) throws Throwable {
			invocationCount++;
			return methodInvocation.proceed();
		}
	}

}
