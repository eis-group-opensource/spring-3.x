/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.aspectj;

import static org.junit.Assert.*;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.junit.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.ITestBean;
import org.springframework.beans.TestBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Test for correct application of the bean() PCD for &#64;AspectJ-based aspects.
 *
 * @author Ramnivas Laddad
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public final class BeanNamePointcutAtAspectTests {

	private ITestBean testBean1;

	private ITestBean testBean2;

	private ITestBean testBean3;

	private CounterAspect counterAspect;


	@org.junit.Before
	public void setUp() {
		ClassPathXmlApplicationContext ctx =
			new ClassPathXmlApplicationContext(getClass().getSimpleName() + ".xml", getClass());
		counterAspect = (CounterAspect) ctx.getBean("counterAspect");
		testBean1 = (ITestBean) ctx.getBean("testBean1");
		testBean2 = (ITestBean) ctx.getBean("testBean2");
		testBean3 = (ITestBean) ctx.getBean("testBean3");
	}

	@Test
	public void testMatchingBeanName() {
		assertTrue("Expected a proxy", testBean1 instanceof Advised);
		// Call two methods to test for SPR-3953-like condition
		testBean1.setAge(20);
		testBean1.setName("");
		assertEquals(2 /*TODO: make this 3 when upgrading to AspectJ 1.6.0 and advice in CounterAspect are uncommented*/, counterAspect.count);
	}

	@Test
	public void testNonMatchingBeanName() {
		assertFalse("Didn't expect a proxy", testBean3 instanceof Advised);
		testBean3.setAge(20);
		assertEquals(0, counterAspect.count);
	}

	@Test
	public void testProgrammaticProxyCreation() {
		ITestBean testBean = new TestBean();

		AspectJProxyFactory factory = new AspectJProxyFactory();
		factory.setTarget(testBean);

		CounterAspect myCounterAspect = new CounterAspect();
		factory.addAspect(myCounterAspect);

		ITestBean proxyTestBean = factory.getProxy();

		assertTrue("Expected a proxy", proxyTestBean instanceof Advised);
		proxyTestBean.setAge(20);
		assertEquals("Programmatically created proxy shouldn't match bean()", 0, myCounterAspect.count);
	}

}


@Aspect
class CounterAspect {

	int count;

	@Before("execution(* set*(..)) && bean(testBean1)")
	public void increment1ForAnonymousPointcut() {
		count++;
	}
	
}
