/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation;

import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import example.scannable.FooService;
import example.scannable.ServiceInvocationCounter;
import static org.junit.Assert.*;
import org.junit.Test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Mark Fisher
 * @author Juergen Hoeller
 */
public class SimpleConfigTests {
	
	@Test
	public void testFooService() throws Exception {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(getConfigLocations(), getClass());

		FooService fooService = ctx.getBean("fooServiceImpl", FooService.class);
		ServiceInvocationCounter serviceInvocationCounter = ctx.getBean("serviceInvocationCounter", ServiceInvocationCounter.class);

		String value = fooService.foo(1);
		assertEquals("bar", value);

		Future future = fooService.asyncFoo(1);
		assertTrue(future instanceof FutureTask);
		assertEquals("bar", future.get());

		assertEquals(2, serviceInvocationCounter.getCount());

		fooService.foo(1);
		assertEquals(3, serviceInvocationCounter.getCount());
	}

	public String[] getConfigLocations() {
		return new String[] {"simpleConfigTests.xml"};
	}

}
