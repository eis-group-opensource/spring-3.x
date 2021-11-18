/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.aspectj;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * See SPR-1682.
 * 
 * @author Adrian Colyer
 * @author Chris Beams
 */
public final class SharedPointcutWithArgsMismatchTests {

	private ToBeAdvised toBeAdvised;

	
	@Before
	public void setUp() {
		ClassPathXmlApplicationContext ctx =
			new ClassPathXmlApplicationContext(getClass().getSimpleName() + ".xml", getClass());
		toBeAdvised = (ToBeAdvised) ctx.getBean("toBeAdvised");
	}

	@Test
	public void testMismatchedArgBinding() {
		this.toBeAdvised.foo("Hello");
	}


}


class ToBeAdvised {

	public void foo(String s) {
		System.out.println(s);
	}
}


class MyAspect {

	public void doBefore(int x) {
		System.out.println(x);
	}

	public void doBefore(String x) {
		System.out.println(x);
	}
}

