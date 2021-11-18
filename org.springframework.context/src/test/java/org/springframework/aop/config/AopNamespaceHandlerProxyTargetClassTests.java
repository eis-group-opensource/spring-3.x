/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.config;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.ITestBean;

/**
 * @author Rob Harrop
 * @author Chris Beams
 */
public final class AopNamespaceHandlerProxyTargetClassTests extends AopNamespaceHandlerTests {

	@Test
	public void testIsClassProxy() {
		ITestBean bean = getTestBean();
		assertTrue("Should be a CGLIB proxy", AopUtils.isCglibProxy(bean));
		assertTrue("Should expose proxy", ((Advised) bean).isExposeProxy());
	}

}
