/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.aspectj.generic;

import static org.junit.Assert.*;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Tests for AspectJ pointcut expression matching when working with bridge methods.
 *
 * <p>Depending on the caller's static type either the bridge method or the user-implemented method
 * gets called as the way into the proxy. Therefore, we need tests for calling a bean with
 * static type set to type with generic method and to type with specific non-generic implementation.
 *
 * <p>This class focuses on JDK proxy, while a subclass, GenericBridgeMethodMatchingClassProxyTests,
 * focuses on class proxying.
 *
 * See SPR-3556 for more details.
 *
 * @author Ramnivas Laddad
 * @author Chris Beams
 */
public class GenericBridgeMethodMatchingTests {

	protected DerivedInterface<String> testBean;

	protected GenericCounterAspect counterAspect;


	@SuppressWarnings("unchecked")
	@org.junit.Before
	public void setUp() {
		ClassPathXmlApplicationContext ctx =
			new ClassPathXmlApplicationContext(getClass().getSimpleName() + "-context.xml", getClass());
		
		counterAspect = (GenericCounterAspect) ctx.getBean("counterAspect");
		counterAspect.count = 0;
		
		testBean = (DerivedInterface<String>) ctx.getBean("testBean");
	}


	@Test
	public void testGenericDerivedInterfaceMethodThroughInterface() {
		testBean.genericDerivedInterfaceMethod("");
		assertEquals(1, counterAspect.count);
	}

	@Test
	public void testGenericBaseInterfaceMethodThroughInterface() {
		testBean.genericBaseInterfaceMethod("");
		assertEquals(1, counterAspect.count);
	}

}


interface BaseInterface<T> {

	void genericBaseInterfaceMethod(T t);
}


interface DerivedInterface<T> extends BaseInterface<T> {

	public void genericDerivedInterfaceMethod(T t);
}


class DerivedStringParameterizedClass implements DerivedInterface<String> {

	public void genericDerivedInterfaceMethod(String t) {
	}

	public void genericBaseInterfaceMethod(String t) {
	}
}

@Aspect
class GenericCounterAspect {

	public int count;

	@Before("execution(* *..BaseInterface+.*(..))")
	public void increment() {
		count++;
	}

}

