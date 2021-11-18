/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.cache.support;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * A basic, no operation {@link CacheManager} implementation suitable
 * for disabling caching, typically used for backing cache declarations
 * without an actual backing store.
 *
 * <p>Will simply accept any items into the cache not actually storing them.
 *
 * @author Costin Leau
 * @since 3.1
 * @see CompositeCacheManager
 */
public class NoOpCacheManager implements CacheManager {

	private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();
	private Set<String> names = new LinkedHashSet<String>();

	private static class NoOpCache implements Cache {

		private final String name;

		public NoOpCache(String name) {
			this.name = name;
		}

		public void clear() {
		}

		public void evict(Object key) {
		}

		public ValueWrapper get(Object key) {
			return null;
		}

		public String getName() {
			return name;
		}

		public Object getNativeCache() {
			return null;
		}

		public void put(Object key, Object value) {
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 *  This implementation always returns a {@link Cache} implementation that will not
	 *  store items. Additionally, the request cache will be remembered by the manager for consistency.
	 */
	public Cache getCache(String name) {
		Cache cache = caches.get(name);
		if (cache == null) {
			caches.putIfAbsent(name, new NoOpCache(name));
			synchronized (names) {
				names.add(name);
			}
		}

		return caches.get(name);
	}

	/**
	 * {@inheritDoc}
	 *
	 * This implementation returns the name of the caches previously requested.
	 */
	public Collection<String> getCacheNames() {
		return Collections.unmodifiableSet(names);
	}
}
