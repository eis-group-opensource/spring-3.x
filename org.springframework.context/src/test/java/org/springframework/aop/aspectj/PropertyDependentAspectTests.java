/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.aspectj;

import static org.junit.Assert.*;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.junit.Test;
import org.springframework.aop.framework.Advised;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Check that an aspect that depends on another bean, where the referenced bean
 * itself is advised by the same aspect, works correctly.
 *
 * @author Ramnivas Laddad
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public final class PropertyDependentAspectTests {

	@Test
	public void testPropertyDependentAspectWithPropertyDeclaredBeforeAdvice() throws Exception {
		checkXmlAspect(getClass().getSimpleName() + "-before.xml");
	}

	@Test
	public void testPropertyDependentAspectWithPropertyDeclaredAfterAdvice() throws Exception {
		checkXmlAspect(getClass().getSimpleName() + "-after.xml");
	}

	@Test
	public void testPropertyDependentAtAspectJAspectWithPropertyDeclaredBeforeAdvice() throws Exception {
		checkAtAspectJAspect(getClass().getSimpleName() + "-atAspectJ-before.xml");
	}

	@Test
	public void testPropertyDependentAtAspectJAspectWithPropertyDeclaredAfterAdvice() throws Exception {
		checkAtAspectJAspect(getClass().getSimpleName() + "-atAspectJ-after.xml");
	}

	private void checkXmlAspect(String appContextFile) {
		ApplicationContext context = new ClassPathXmlApplicationContext(appContextFile, getClass());
		ICounter counter = (ICounter) context.getBean("counter");
		assertTrue("Proxy didn't get created", counter instanceof Advised);

		counter.increment();
		JoinPointMonitorAspect callCountingAspect = (JoinPointMonitorAspect)context.getBean("monitoringAspect");
		assertEquals("Advise didn't get executed", 1, callCountingAspect.beforeExecutions);
		assertEquals("Advise didn't get executed", 1, callCountingAspect.aroundExecutions);
	}

	private void checkAtAspectJAspect(String appContextFile) {
		ApplicationContext context = new ClassPathXmlApplicationContext(appContextFile, getClass());
		ICounter counter = (ICounter) context.getBean("counter");
		assertTrue("Proxy didn't get created", counter instanceof Advised);

		counter.increment();
		JoinPointMonitorAtAspectJAspect callCountingAspect = (JoinPointMonitorAtAspectJAspect)context.getBean("monitoringAspect");
		assertEquals("Advise didn't get executed", 1, callCountingAspect.beforeExecutions);
		assertEquals("Advise didn't get executed", 1, callCountingAspect.aroundExecutions);
	}

}


class JoinPointMonitorAspect {

	/**
	 * The counter property is purposefully not used in the aspect to avoid distraction
	 * from the main bug -- merely needing a dependency on an advised bean
	 * is sufficient to reproduce the bug.
	 */
	private ICounter counter;
	
	int beforeExecutions;
	int aroundExecutions;

	public void before() {
		beforeExecutions++;
	}

	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		aroundExecutions++;
		return pjp.proceed();
	}

	public ICounter getCounter() {
		return counter;
	}

	public void setCounter(ICounter counter) {
		this.counter = counter;
	}

}


@Aspect
class JoinPointMonitorAtAspectJAspect {
	/* The counter property is purposefully not used in the aspect to avoid distraction
	 * from the main bug -- merely needing a dependency on an advised bean
	 * is sufficient to reproduce the bug.
	 */
	private ICounter counter;
	
	int beforeExecutions;
	int aroundExecutions;

	@Before("execution(* increment*())")
	public void before() {
		beforeExecutions++;
	}

	@Around("execution(* increment*())")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		aroundExecutions++;
		return pjp.proceed();
	}

	public ICounter getCounter() {
		return counter;
	}

	public void setCounter(ICounter counter) {
		this.counter = counter;
	}

}