/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.cache.vendor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.cache.Cache;

/**
 * Test for native cache implementations.
 * 
 * @author Costin Leau
 */
public abstract class AbstractNativeCacheTests<T> {

	protected T nativeCache;
	protected Cache cache;
	protected final static String CACHE_NAME = "testCache";

	@Before
	public void setUp() throws Exception {
		nativeCache = createNativeCache();
		cache = createCache(nativeCache);
		cache.clear();
	}


	protected abstract T createNativeCache() throws Exception;

	protected abstract Cache createCache(T nativeCache);


	@Test
	public void testCacheName() throws Exception {
		assertEquals(CACHE_NAME, cache.getName());
	}

	@Test
	public void testNativeCache() throws Exception {
		assertSame(nativeCache, cache.getNativeCache());
	}

	@Test
	public void testCachePut() throws Exception {
		Object key = "enescu";
		Object value = "george";

		assertNull(cache.get(key));
		cache.put(key, value);
		assertEquals(value, cache.get(key).get());
	}

	@Test
	public void testCacheRemove() throws Exception {
		Object key = "enescu";
		Object value = "george";

		assertNull(cache.get(key));
		cache.put(key, value);
	}

	@Test
	public void testCacheClear() throws Exception {
		assertNull(cache.get("enescu"));
		cache.put("enescu", "george");
		assertNull(cache.get("vlaicu"));
		cache.put("vlaicu", "aurel");
		cache.clear();
		assertNull(cache.get("vlaicu"));
		assertNull(cache.get("enescu"));
	}
}