/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.aop.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Tests to check if the first implicit join point argument is correctly processed.
 * See SPR-3723 for more details.
 *   
 * @author Ramnivas Laddad
 * @author Chris Beams
 */
public final class ImplicitJPArgumentMatchingTests {
	
	@Test
	public void testAspect() {
		// nothing to really test; it is enough if we don't get error while creating app context
		new ClassPathXmlApplicationContext(getClass().getSimpleName() + ".xml", getClass());
	}
	
	static class CounterAspect {
		public void increment(ProceedingJoinPoint pjp, Object bean, Object argument) throws Throwable {
			pjp.proceed();
		}
	}
}

