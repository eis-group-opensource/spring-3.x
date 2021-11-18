/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.cache.ehcache;

import static org.junit.Assert.*;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.junit.Test;
import org.springframework.cache.Cache;
import org.springframework.cache.vendor.AbstractNativeCacheTests;

/**
 * Integration test for EhCache cache.
 * 
 * @author Costin Leau
 */
public class EhCacheCacheTests extends AbstractNativeCacheTests<Ehcache> {

	@Override
	protected Ehcache createNativeCache() throws Exception {
		EhCacheFactoryBean fb = new EhCacheFactoryBean();
		fb.setBeanName(CACHE_NAME);
		fb.setCacheName(CACHE_NAME);
		fb.afterPropertiesSet();
		return fb.getObject();
	}

	@Override
	protected Cache createCache(Ehcache nativeCache) {
		return new EhCacheCache(nativeCache);
	}

	@Test
	public void testExpiredElements() throws Exception {
		String key = "brancusi";
		String value = "constantin";
		Element brancusi = new Element(key, value);
		// ttl = 10s
		brancusi.setTimeToLive(3);
		nativeCache.put(brancusi);

		assertEquals(value, cache.get(key).get());
		// wait for the entry to expire
		Thread.sleep(5 * 1000);
		assertNull(cache.get(key));
	}
}