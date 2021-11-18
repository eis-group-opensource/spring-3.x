/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.aspectj;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.aop.support.AopUtils;

/**
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public final class AroundAdviceCircularTests extends AroundAdviceBindingTests {

	@Test
	public void testBothBeansAreProxies() {
		Object tb = ctx.getBean("testBean");
		assertTrue(AopUtils.isAopProxy(tb));
		Object tb2 = ctx.getBean("testBean2");
		assertTrue(AopUtils.isAopProxy(tb2));
	}

}
