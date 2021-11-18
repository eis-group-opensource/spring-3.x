/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.cache;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.springframework.cache.support.NoOpCacheManager;

public class NoOpCacheManagerTests {

	private CacheManager manager;

	@Before
	public void setup() {
		manager = new NoOpCacheManager();
	}

	@Test
	public void testGetCache() throws Exception {
		Cache cache = manager.getCache("bucket");
		assertNotNull(cache);
		assertSame(cache, manager.getCache("bucket"));
	}

	@Test
	public void testNoOpCache() throws Exception {
		String name = UUID.randomUUID().toString();
		Cache cache = manager.getCache(name);
		assertEquals(name, cache.getName());
		Object key = new Object();
		cache.put(key, new Object());
		assertNull(cache.get(key));
		assertNull(cache.getNativeCache());
	}

	@Test
	public void testCacheName() throws Exception {
		String name = "bucket";
		assertFalse(manager.getCacheNames().contains(name));
		manager.getCache(name);
		assertTrue(manager.getCacheNames().contains(name));
	}
}
