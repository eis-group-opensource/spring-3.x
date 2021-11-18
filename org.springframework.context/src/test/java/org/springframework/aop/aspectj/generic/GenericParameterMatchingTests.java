/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.aspectj.generic;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Tests that poitncut matching is correct with generic method parameter.
 * See SPR-3904 for more details.
 *
 * @author Ramnivas Laddad
 * @author Chris Beams
 */
public final class GenericParameterMatchingTests {

	private CounterAspect counterAspect;

	private GenericInterface<String> testBean;


	@SuppressWarnings("unchecked")
	@org.junit.Before
	public void setUp() {
		ClassPathXmlApplicationContext ctx =
			new ClassPathXmlApplicationContext(getClass().getSimpleName() + "-context.xml", getClass());
		
		counterAspect = (CounterAspect) ctx.getBean("counterAspect");
		counterAspect.reset();
		
		testBean = (GenericInterface<String>) ctx.getBean("testBean");
	}

	
	@Test
	public void testGenericInterfaceGenericArgExecution() {
		testBean.save("");
		assertEquals(1, counterAspect.genericInterfaceGenericArgExecutionCount);
	}

	@Test
	public void testGenericInterfaceGenericCollectionArgExecution() {
		testBean.saveAll(null);
		assertEquals(1, counterAspect.genericInterfaceGenericCollectionArgExecutionCount);
	}
	
	@Test
	public void testGenericInterfaceSubtypeGenericCollectionArgExecution() {
		testBean.saveAll(null);
		assertEquals(1, counterAspect.genericInterfaceSubtypeGenericCollectionArgExecutionCount);
	}


	static interface GenericInterface<T> {

		public void save(T bean);

		public void saveAll(Collection<T> beans);
	}


	static class GenericImpl<T> implements GenericInterface<T> {

		public void save(T bean) {
		}

		public void saveAll(Collection<T> beans) {
		}
	}


	@Aspect
	static class CounterAspect {

		int genericInterfaceGenericArgExecutionCount;
		int genericInterfaceGenericCollectionArgExecutionCount;
		int genericInterfaceSubtypeGenericCollectionArgExecutionCount;
		
		public void reset() {
			genericInterfaceGenericArgExecutionCount = 0;
			genericInterfaceGenericCollectionArgExecutionCount = 0;
			genericInterfaceSubtypeGenericCollectionArgExecutionCount = 0;
		}
		
		@Pointcut("execution(* org.springframework.aop.aspectj.generic.GenericParameterMatchingTests.GenericInterface.save(..))")
		public void genericInterfaceGenericArgExecution() {} 
		
		@Pointcut("execution(* org.springframework.aop.aspectj.generic.GenericParameterMatchingTests.GenericInterface.saveAll(..))")
		public void GenericInterfaceGenericCollectionArgExecution() {} 

		@Pointcut("execution(* org.springframework.aop.aspectj.generic.GenericParameterMatchingTests.GenericInterface+.saveAll(..))")
		public void genericInterfaceSubtypeGenericCollectionArgExecution() {} 

		@Before("genericInterfaceGenericArgExecution()")
		public void incrementGenericInterfaceGenericArgExecution() {
			genericInterfaceGenericArgExecutionCount++;
		}

		@Before("GenericInterfaceGenericCollectionArgExecution()")
		public void incrementGenericInterfaceGenericCollectionArgExecution() {
			genericInterfaceGenericCollectionArgExecutionCount++;
		}

		@Before("genericInterfaceSubtypeGenericCollectionArgExecution()")
		public void incrementGenericInterfaceSubtypeGenericCollectionArgExecution() {
			genericInterfaceSubtypeGenericCollectionArgExecutionCount++;
		}
	}

}
