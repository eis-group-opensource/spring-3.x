/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.support;

import static org.junit.Assert.assertEquals;
import static test.util.TestResourceUtils.qualifiedResource;

import org.junit.Test;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.Resource;

import test.aop.NopInterceptor;
import test.aop.SerializableNopInterceptor;
import test.beans.ITestBean;
import test.beans.Person;
import test.beans.TestBean;
import test.util.SerializationTestUtils;

/**
 * @author Rod Johnson
 * @author Chris Beams
 */
public final class RegexpMethodPointcutAdvisorIntegrationTests {
	
	private static final Resource CONTEXT =
		qualifiedResource(RegexpMethodPointcutAdvisorIntegrationTests.class, "context.xml");

	@Test
	public void testSinglePattern() throws Throwable {
		BeanFactory bf = new XmlBeanFactory(CONTEXT); 
		ITestBean advised = (ITestBean) bf.getBean("settersAdvised");
		// Interceptor behind regexp advisor
		NopInterceptor nop = (NopInterceptor) bf.getBean("nopInterceptor");
		assertEquals(0, nop.getCount());
		
		int newAge = 12;
		// Not advised
		advised.exceptional(null);
		assertEquals(0, nop.getCount());
		advised.setAge(newAge);
		assertEquals(newAge, advised.getAge());
		// Only setter fired
		assertEquals(1, nop.getCount());
	}
	
	@Test
	public void testMultiplePatterns() throws Throwable {
		BeanFactory bf = new XmlBeanFactory(CONTEXT); 
		// This is a CGLIB proxy, so we can proxy it to the target class
		TestBean advised = (TestBean) bf.getBean("settersAndAbsquatulateAdvised");
		// Interceptor behind regexp advisor
		NopInterceptor nop = (NopInterceptor) bf.getBean("nopInterceptor");
		assertEquals(0, nop.getCount());
	
		int newAge = 12;
		// Not advised
		advised.exceptional(null);
		assertEquals(0, nop.getCount());
		
		// This is proxied
		advised.absquatulate();
		assertEquals(1, nop.getCount());
		advised.setAge(newAge);
		assertEquals(newAge, advised.getAge());
		// Only setter fired
		assertEquals(2, nop.getCount());
	}
	
	@Test
	public void testSerialization() throws Throwable {
		BeanFactory bf = new XmlBeanFactory(CONTEXT); 
		// This is a CGLIB proxy, so we can proxy it to the target class
		Person p = (Person) bf.getBean("serializableSettersAdvised");
		// Interceptor behind regexp advisor
		NopInterceptor nop = (NopInterceptor) bf.getBean("nopInterceptor");
		assertEquals(0, nop.getCount());
	
		int newAge = 12;
		// Not advised
		assertEquals(0, p.getAge());
		assertEquals(0, nop.getCount());
		
		// This is proxied
		p.setAge(newAge);
		assertEquals(1, nop.getCount());
		p.setAge(newAge);
		assertEquals(newAge, p.getAge());
		// Only setter fired
		assertEquals(2, nop.getCount());
		
		// Serialize and continue...
		p = (Person) SerializationTestUtils.serializeAndDeserialize(p);
		assertEquals(newAge, p.getAge());
		// Remembers count, but we need to get a new reference to nop...
		nop = (SerializableNopInterceptor) ((Advised) p).getAdvisors()[0].getAdvice();
		assertEquals(2, nop.getCount());
		assertEquals("serializableSettersAdvised", p.getName());
		p.setAge(newAge + 1);
		assertEquals(3, nop.getCount());
		assertEquals(newAge + 1, p.getAge());
	}

}
