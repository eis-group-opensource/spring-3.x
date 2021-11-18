/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.framework;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.*;

import java.io.Serializable;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import org.springframework.aop.interceptor.ExposeInvocationInterceptor;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.IOther;
import org.springframework.beans.ITestBean;
import org.springframework.beans.TestBean;

/**
 * @since 13.03.2003
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Chris Beams
 */
@SuppressWarnings("serial")
public final class JdkDynamicProxyTests extends AbstractAopProxyTests implements Serializable {

	protected Object createProxy(ProxyCreatorSupport as) {
		assertFalse("Not forcible CGLIB", as.isProxyTargetClass());
		Object proxy = as.createAopProxy().getProxy();
		assertTrue("Should be a JDK proxy: " + proxy.getClass(), AopUtils.isJdkDynamicProxy(proxy));
		return proxy;
	}
	
	protected AopProxy createAopProxy(AdvisedSupport as) {
		return new JdkDynamicAopProxy(as);
	}
	
	public void testNullConfig() {
		try {
			new JdkDynamicAopProxy(null);
			fail("Shouldn't allow null interceptors");
		} 
		catch (IllegalArgumentException ex) {
			// Ok
		}
	}

	public void testProxyIsJustInterface() throws Throwable {
		TestBean raw = new TestBean();
		raw.setAge(32);
		AdvisedSupport pc = new AdvisedSupport(new Class[] {ITestBean.class});
		pc.setTarget(raw);
		JdkDynamicAopProxy aop = new JdkDynamicAopProxy(pc);

		Object proxy = aop.getProxy();
		assertTrue(proxy instanceof ITestBean);
		assertTrue(!(proxy instanceof TestBean));
	}

	public void testInterceptorIsInvokedWithNoTarget() throws Throwable {
		// Test return value
		int age = 25;
		MethodInterceptor mi = createMock(MethodInterceptor.class);

		AdvisedSupport pc = new AdvisedSupport(new Class[] { ITestBean.class });
		pc.addAdvice(mi);
		AopProxy aop = createAopProxy(pc);

		expect(mi.invoke(null)).andReturn(age);
		replay(mi);

		ITestBean tb = (ITestBean) aop.getProxy();
		assertTrue("correct return value", tb.getAge() == age);
		verify(mi);
	}
	
	public void testTargetCanGetInvocationWithPrivateClass() throws Throwable {
		final ExposedInvocationTestBean expectedTarget = new ExposedInvocationTestBean() {
			protected void assertions(MethodInvocation invocation) {
				assertTrue(invocation.getThis() == this);
				assertTrue("Invocation should be on ITestBean: " + invocation.getMethod(), 
					invocation.getMethod().getDeclaringClass() == ITestBean.class);
			}
		};
	
		AdvisedSupport pc = new AdvisedSupport(new Class[] { ITestBean.class, IOther.class });
		pc.addAdvice(ExposeInvocationInterceptor.INSTANCE);
		TrapTargetInterceptor tii = new TrapTargetInterceptor() {
			public Object invoke(MethodInvocation invocation) throws Throwable {
				// Assert that target matches BEFORE invocation returns
				assertEquals("Target is correct", expectedTarget, invocation.getThis());
				return super.invoke(invocation);
			}
		};
		pc.addAdvice(tii);
		pc.setTarget(expectedTarget);
		AopProxy aop = createAopProxy(pc);

		ITestBean tb = (ITestBean) aop.getProxy();
		tb.getName();
		// Not safe to trap invocation
		//assertTrue(tii.invocation == target.invocation);
	
		//assertTrue(target.invocation.getProxy() == tb);

		//	((IOther) tb).absquatulate();
		//MethodInvocation minv =  tii.invocation;
		//assertTrue("invoked on iother, not " + minv.getMethod().getDeclaringClass(), minv.getMethod().getDeclaringClass() == IOther.class);
		//assertTrue(target.invocation == tii.invocation);
	}

	public void testProxyNotWrappedIfIncompatible() {
		FooBar bean = new FooBar();
		ProxyCreatorSupport as = new ProxyCreatorSupport();
		as.setInterfaces(new Class[] {Foo.class});
		as.setTarget(bean);

		Foo proxy = (Foo) createProxy(as);
		assertSame("Target should be returned when return types are incompatible", bean, proxy.getBarThis());
		assertSame("Proxy should be returned when return types are compatible", proxy, proxy.getFooThis());

	}

	public void testEqualsAndHashCodeDefined() throws Exception {
		AdvisedSupport as = new AdvisedSupport(new Class[]{Named.class});
		as.setTarget(new Person());
		JdkDynamicAopProxy aopProxy = new JdkDynamicAopProxy(as);
		Named proxy = (Named) aopProxy.getProxy();
		Named named = new Person();
		assertEquals("equals() returned false", proxy, named);
		assertEquals("hashCode() not equal", proxy.hashCode(), named.hashCode());
	}


	public static interface Foo {

		Bar getBarThis();

		Foo getFooThis();
	}


	public static interface Bar {

	}


	public static class FooBar implements Foo, Bar {

		public Bar getBarThis() {
			return this;
		}

		public Foo getFooThis() {
			return this;
		}
	}


	public static interface Named {

		String getName();

		boolean equals(Object other);

		int hashCode();
	}


	public static class Person implements Named {

		private final String name = "Rob Harrop";

		public String getName() {
			return this.name;
		}

		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			final Person person = (Person) o;

			if (!name.equals(person.name)) return false;

			return true;
		}

		public int hashCode() {
			return name.hashCode();
		}
	}

}
