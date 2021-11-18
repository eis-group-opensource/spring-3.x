/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.aspectj;

import static org.junit.Assert.assertTrue;

import java.io.Serializable;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Adrian Colyer
 * @author Chris Beams
 */
public final class DeclarationOrderIndependenceTests {

	private TopsyTurvyAspect aspect;

	private TopsyTurvyTarget target;
	

	@Before
	public void setUp() {
		ClassPathXmlApplicationContext ctx =
			new ClassPathXmlApplicationContext(getClass().getSimpleName() + ".xml", getClass());
		aspect = (TopsyTurvyAspect) ctx.getBean("topsyTurvyAspect");
		target = (TopsyTurvyTarget) ctx.getBean("topsyTurvyTarget");
	}

	@Test
	public void testTargetIsSerializable() {
		assertTrue("target bean is serializable",this.target instanceof Serializable);
	}
	
	@Test
	public void testTargetIsBeanNameAware() {
		assertTrue("target bean is bean name aware",this.target instanceof BeanNameAware);
	}
	
	@Test
	public void testBeforeAdviceFiringOk() {
		AspectCollaborator collab = new AspectCollaborator();
		this.aspect.setCollaborator(collab);
		this.target.doSomething();
		assertTrue("before advice fired",collab.beforeFired);
	}
	
	@Test
	public void testAroundAdviceFiringOk() {
		AspectCollaborator collab = new AspectCollaborator();
		this.aspect.setCollaborator(collab);
		this.target.getX();
		assertTrue("around advice fired",collab.aroundFired);
	}
	
	@Test
	public void testAfterReturningFiringOk() {
		AspectCollaborator collab = new AspectCollaborator();
		this.aspect.setCollaborator(collab);
		this.target.getX();
		assertTrue("after returning advice fired",collab.afterReturningFired);		
	}
	
	
	/** public visibility is required */
	public static class BeanNameAwareMixin implements BeanNameAware {
	
		private String beanName;
		
		/* (non-Javadoc)
		 * @see org.springframework.beans.factory.BeanNameAware#setBeanName(java.lang.String)
		 */
		public void setBeanName(String name) {
			this.beanName = name;
		}
	
	}
	
	/** public visibility is required */
	@SuppressWarnings("serial")
	public static class SerializableMixin implements Serializable {
	}

}


class TopsyTurvyAspect {
	
	interface Collaborator {
		void beforeAdviceFired();
		void afterReturningAdviceFired();
		void aroundAdviceFired();
	}
	
	private Collaborator collaborator;
	
	public void setCollaborator(Collaborator collaborator) {
		this.collaborator = collaborator;
	}

	public void before() {
		this.collaborator.beforeAdviceFired();
	}
	
	public void afterReturning() {
		this.collaborator.afterReturningAdviceFired();
	}
	
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		Object ret = pjp.proceed();
		this.collaborator.aroundAdviceFired();
		return ret;
	}
}


interface TopsyTurvyTarget {

	public abstract void doSomething();

	public abstract int getX();

}


class TopsyTurvyTargetImpl implements TopsyTurvyTarget {

	private int x = 5;
	
	/* (non-Javadoc)
	 * @see org.springframework.aop.aspectj.TopsyTurvyTarget#doSomething()
	 */
	public void doSomething() {
		this.x = 10;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.aop.aspectj.TopsyTurvyTarget#getX()
	 */
	public int getX() {
		return x;
	}
	
}


class AspectCollaborator implements TopsyTurvyAspect.Collaborator {

	public boolean afterReturningFired = false;
	public boolean aroundFired = false;
	public boolean beforeFired = false;
	
	/* (non-Javadoc)
	 * @see org.springframework.aop.aspectj.TopsyTurvyAspect.Collaborator#afterReturningAdviceFired()
	 */
	public void afterReturningAdviceFired() {
		this.afterReturningFired = true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.aop.aspectj.TopsyTurvyAspect.Collaborator#aroundAdviceFired()
	 */
	public void aroundAdviceFired() {
		this.aroundFired = true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.aop.aspectj.TopsyTurvyAspect.Collaborator#beforeAdviceFired()
	 */
	public void beforeAdviceFired() {
		this.beforeFired = true;
	}
	
}
