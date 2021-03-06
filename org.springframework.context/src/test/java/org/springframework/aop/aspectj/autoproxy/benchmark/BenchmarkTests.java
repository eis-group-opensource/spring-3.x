/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.aspectj.autoproxy.benchmark;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.junit.Test;
import org.springframework.aop.Advisor;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.ITestBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StopWatch;

/**
 * Integration tests for AspectJ auto proxying. Includes mixing with Spring AOP 
 * Advisors to demonstrate that existing autoproxying contract is honoured.
 *
 * @author Rod Johnson
 * @author Chris Beams
 */
public final class BenchmarkTests {
	
	private static final Class<?> CLASS = BenchmarkTests.class;

	private static final String ASPECTJ_CONTEXT = CLASS.getSimpleName() + "-aspectj.xml";

	private static final String SPRING_AOP_CONTEXT = CLASS.getSimpleName() + "-springAop.xml";

	@Test
	public void testRepeatedAroundAdviceInvocationsWithAspectJ() {
		testRepeatedAroundAdviceInvocations(ASPECTJ_CONTEXT, getCount(), "AspectJ");
	}
	
	@Test
	public void testRepeatedAroundAdviceInvocationsWithSpringAop() {
		testRepeatedAroundAdviceInvocations(SPRING_AOP_CONTEXT, getCount(), "Spring AOP");
	}
	
	@Test
	public void testRepeatedBeforeAdviceInvocationsWithAspectJ() {
		testBeforeAdviceWithoutJoinPoint(ASPECTJ_CONTEXT, getCount(), "AspectJ");
	}
	
	@Test
	public void testRepeatedBeforeAdviceInvocationsWithSpringAop() {
		testBeforeAdviceWithoutJoinPoint(SPRING_AOP_CONTEXT, getCount(), "Spring AOP");
	}
	
	@Test
	public void testRepeatedAfterReturningAdviceInvocationsWithAspectJ() {
		testAfterReturningAdviceWithoutJoinPoint(ASPECTJ_CONTEXT, getCount(), "AspectJ");
	}
	
	@Test
	public void testRepeatedAfterReturningAdviceInvocationsWithSpringAop() {
		testAfterReturningAdviceWithoutJoinPoint(SPRING_AOP_CONTEXT, getCount(), "Spring AOP");
	}

	@Test
	public void testRepeatedMixWithAspectJ() {
		testMix(ASPECTJ_CONTEXT, getCount(), "AspectJ");
	}
	
	@Test
	public void testRepeatedMixWithSpringAop() {
		testMix(SPRING_AOP_CONTEXT, getCount(), "Spring AOP");
	}
	
	/**
	 * Change the return number to a higher number to make this test useful.
	 */
	protected int getCount() {
		return 10;
	}

	private long testRepeatedAroundAdviceInvocations(String file, int howmany, String technology) {
		ClassPathXmlApplicationContext bf = new ClassPathXmlApplicationContext(file, CLASS);

		StopWatch sw = new StopWatch();
		sw.start(howmany + " repeated around advice invocations with " + technology);
		ITestBean adrian = (ITestBean) bf.getBean("adrian");
		
		assertTrue(AopUtils.isAopProxy(adrian));
		assertEquals(68, adrian.getAge());
		
		for (int i = 0; i < howmany; i++) {
			adrian.getAge();
		}
		
		sw.stop();
		System.out.println(sw.prettyPrint());
		return sw.getLastTaskTimeMillis();
	}
	
	private long testBeforeAdviceWithoutJoinPoint(String file, int howmany, String technology) {
		ClassPathXmlApplicationContext bf = new ClassPathXmlApplicationContext(file, CLASS);

		StopWatch sw = new StopWatch();
		sw.start(howmany + " repeated before advice invocations with " + technology);
		ITestBean adrian = (ITestBean) bf.getBean("adrian");
		
		assertTrue(AopUtils.isAopProxy(adrian));
		Advised a = (Advised) adrian;
		assertTrue(a.getAdvisors().length >= 3);
		assertEquals("adrian", adrian.getName());
		
		for (int i = 0; i < howmany; i++) {
			adrian.getName();
		}
		
		sw.stop();
		System.out.println(sw.prettyPrint());
		return sw.getLastTaskTimeMillis();
	}
	
	private long testAfterReturningAdviceWithoutJoinPoint(String file, int howmany, String technology) {
		ClassPathXmlApplicationContext bf = new ClassPathXmlApplicationContext(file, CLASS);

		StopWatch sw = new StopWatch();
		sw.start(howmany + " repeated after returning advice invocations with " + technology);
		ITestBean adrian = (ITestBean) bf.getBean("adrian");
		
		assertTrue(AopUtils.isAopProxy(adrian));
		Advised a = (Advised) adrian;
		assertTrue(a.getAdvisors().length >= 3);
		// Hits joinpoint
		adrian.setAge(25);
		
		for (int i = 0; i < howmany; i++) {
			adrian.setAge(i);
		}
		
		sw.stop();
		System.out.println(sw.prettyPrint());
		return sw.getLastTaskTimeMillis();
	}
	
	private long testMix(String file, int howmany, String technology) {
		ClassPathXmlApplicationContext bf = new ClassPathXmlApplicationContext(file, CLASS);

		StopWatch sw = new StopWatch();
		sw.start(howmany + " repeated mixed invocations with " + technology);
		ITestBean adrian = (ITestBean) bf.getBean("adrian");
		
		assertTrue(AopUtils.isAopProxy(adrian));
		Advised a = (Advised) adrian;
		assertTrue(a.getAdvisors().length >= 3);
		
		for (int i = 0; i < howmany; i++) {
			// Hit all 3 joinpoints
			adrian.getAge();
			adrian.getName();
			adrian.setAge(i);
			
			// Invoke three non-advised methods
			adrian.getDoctor();
			adrian.getLawyer();
			adrian.getSpouse();
		}
		
		sw.stop();
		System.out.println(sw.prettyPrint());
		return sw.getLastTaskTimeMillis();
	}

}


class MultiplyReturnValueInterceptor implements MethodInterceptor {
	
	private int multiple = 2;
	
	public int invocations;
	
	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}
	
	public int getMultiple() {
		return this.multiple;
	}
	
	public Object invoke(MethodInvocation mi) throws Throwable {
		++invocations;
		int result = (Integer) mi.proceed();
		return result * this.multiple;
	}

}


class TraceAfterReturningAdvice implements AfterReturningAdvice {
	
	public int afterTakesInt;
	
	public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
		++afterTakesInt;
	}
	
	public static Advisor advisor() {
		return new DefaultPointcutAdvisor(
			new StaticMethodMatcherPointcut() {
				public boolean matches(Method method, Class<?> targetClass) {
					return method.getParameterTypes().length == 1 &&
						method.getParameterTypes()[0].equals(Integer.class);
				}
			},
			new TraceAfterReturningAdvice());
	}

}


@Aspect
class TraceAspect {
	
	public int beforeStringReturn;
	
	public int afterTakesInt;
	
	@Before("execution(String *.*(..))")
	public void traceWithoutJoinPoint() {
		++beforeStringReturn;
	}
	
	@AfterReturning("execution(void *.*(int))")
	public void traceWithoutJoinPoint2() {
		++afterTakesInt;
	}

}


class TraceBeforeAdvice implements MethodBeforeAdvice {
	
	public int beforeStringReturn;
	
	public void before(Method method, Object[] args, Object target) throws Throwable {
		++beforeStringReturn;
	}
	
	public static Advisor advisor() {
		return new DefaultPointcutAdvisor(
			new StaticMethodMatcherPointcut() {
				public boolean matches(Method method, Class<?> targetClass) {
					return method.getReturnType().equals(String.class);
				}
			},
			new TraceBeforeAdvice());
	}

}
