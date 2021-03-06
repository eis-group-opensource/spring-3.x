/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.aspectj.generic;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.TestBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import test.beans.Employee;

/**
 * Tests ensuring that after-returning advice for generic parameters bound to
 * the advice and the return type follow AspectJ semantics.
 *
 * <p>See SPR-3628 for more details.
 *
 * @author Ramnivas Laddad
 * @author Chris Beams
 */
public final class AfterReturningGenericTypeMatchingTests {

	private GenericReturnTypeVariationClass testBean;

	private CounterAspect counterAspect;


	@Before
	public void setUp() {
		ClassPathXmlApplicationContext ctx =
			new ClassPathXmlApplicationContext(getClass().getSimpleName() + "-context.xml", getClass());
		
		counterAspect = (CounterAspect) ctx.getBean("counterAspect");
		counterAspect.reset();
		
		testBean = (GenericReturnTypeVariationClass) ctx.getBean("testBean");
	}

	@Test
	public void testReturnTypeExactMatching() {
		testBean.getStrings();
		assertEquals(1, counterAspect.getStringsInvocationsCount);
		assertEquals(0, counterAspect.getIntegersInvocationsCount);

		counterAspect.reset();

		testBean.getIntegers();
		assertEquals(0, counterAspect.getStringsInvocationsCount);
		assertEquals(1, counterAspect.getIntegersInvocationsCount);
	}

	@Test
	public void testReturnTypeRawMatching() {
		testBean.getStrings();
		assertEquals(1, counterAspect.getRawsInvocationsCount);

		counterAspect.reset();

		testBean.getIntegers();
		assertEquals(1, counterAspect.getRawsInvocationsCount);
	}

	@Test
	public void testReturnTypeUpperBoundMatching() {
		testBean.getIntegers();
		assertEquals(1, counterAspect.getNumbersInvocationsCount);
	}

	@Test
	public void testReturnTypeLowerBoundMatching() {
		testBean.getTestBeans();
		assertEquals(1, counterAspect.getTestBeanInvocationsCount);

		counterAspect.reset();

		testBean.getEmployees();
		assertEquals(0, counterAspect.getTestBeanInvocationsCount);
	}

}


class GenericReturnTypeVariationClass {

	public Collection<String> getStrings() {
		return new ArrayList<String>();
	}

	public Collection<Integer> getIntegers() {
		return new ArrayList<Integer>();
	}

	public Collection<TestBean> getTestBeans() {
		return new ArrayList<TestBean>();
	}

	public Collection<Employee> getEmployees() {
		return new ArrayList<Employee>();
	}
}


@Aspect
class CounterAspect {

	int getRawsInvocationsCount;

	int getStringsInvocationsCount;

	int getIntegersInvocationsCount;

	int getNumbersInvocationsCount;

	int getTestBeanInvocationsCount;

	@Pointcut("execution(* org.springframework.aop.aspectj.generic.GenericReturnTypeVariationClass.*(..))")
	public void anyTestMethod() {
	}

	@AfterReturning(pointcut = "anyTestMethod()", returning = "ret")
	public void incrementGetRawsInvocationsCount(Collection<?> ret) {
		getRawsInvocationsCount++;
	}

	@AfterReturning(pointcut = "anyTestMethod()", returning = "ret")
	public void incrementGetStringsInvocationsCount(Collection<String> ret) {
		getStringsInvocationsCount++;
	}

	@AfterReturning(pointcut = "anyTestMethod()", returning = "ret")
	public void incrementGetIntegersInvocationsCount(Collection<Integer> ret) {
		getIntegersInvocationsCount++;
	}

	@AfterReturning(pointcut = "anyTestMethod()", returning = "ret")
	public void incrementGetNumbersInvocationsCount(Collection<? extends Number> ret) {
		getNumbersInvocationsCount++;
	}

	@AfterReturning(pointcut = "anyTestMethod()", returning = "ret")
	public void incrementTestBeanInvocationsCount(Collection<? super TestBean> ret) {
		getTestBeanInvocationsCount++;
	}

	public void reset() {
		getRawsInvocationsCount = 0;
		getStringsInvocationsCount = 0;
		getIntegersInvocationsCount = 0;
		getNumbersInvocationsCount = 0;
		getTestBeanInvocationsCount = 0;
	}
}

