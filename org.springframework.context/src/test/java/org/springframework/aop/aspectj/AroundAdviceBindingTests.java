/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.aspectj;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertTrue;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.springframework.aop.aspectj.AroundAdviceBindingTestAspect.AroundAdviceBindingCollaborator;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.ITestBean;
import org.springframework.beans.TestBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Tests for various parameter binding scenarios with before advice.
 *
 * @author Adrian Colyer
 * @author Chris Beams
 */
public class AroundAdviceBindingTests {

	private AroundAdviceBindingCollaborator mockCollaborator;
	
	private ITestBean testBeanProxy;
	
	private TestBean testBeanTarget;
	
	protected ApplicationContext ctx;

	@Before
	public void onSetUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext(getClass().getSimpleName() + ".xml", getClass());
		
		AroundAdviceBindingTestAspect  aroundAdviceAspect = ((AroundAdviceBindingTestAspect) ctx.getBean("testAspect"));
		
		ITestBean injectedTestBean = (ITestBean) ctx.getBean("testBean");
		assertTrue(AopUtils.isAopProxy(injectedTestBean));
		
		this.testBeanProxy = injectedTestBean;
		// we need the real target too, not just the proxy...
		
		this.testBeanTarget = (TestBean) ((Advised) testBeanProxy).getTargetSource().getTarget();
		
		mockCollaborator = createNiceMock(AroundAdviceBindingCollaborator.class);
		aroundAdviceAspect.setCollaborator(mockCollaborator);
	}

	@Test
	public void testOneIntArg() {
		mockCollaborator.oneIntArg(5);
		replay(mockCollaborator);
		testBeanProxy.setAge(5);
		verify(mockCollaborator);
	}
	
	@Test
	public void testOneObjectArgBoundToTarget() {
		mockCollaborator.oneObjectArg(this.testBeanTarget);
		replay(mockCollaborator);
		testBeanProxy.getAge();
		verify(mockCollaborator);
	}
	
	@Test
	public void testOneIntAndOneObjectArgs() {
		mockCollaborator.oneIntAndOneObject(5, this.testBeanProxy);
		replay(mockCollaborator);
		testBeanProxy.setAge(5);
		verify(mockCollaborator);
	}
	
	@Test
	public void testJustJoinPoint() {
		mockCollaborator.justJoinPoint("getAge");
		replay(mockCollaborator);
		testBeanProxy.getAge();
		verify(mockCollaborator);
	}
	
}


class AroundAdviceBindingTestAspect {

	private AroundAdviceBindingCollaborator collaborator = null;

	public void setCollaborator(AroundAdviceBindingCollaborator aCollaborator) {
		this.collaborator = aCollaborator;
	}

	// "advice" methods
	public void oneIntArg(ProceedingJoinPoint pjp, int age) throws Throwable {
		this.collaborator.oneIntArg(age);
		pjp.proceed();
	}

	public int oneObjectArg(ProceedingJoinPoint pjp, Object bean) throws Throwable {
		this.collaborator.oneObjectArg(bean);
		return ((Integer) pjp.proceed()).intValue();
	}

	public void oneIntAndOneObject(ProceedingJoinPoint pjp, int x , Object o) throws Throwable {
		this.collaborator.oneIntAndOneObject(x,o);
		pjp.proceed();
	}

	public int justJoinPoint(ProceedingJoinPoint pjp) throws Throwable {
		this.collaborator.justJoinPoint(pjp.getSignature().getName());
		return ((Integer) pjp.proceed()).intValue();
	}

	/**
	 * Collaborator interface that makes it easy to test this aspect
	 * is working as expected through mocking.
	 */
	public interface AroundAdviceBindingCollaborator {

		void oneIntArg(int x);

		void oneObjectArg(Object o);

		void oneIntAndOneObject(int x, Object o);

		void justJoinPoint(String s);
	}

}
