/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.config;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public final class PrototypeProxyTests {

	@Test
	public void testInjectionBeforeWrappingCheckDoesNotKickInForPrototypeProxy() {
		new ClassPathXmlApplicationContext(getClass().getSimpleName() + "-context.xml", getClass());
	}

}
