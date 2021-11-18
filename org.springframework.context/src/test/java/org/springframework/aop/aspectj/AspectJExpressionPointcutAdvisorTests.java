/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.aspectj;

import static org.junit.Assert.assertEquals;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.ITestBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public final class AspectJExpressionPointcutAdvisorTests {

	private ITestBean testBean;

	private CallCountingInterceptor interceptor;

	@Before
	public void setUp() {
		ClassPathXmlApplicationContext ctx =
			new ClassPathXmlApplicationContext(getClass().getSimpleName() + ".xml", getClass());
		testBean = (ITestBean) ctx.getBean("testBean");
		interceptor = (CallCountingInterceptor) ctx.getBean("interceptor");
	}

	@Test
	public void testPointcutting() {
		assertEquals("Count should be 0", 0, interceptor.getCount());
		testBean.getSpouses();
		assertEquals("Count should be 1", 1, interceptor.getCount());
		testBean.getSpouse();
		assertEquals("Count should be 1", 1, interceptor.getCount());
	}

}


class CallCountingInterceptor implements MethodInterceptor {

	private int count;

	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		count++;
		return methodInvocation.proceed();
	}

	public int getCount() {
		return count;
	}

	public void reset() {
		this.count = 0;
	}
}