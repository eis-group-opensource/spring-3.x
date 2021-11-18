/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.cache.config;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.cache.interceptor.CacheInterceptor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * @author Costin Leau
 * @author Chris Beams
 */
public class CacheAdviceNamespaceTests extends AbstractAnnotationTests {

	@Override
	protected ApplicationContext getApplicationContext() {
		return new GenericXmlApplicationContext(
				"/org/springframework/cache/config/cache-advice.xml");
	}

	@Test
	public void testKeyStrategy() throws Exception {
		CacheInterceptor bean = ctx.getBean("cacheAdviceClass", CacheInterceptor.class);
		Assert.assertSame(ctx.getBean("keyGenerator"), bean.getKeyGenerator());
	}
}
