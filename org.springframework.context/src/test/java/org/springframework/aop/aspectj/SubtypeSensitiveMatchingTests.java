/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.aspectj;

import static org.junit.Assert.*;

import java.io.Serializable;

import org.junit.Before;
import org.junit.Test;
import org.springframework.aop.framework.Advised;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Adrian Colyer
 * @author Chris Beams
 */
public final class SubtypeSensitiveMatchingTests {

	private NonSerializableFoo nonSerializableBean;

	private SerializableFoo serializableBean;

	private Bar bar;
	

	@Before
	public void setUp() {
		ClassPathXmlApplicationContext ctx =
			new ClassPathXmlApplicationContext(getClass().getSimpleName() + ".xml", getClass());
		nonSerializableBean = (NonSerializableFoo) ctx.getBean("testClassA");
		serializableBean = (SerializableFoo) ctx.getBean("testClassB");
		bar = (Bar) ctx.getBean("testClassC");
	}

	@Test
	public void testBeansAreProxiedOnStaticMatch() {
		assertTrue("bean with serializable type should be proxied",
				this.serializableBean instanceof Advised);
	}
	
	@Test
	public void testBeansThatDoNotMatchBasedSolelyOnRuntimeTypeAreNotProxied() {
		assertFalse("bean with non-serializable type should not be proxied",
				this.nonSerializableBean instanceof Advised);		
	}
	
	@Test
	public void testBeansThatDoNotMatchBasedOnOtherTestAreProxied() {
		assertTrue("bean with args check should be proxied",
				this.bar instanceof Advised);
	}

}

//strange looking interfaces are just to set up certain test conditions...
interface NonSerializableFoo { void foo(); }

interface SerializableFoo extends Serializable { void foo(); }

class SubtypeMatchingTestClassA implements NonSerializableFoo {
	
	public void foo() {}
	
}

@SuppressWarnings("serial")
class SubtypeMatchingTestClassB implements SerializableFoo {
	
	public void foo() {}

}

interface Bar { void bar(Object o); }

class SubtypeMatchingTestClassC implements Bar {

	public void bar(Object o) {}
	
}