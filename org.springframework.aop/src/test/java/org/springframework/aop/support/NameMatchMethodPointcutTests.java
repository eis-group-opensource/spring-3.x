/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.support;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.ProxyFactory;

import test.aop.NopInterceptor;
import test.aop.SerializableNopInterceptor;
import test.beans.Person;
import test.beans.SerializablePerson;
import test.util.SerializationTestUtils;

/**
 * @author Rod Johnson
 * @author Chris Beams
 */
public final class NameMatchMethodPointcutTests {
	
	protected NameMatchMethodPointcut pc;
	
	protected Person proxied;
	
	protected SerializableNopInterceptor nop;

	/**
	 * Create an empty pointcut, populating instance variables.
	 */
	@Before
	public void setUp() {
		ProxyFactory pf = new ProxyFactory(new SerializablePerson());
		nop = new SerializableNopInterceptor();
		pc = new NameMatchMethodPointcut();
		pf.addAdvisor(new DefaultPointcutAdvisor(pc, nop));
		proxied = (Person) pf.getProxy();
	}
	
	@Test
	public void testMatchingOnly() {
		// Can't do exact matching through isMatch
		assertTrue(pc.isMatch("echo", "ech*"));
		assertTrue(pc.isMatch("setName", "setN*"));
		assertTrue(pc.isMatch("setName", "set*"));
		assertFalse(pc.isMatch("getName", "set*"));
		assertFalse(pc.isMatch("setName", "set"));
		assertTrue(pc.isMatch("testing", "*ing"));
	}
		
	@Test
	public void testEmpty() throws Throwable {
		assertEquals(0, nop.getCount());
		proxied.getName();
		proxied.setName("");
		proxied.echo(null);
		assertEquals(0, nop.getCount());
	}
	
	
	@Test
	public void testMatchOneMethod() throws Throwable {
		pc.addMethodName("echo");
		pc.addMethodName("set*");
		assertEquals(0, nop.getCount());
		proxied.getName();
		proxied.getName();
		assertEquals(0, nop.getCount());
		proxied.echo(null);
		assertEquals(1, nop.getCount());
		
		proxied.setName("");
		assertEquals(2, nop.getCount());
		proxied.setAge(25);
		assertEquals(25, proxied.getAge());
		assertEquals(3, nop.getCount());
	}

	@Test
	public void testSets() throws Throwable {
		pc.setMappedNames(new String[] { "set*", "echo" });
		assertEquals(0, nop.getCount());
		proxied.getName();
		proxied.setName("");
		assertEquals(1, nop.getCount());
		proxied.echo(null);
		assertEquals(2, nop.getCount());
	}
	
	@Test
	public void testSerializable() throws Throwable {
		testSets();
		// Count is now 2
		Person p2 = (Person) SerializationTestUtils.serializeAndDeserialize(proxied);
		NopInterceptor nop2 = (NopInterceptor) ((Advised) p2).getAdvisors()[0].getAdvice();
		p2.getName();
		assertEquals(2, nop2.getCount());
		p2.echo(null);
		assertEquals(3, nop2.getCount());
	}

	@Test
	public void testEqualsAndHashCode() throws Exception {
		NameMatchMethodPointcut pc1 = new NameMatchMethodPointcut();
		NameMatchMethodPointcut pc2 = new NameMatchMethodPointcut();

		String foo = "foo";

		assertEquals(pc1, pc2);
		assertEquals(pc1.hashCode(), pc2.hashCode());

		pc1.setMappedName(foo);
		assertFalse(pc1.equals(pc2));
		assertTrue(pc1.hashCode() != pc2.hashCode());

		pc2.setMappedName(foo);
		assertEquals(pc1, pc2);
		assertEquals(pc1.hashCode(), pc2.hashCode());
	}

}
