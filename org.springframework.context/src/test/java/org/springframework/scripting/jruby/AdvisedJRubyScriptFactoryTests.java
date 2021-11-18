/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scripting.jruby;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scripting.Messenger;

import test.advice.CountingBeforeAdvice;

/**
 * @author Rob Harrop
 * @author Chris Beams
 */
public final class AdvisedJRubyScriptFactoryTests {
	
	private static final Class<?> CLASS = AdvisedJRubyScriptFactoryTests.class;
	private static final String CLASSNAME = CLASS.getSimpleName();
	
	private static final String FACTORYBEAN_CONTEXT = CLASSNAME + "-factoryBean.xml";
	private static final String APC_CONTEXT = CLASSNAME + "-beanNameAutoProxyCreator.xml";

	@Test
	public void testAdviseWithProxyFactoryBean() {
		ClassPathXmlApplicationContext ctx =
			new ClassPathXmlApplicationContext(FACTORYBEAN_CONTEXT, CLASS);

		Messenger bean = (Messenger) ctx.getBean("messenger");
		assertTrue("Bean is not a proxy", AopUtils.isAopProxy(bean));
		assertTrue("Bean is not an Advised object", bean instanceof Advised);

		CountingBeforeAdvice advice = (CountingBeforeAdvice) ctx.getBean("advice");
		assertEquals(0, advice.getCalls());
		bean.getMessage();
		assertEquals(1, advice.getCalls());
	}

	@Test
	public void testAdviseWithBeanNameAutoProxyCreator() {
		ClassPathXmlApplicationContext ctx =
			new ClassPathXmlApplicationContext(APC_CONTEXT, CLASS);

		Messenger bean = (Messenger) ctx.getBean("messenger");
		assertTrue("Bean is not a proxy", AopUtils.isAopProxy(bean));
		assertTrue("Bean is not an Advised object", bean instanceof Advised);

		CountingBeforeAdvice advice = (CountingBeforeAdvice) ctx.getBean("advice");
		assertEquals(0, advice.getCalls());
		bean.getMessage();
		assertEquals(1, advice.getCalls());
	}

}
