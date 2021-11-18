/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.dao.annotation;

import junit.framework.TestCase;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.dao.annotation.PersistenceExceptionTranslationAdvisorTests.RepositoryInterface;
import org.springframework.dao.annotation.PersistenceExceptionTranslationAdvisorTests.RepositoryInterfaceImpl;
import org.springframework.dao.annotation.PersistenceExceptionTranslationAdvisorTests.StereotypedRepositoryInterfaceImpl;
import org.springframework.dao.support.ChainedPersistenceExceptionTranslator;
import org.springframework.stereotype.Repository;

/**
 * Unit tests for PersistenceExceptionTranslationPostProcessor. Does not test translation; there are separate unit tests
 * for the Spring AOP Advisor. Just checks whether proxying occurs correctly, as a unit test should.
 *
 * @author Rod Johnson
 */
public class PersistenceExceptionTranslationPostProcessorTests extends TestCase {

	public void testFailsWithNoPersistenceExceptionTranslators() {
		GenericApplicationContext gac = new GenericApplicationContext();
		gac.registerBeanDefinition("translator",
				new RootBeanDefinition(PersistenceExceptionTranslationPostProcessor.class));
		gac.registerBeanDefinition("proxied", new RootBeanDefinition(StereotypedRepositoryInterfaceImpl.class));
		try {
			gac.refresh();
			fail("Should fail with no translators");
		}
		catch (BeansException ex) {
			// Ok
		}
	}

	public void testProxiesCorrectly() {
		GenericApplicationContext gac = new GenericApplicationContext();
		gac.registerBeanDefinition("translator",
				new RootBeanDefinition(PersistenceExceptionTranslationPostProcessor.class));
		gac.registerBeanDefinition("notProxied", new RootBeanDefinition(RepositoryInterfaceImpl.class));
		gac.registerBeanDefinition("proxied", new RootBeanDefinition(StereotypedRepositoryInterfaceImpl.class));
		gac.registerBeanDefinition("classProxied", new RootBeanDefinition(RepositoryWithoutInterface.class));
		gac.registerBeanDefinition("classProxiedAndAdvised",
				new RootBeanDefinition(RepositoryWithoutInterfaceAndOtherwiseAdvised.class));
		gac.registerBeanDefinition("chainedTranslator",
				new RootBeanDefinition(ChainedPersistenceExceptionTranslator.class));
		gac.registerBeanDefinition("proxyCreator",
				BeanDefinitionBuilder.rootBeanDefinition(AnnotationAwareAspectJAutoProxyCreator.class).
						addPropertyValue("order", 50).getBeanDefinition());
		gac.registerBeanDefinition("logger", new RootBeanDefinition(LogAllAspect.class));
		gac.refresh();

		RepositoryInterface shouldNotBeProxied = (RepositoryInterface) gac.getBean("notProxied");
		assertFalse(AopUtils.isAopProxy(shouldNotBeProxied));
		RepositoryInterface shouldBeProxied = (RepositoryInterface) gac.getBean("proxied");
		assertTrue(AopUtils.isAopProxy(shouldBeProxied));
		RepositoryWithoutInterface rwi = (RepositoryWithoutInterface) gac.getBean("classProxied");
		assertTrue(AopUtils.isAopProxy(rwi));
		checkWillTranslateExceptions(rwi);

		Additional rwi2 = (Additional) gac.getBean("classProxiedAndAdvised");
		assertTrue(AopUtils.isAopProxy(rwi2));
		rwi2.additionalMethod();
		checkWillTranslateExceptions(rwi2);
	}

	protected void checkWillTranslateExceptions(Object o) {
		assertTrue(o instanceof Advised);
		Advised a = (Advised) o;
		for (Advisor advisor : a.getAdvisors()) {
			if (advisor instanceof PersistenceExceptionTranslationAdvisor) {
				return;
			}
		}
		fail("No translation");
	}

	@Repository
	public static class RepositoryWithoutInterface {

		public void nameDoesntMatter() {
		}
	}

	public interface Additional {

		void additionalMethod();
	}

	public static class RepositoryWithoutInterfaceAndOtherwiseAdvised extends StereotypedRepositoryInterfaceImpl
			implements Additional {

		public void additionalMethod() {
		}
	}

	@Aspect
	public static class LogAllAspect {

		//@Before("execution(* *())")
		@Before("execution(void *.additionalMethod())")
		public void log(JoinPoint jp) {
			System.out.println("Before " + jp.getSignature().getName());
		}
	}

}
