/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.aspectj;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Ramnivas Laddad
 * @author Chris Beams
 */
public class DeclareParentsDelegateRefTests {

	protected NoMethodsBean noMethodsBean;

	protected Counter counter;
	

	@Before
	public void setUp() {
		ClassPathXmlApplicationContext ctx =
			new ClassPathXmlApplicationContext(getClass().getSimpleName() + ".xml", getClass());
		noMethodsBean = (NoMethodsBean) ctx.getBean("noMethodsBean");
		counter = (Counter) ctx.getBean("counter");
		counter.reset();
	}

	@Test
	public void testIntroductionWasMade() {
		assertTrue("Introduction must have been made", noMethodsBean instanceof ICounter);
	}
	
	@Test
	public void testIntroductionDelegation() {
		((ICounter)noMethodsBean).increment();
		assertEquals("Delegate's counter should be updated", 1, counter.getCount());
	}

}


interface NoMethodsBean {
}


class NoMethodsBeanImpl implements NoMethodsBean {
}
	
