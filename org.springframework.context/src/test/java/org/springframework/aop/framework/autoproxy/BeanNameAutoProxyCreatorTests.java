/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.framework.autoproxy;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.ITestBean;
import org.springframework.beans.TestBean;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import test.advice.CountingBeforeAdvice;
import test.interceptor.NopInterceptor;
import test.mixin.Lockable;
import test.mixin.LockedException;
import test.util.TimeStamped;

/**
 * @author Rod Johnson
 * @author Rob Harrop
 * @author Chris Beams
 */
public class BeanNameAutoProxyCreatorTests {

	private BeanFactory beanFactory;

	@Before
	public void setUp() throws IOException {
		// Note that we need an ApplicationContext, not just a BeanFactory,
		// for post-processing and hence auto-proxying to work.
		beanFactory =
			new ClassPathXmlApplicationContext(getClass().getSimpleName() + "-context.xml", getClass());
	}

	@Test
	public void testNoProxy() {
		TestBean tb = (TestBean) beanFactory.getBean("noproxy");
		assertFalse(AopUtils.isAopProxy(tb));
		assertEquals("noproxy", tb.getName());
	}

	@Test
	public void testJdkProxyWithExactNameMatch() {
		ITestBean tb = (ITestBean) beanFactory.getBean("onlyJdk");
		jdkAssertions(tb, 1);
		assertEquals("onlyJdk", tb.getName());
	}

	@Test
	public void testJdkProxyWithDoubleProxying() {
		ITestBean tb = (ITestBean) beanFactory.getBean("doubleJdk");
		jdkAssertions(tb, 2);
		assertEquals("doubleJdk", tb.getName());
	}

	@Test
	public void testJdkIntroduction() {
		ITestBean tb = (ITestBean) beanFactory.getBean("introductionUsingJdk");
		NopInterceptor nop = (NopInterceptor) beanFactory.getBean("introductionNopInterceptor");
		assertEquals(0, nop.getCount());
		assertTrue(AopUtils.isJdkDynamicProxy(tb));
		int age = 5;
		tb.setAge(age);
		assertEquals(age, tb.getAge());
		assertTrue("Introduction was made", tb instanceof TimeStamped);
		assertEquals(0, ((TimeStamped) tb).getTimeStamp());
		assertEquals(3, nop.getCount());		
		assertEquals("introductionUsingJdk", tb.getName());

		ITestBean tb2 = (ITestBean) beanFactory.getBean("second-introductionUsingJdk");

		// Check two per-instance mixins were distinct
		Lockable lockable1 = (Lockable) tb;
		Lockable lockable2 = (Lockable) tb2;
		assertFalse(lockable1.locked());
		assertFalse(lockable2.locked());
		tb.setAge(65);
		assertEquals(65, tb.getAge());
		lockable1.lock();
		assertTrue(lockable1.locked());
		// Shouldn't affect second
		assertFalse(lockable2.locked());
		// Can still mod second object
		tb2.setAge(12);
		// But can't mod first
		try {
			tb.setAge(6);
			fail("Mixin should have locked this object");
		}
		catch (LockedException ex) {
			// Ok
		}
	}

	@Test
	public void testJdkIntroductionAppliesToCreatedObjectsNotFactoryBean() {
		ITestBean tb = (ITestBean) beanFactory.getBean("factory-introductionUsingJdk");
		NopInterceptor nop = (NopInterceptor) beanFactory.getBean("introductionNopInterceptor");
		assertEquals("NOP should not have done any work yet", 0, nop.getCount());
		assertTrue(AopUtils.isJdkDynamicProxy(tb));
		int age = 5;
		tb.setAge(age);
		assertEquals(age, tb.getAge());
		assertTrue("Introduction was made", tb instanceof TimeStamped);
		assertEquals(0, ((TimeStamped) tb).getTimeStamp());
		assertEquals(3, nop.getCount());		
	
		ITestBean tb2 = (ITestBean) beanFactory.getBean("second-introductionUsingJdk");
			
		// Check two per-instance mixins were distinct
		Lockable lockable1 = (Lockable) tb;
		Lockable lockable2 = (Lockable) tb2;
		assertFalse(lockable1.locked());
		assertFalse(lockable2.locked());
		tb.setAge(65);
		assertEquals(65, tb.getAge());
		lockable1.lock();
		assertTrue(lockable1.locked());
		// Shouldn't affect second
		assertFalse(lockable2.locked());
		// Can still mod second object
		tb2.setAge(12);
		// But can't mod first
		try {
			tb.setAge(6);
			fail("Mixin should have locked this object");
		}
		catch (LockedException ex) {
			// Ok
		}
	}

	@Test
	public void testJdkProxyWithWildcardMatch() {
		ITestBean tb = (ITestBean) beanFactory.getBean("jdk1");
		jdkAssertions(tb, 1);
		assertEquals("jdk1", tb.getName());
	}

	@Test
	public void testCglibProxyWithWildcardMatch() {
		TestBean tb = (TestBean) beanFactory.getBean("cglib1");
		cglibAssertions(tb);
		assertEquals("cglib1", tb.getName());
	}

	@Test
	public void testWithFrozenProxy() {
		ITestBean testBean = (ITestBean) beanFactory.getBean("frozenBean");
		assertTrue(((Advised)testBean).isFrozen());
	}

	private void jdkAssertions(ITestBean tb, int nopInterceptorCount)  {
		NopInterceptor nop = (NopInterceptor) beanFactory.getBean("nopInterceptor");
		assertEquals(0, nop.getCount());
		assertTrue(AopUtils.isJdkDynamicProxy(tb));
		int age = 5;
		tb.setAge(age);
		assertEquals(age, tb.getAge());
		assertEquals(2 * nopInterceptorCount, nop.getCount());
	}

	/**
	 * Also has counting before advice.
	 */
	private void cglibAssertions(TestBean tb) {
		CountingBeforeAdvice cba = (CountingBeforeAdvice) beanFactory.getBean("countingBeforeAdvice");
		NopInterceptor nop = (NopInterceptor) beanFactory.getBean("nopInterceptor");
		assertEquals(0, cba.getCalls());
		assertEquals(0, nop.getCount());
		assertTrue(AopUtils.isCglibProxy(tb));
		int age = 5;
		tb.setAge(age);
		assertEquals(age, tb.getAge());
		assertEquals(2, nop.getCount());
		assertEquals(2, cba.getCalls());		
	}

}


class CreatesTestBean implements FactoryBean<Object> {

	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return new TestBean();
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class<?> getObjectType() {
		return TestBean.class;
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

}