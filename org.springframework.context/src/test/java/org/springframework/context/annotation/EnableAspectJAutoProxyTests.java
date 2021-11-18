/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;

import example.scannable.FooService;
import example.scannable.ServiceInvocationCounter;

/**
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public class EnableAspectJAutoProxyTests {

	@Configuration
	@ComponentScan("example.scannable")
	@EnableAspectJAutoProxy
	static class Config_WithJDKProxy {
	}

	@Configuration
	@ComponentScan("example.scannable")
	@EnableAspectJAutoProxy(proxyTargetClass=true)
	static class Config_WithCGLIBProxy {
	}

	@Test
	public void withJDKProxy() throws Exception {
		ApplicationContext ctx =
				new AnnotationConfigApplicationContext(Config_WithJDKProxy.class);

		aspectIsApplied(ctx);
		assertThat(AopUtils.isJdkDynamicProxy(ctx.getBean(FooService.class)), is(true));
	}

	@Test
	public void withCGLIBProxy() throws Exception {
		ApplicationContext ctx =
				new AnnotationConfigApplicationContext(Config_WithCGLIBProxy.class);

		aspectIsApplied(ctx);
		assertThat(AopUtils.isCglibProxy(ctx.getBean(FooService.class)), is(true));
	}


	private void aspectIsApplied(ApplicationContext ctx) throws Exception {
		FooService fooService = ctx.getBean(FooService.class);
		ServiceInvocationCounter counter = ctx.getBean(ServiceInvocationCounter.class);

		assertEquals(0, counter.getCount());

		assertTrue(fooService.isInitCalled());
		assertEquals(1, counter.getCount());

		String value = fooService.foo(1);
		assertEquals("bar", value);
		assertEquals(2, counter.getCount());

		fooService.foo(1);
		assertEquals(3, counter.getCount());
	}
}