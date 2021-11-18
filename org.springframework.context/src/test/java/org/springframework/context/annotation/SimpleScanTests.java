/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import example.scannable.FooService;
import example.scannable.ServiceInvocationCounter;

/**
 * @author Mark Fisher
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public class SimpleScanTests {

	protected String[] getConfigLocations() {
		return new String[] {"simpleScanTests.xml"};
	}

	@Test
	public void testFooService() throws Exception {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(getConfigLocations(), getClass());
		
		FooService fooService = (FooService) ctx.getBean("fooServiceImpl");
		ServiceInvocationCounter serviceInvocationCounter = (ServiceInvocationCounter) ctx.getBean("serviceInvocationCounter");
		
		assertEquals(0, serviceInvocationCounter.getCount());

		assertTrue(fooService.isInitCalled());
		assertEquals(1, serviceInvocationCounter.getCount());

		String value = fooService.foo(1);
		assertEquals("bar", value);
		assertEquals(2, serviceInvocationCounter.getCount());
		
		fooService.foo(1);
		assertEquals(3, serviceInvocationCounter.getCount());
	}

}
