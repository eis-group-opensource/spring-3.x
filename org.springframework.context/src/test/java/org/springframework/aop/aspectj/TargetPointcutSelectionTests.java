/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.aspectj;

import static org.junit.Assert.assertEquals;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Tests for target selection matching (see SPR-3783).
 * Thanks to Tomasz Blachowicz for the bug report!
 *
 * @author Ramnivas Laddad
 * @author Chris Beams
 */
public final class TargetPointcutSelectionTests {

	public TestInterface testImpl1;
	public TestInterface testImpl2;
	public TestAspect testAspectForTestImpl1;
	public TestAspect testAspectForAbstractTestImpl;
	public TestInterceptor testInterceptor;
	

	@Before
	public void setUp() {
		ClassPathXmlApplicationContext ctx =
			new ClassPathXmlApplicationContext(getClass().getSimpleName() + ".xml", getClass());
		testImpl1 = (TestInterface) ctx.getBean("testImpl1");
		testImpl2 = (TestInterface) ctx.getBean("testImpl2");
		testAspectForTestImpl1 = (TestAspect) ctx.getBean("testAspectForTestImpl1");
		testAspectForAbstractTestImpl = (TestAspect) ctx.getBean("testAspectForAbstractTestImpl");
		testInterceptor = (TestInterceptor) ctx.getBean("testInterceptor");
		
		testAspectForTestImpl1.count = 0;
		testAspectForAbstractTestImpl.count = 0;
		testInterceptor.count = 0;
	}
	
	@Test
	public void testTargetSelectionForMatchedType() {
		testImpl1.interfaceMethod();
		assertEquals("Should have been advised by POJO advice for impl", 1, testAspectForTestImpl1.count);
		assertEquals("Should have been advised by POJO advice for base type", 1, testAspectForAbstractTestImpl.count);
		assertEquals("Should have been advised by advisor", 1, testInterceptor.count);
	}

	@Test
	public void testTargetNonSelectionForMismatchedType() {
		testImpl2.interfaceMethod();
		assertEquals("Shouldn't have been advised by POJO advice for impl", 0, testAspectForTestImpl1.count);
		assertEquals("Should have been advised by POJO advice for base type", 1, testAspectForAbstractTestImpl.count);
		assertEquals("Shouldn't have been advised by advisor", 0, testInterceptor.count);
	}


	public static interface TestInterface {

		public void interfaceMethod();
	}
	

	// Reproducing bug requires that the class specified in target() pointcut doesn't
	// include the advised method's implementation (instead a base class should include it)
	public static abstract class AbstractTestImpl implements TestInterface {

		public void interfaceMethod() {
		}
	}
	

	public static class TestImpl1 extends AbstractTestImpl {
	}


	public static class TestImpl2 extends AbstractTestImpl {
	}


	public static class TestAspect {

		public int count;
		
		public void increment() {
			count++;
		}
	}
	

	public static class TestInterceptor extends TestAspect implements MethodInterceptor {

		public Object invoke(MethodInvocation mi) throws Throwable {
			increment();
			return mi.proceed();
		}
	}

}
