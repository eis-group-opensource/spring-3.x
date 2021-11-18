/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.cache.concurrent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.cache.Cache;
import org.springframework.cache.vendor.AbstractNativeCacheTests;

/**
 * @author Costin Leau
 */
public class ConcurrentCacheTests extends AbstractNativeCacheTests<ConcurrentMap<Object, Object>> {

	@Override
	protected Cache createCache(ConcurrentMap<Object, Object> nativeCache) {
		return new ConcurrentMapCache(CACHE_NAME, nativeCache, true);
	}

	@Override
	protected ConcurrentMap<Object, Object> createNativeCache() throws Exception {
		return new ConcurrentHashMap<Object, Object>();
	}
}
