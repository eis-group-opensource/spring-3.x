/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.cache.interceptor;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.AnnotationCacheOperationSource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.expression.EvaluationContext;
import org.springframework.util.ReflectionUtils;

import edu.emory.mathcs.backport.java.util.Collections;

public class ExpressionEvalutatorTest {
	private ExpressionEvaluator eval = new ExpressionEvaluator();

	private AnnotationCacheOperationSource source = new AnnotationCacheOperationSource();

	private Collection<CacheOperation> getOps(String name) {
		Method method = ReflectionUtils.findMethod(AnnotatedClass.class, name, Object.class, Object.class);
		return source.getCacheOperations(method, AnnotatedClass.class);
	}

	@Test
	public void testMultipleCachingSource() throws Exception {
		Collection<CacheOperation> ops = getOps("multipleCaching");
		assertEquals(2, ops.size());
		Iterator<CacheOperation> it = ops.iterator();
		CacheOperation next = it.next();
		assertTrue(next instanceof CacheableOperation);
		assertTrue(next.getCacheNames().contains("test"));
		assertEquals("#a", next.getKey());
		next = it.next();
		assertTrue(next instanceof CacheableOperation);
		assertTrue(next.getCacheNames().contains("test"));
		assertEquals("#b", next.getKey());
	}

	@Test
	public void testMultipleCachingEval() throws Exception {
		AnnotatedClass target = new AnnotatedClass();
		Method method = ReflectionUtils.findMethod(AnnotatedClass.class, "multipleCaching", Object.class,
				Object.class);
		Object[] args = new Object[] { new Object(), new Object() };
		Collection<Cache> map = Collections.singleton(new ConcurrentMapCache("test"));

		EvaluationContext evalCtx = eval.createEvaluationContext(map, method, args, target, target.getClass());
		Collection<CacheOperation> ops = getOps("multipleCaching");

		Iterator<CacheOperation> it = ops.iterator();

		Object keyA = eval.key(it.next().getKey(), method, evalCtx);
		Object keyB = eval.key(it.next().getKey(), method, evalCtx);

		assertEquals(args[0], keyA);
		assertEquals(args[1], keyB);
	}

	private static class AnnotatedClass {
		@Caching(cacheable = { @Cacheable(value = "test", key = "#a"), @Cacheable(value = "test", key = "#b") })
		public void multipleCaching(Object a, Object b) {
		}
	}
}