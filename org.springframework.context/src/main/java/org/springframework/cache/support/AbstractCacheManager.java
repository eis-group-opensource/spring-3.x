/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.cache.support;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.util.Assert;

/**
 * Abstract base class implementing the common {@link CacheManager}
 * methods. Useful for 'static' environments where the backing caches do
 * not change.
 *
 * @author Costin Leau
 * @author Juergen Hoeller
 * @since 3.1
 */
public abstract class AbstractCacheManager implements CacheManager, InitializingBean {

	private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<String, Cache>();

	private Set<String> cacheNames = new LinkedHashSet<String>();


	public void afterPropertiesSet() {
		Collection<? extends Cache> caches = loadCaches();
		Assert.notEmpty(caches, "loadCaches must not return an empty Collection");
		this.cacheMap.clear();

		// preserve the initial order of the cache names
		for (Cache cache : caches) {
			this.cacheMap.put(cache.getName(), cache);
			this.cacheNames.add(cache.getName());
		}
	}

	protected final void addCache(Cache cache) {
		this.cacheMap.put(cache.getName(), cache);
		this.cacheNames.add(cache.getName());
	}

	public Cache getCache(String name) {
		return this.cacheMap.get(name);
	}

	public Collection<String> getCacheNames() {
		return Collections.unmodifiableSet(this.cacheNames);
	}


	/**
	 * Load the caches for this cache manager. Occurs at startup.
	 * The returned collection must not be null.
	 */
	protected abstract Collection<? extends Cache> loadCaches();

}
