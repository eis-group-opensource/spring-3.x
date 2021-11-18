/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.cache.config;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

/**
 * @author Costin Leau
 */
@Cacheable("default")
public class AnnotatedClassCacheableService implements CacheableService<Object> {

	private final AtomicLong counter = new AtomicLong();
	public static final AtomicLong nullInvocations = new AtomicLong();

	public Object cache(Object arg1) {
		return counter.getAndIncrement();
	}

	public Object conditional(int field) {
		return null;
	}

	@CacheEvict("default")
	public void invalidate(Object arg1) {
	}

	@CacheEvict("default")
	public void evictWithException(Object arg1) {
		throw new RuntimeException("exception thrown - evict should NOT occur");
	}

	@CacheEvict(value = "default", allEntries = true)
	public void evictAll(Object arg1) {
	}

	@CacheEvict(value = "default", beforeInvocation = true)
	public void evictEarly(Object arg1) {
		throw new RuntimeException("exception thrown - evict should still occur");
	}

	@CacheEvict(value = "default", key = "#p0")
	public void evict(Object arg1, Object arg2) {
	}

	@CacheEvict(value = "default", key = "#p0", beforeInvocation = true)
	public void invalidateEarly(Object arg1, Object arg2) {
		throw new RuntimeException("exception thrown - evict should still occur");
	}

	@Cacheable(value = "default", key = "#p0")
	public Object key(Object arg1, Object arg2) {
		return counter.getAndIncrement();
	}

	@Cacheable(value = "default", key = "#root.methodName + #root.caches[0].name")
	public Object name(Object arg1) {
		return counter.getAndIncrement();
	}

	@Cacheable(value = "default", key = "#root.methodName + #root.method.name + #root.targetClass + #root.target")
	public Object rootVars(Object arg1) {
		return counter.getAndIncrement();
	}

	@CachePut("default")
	public Object update(Object arg1) {
		return counter.getAndIncrement();
	}

	@CachePut(value = "default", condition = "#arg.equals(3)")
	public Object conditionalUpdate(Object arg) {
		return arg;
	}

	public Object nullValue(Object arg1) {
		nullInvocations.incrementAndGet();
		return null;
	}

	public Number nullInvocations() {
		return nullInvocations.get();
	}

	public Long throwChecked(Object arg1) throws Exception {
		throw new UnsupportedOperationException(arg1.toString());
	}

	public Long throwUnchecked(Object arg1) {
		throw new UnsupportedOperationException();
	}

	// multi annotations

	@Caching(cacheable = { @Cacheable("primary"), @Cacheable("secondary") })
	public Object multiCache(Object arg1) {
		return counter.getAndIncrement();
	}

	@Caching(evict = { @CacheEvict("primary"), @CacheEvict(value = "secondary", key = "#a0"),  @CacheEvict(value = "primary", key = "#p0 + 'A'") })
	public Object multiEvict(Object arg1) {
		return counter.getAndIncrement();
	}

	@Caching(cacheable = { @Cacheable(value = "primary", key = "#root.methodName") }, evict = { @CacheEvict("secondary") })
	public Object multiCacheAndEvict(Object arg1) {
		return counter.getAndIncrement();
	}

	@Caching(cacheable = { @Cacheable(value = "primary", condition = "#a0 == 3") }, evict = { @CacheEvict("secondary") })
	public Object multiConditionalCacheAndEvict(Object arg1) {
		return counter.getAndIncrement();
	}

	@Caching(put = { @CachePut("primary"), @CachePut("secondary") })
	public Object multiUpdate(Object arg1) {
		return arg1;
	}
}