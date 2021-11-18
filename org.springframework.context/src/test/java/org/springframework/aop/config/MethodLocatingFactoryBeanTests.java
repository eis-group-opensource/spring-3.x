/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.config;

import java.lang.reflect.Method;

import static org.easymock.EasyMock.*;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.springframework.beans.factory.BeanFactory;

/**
 * @author Rick Evans
 * @author Chris Beams
 */
public final class MethodLocatingFactoryBeanTests {

	private static final String BEAN_NAME = "string";
	private MethodLocatingFactoryBean factory;
	private BeanFactory beanFactory;
	
	@Before
	public void setUp() {
		factory = new MethodLocatingFactoryBean();
		
		// methods must set up expectations and call replay() manually for this mock
		beanFactory = createMock(BeanFactory.class);
	}
	
	@After
	public void tearDown() {
		verify(beanFactory);
	}

	@Test
	public void testIsSingleton() {
		replay(beanFactory);
		assertTrue(factory.isSingleton());
	}

	@Test
	public void testGetObjectType() {
		replay(beanFactory);
		assertEquals(Method.class, factory.getObjectType());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testWithNullTargetBeanName() {
		replay(beanFactory);
		
		factory.setMethodName("toString()");
		factory.setBeanFactory(beanFactory);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testWithEmptyTargetBeanName() {
		replay(beanFactory);
		
		factory.setTargetBeanName("");
		factory.setMethodName("toString()");
		factory.setBeanFactory(beanFactory);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testWithNullTargetMethodName() {
		replay(beanFactory);
		
		factory.setTargetBeanName(BEAN_NAME);
		factory.setBeanFactory(beanFactory);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testWithEmptyTargetMethodName() {
		replay(beanFactory);
		
		factory.setTargetBeanName(BEAN_NAME);
		factory.setMethodName("");
		factory.setBeanFactory(beanFactory);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testWhenTargetBeanClassCannotBeResolved() {
		expect(beanFactory.getType(BEAN_NAME)).andReturn(null);
		replay(beanFactory);
		
		factory.setTargetBeanName(BEAN_NAME);
		factory.setMethodName("toString()");
		factory.setBeanFactory(beanFactory);
	}

	@Test
	public void testSunnyDayPath() throws Exception {
		expect((Class) beanFactory.getType(BEAN_NAME)).andReturn(String.class);
		replay(beanFactory);
		
		factory.setTargetBeanName(BEAN_NAME);
		factory.setMethodName("toString()");
		factory.setBeanFactory(beanFactory);
		Object result = factory.getObject();
		assertNotNull(result);
		assertTrue(result instanceof Method);
		Method method = (Method) result;
		assertEquals("Bingo", method.invoke("Bingo"));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testWhereMethodCannotBeResolved() {
		expect((Class) beanFactory.getType(BEAN_NAME)).andReturn(String.class);
		replay(beanFactory);
		
		factory.setTargetBeanName(BEAN_NAME);
		factory.setMethodName("loadOfOld()");
		factory.setBeanFactory(beanFactory);
	}

}
