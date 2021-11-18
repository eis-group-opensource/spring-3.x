/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.cache.interceptor;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.util.Assert;

/**
 * Base class implementing {@link CacheOperation}.
 *
 * @author Costin Leau
 */
public abstract class CacheOperation {

	private Set<String> cacheNames = Collections.emptySet();
	private String condition = "";
	private String key = "";
	private String name = "";


	public Set<String> getCacheNames() {
		return cacheNames;
	}

	public String getCondition() {
		return condition;
	}

	public String getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public void setCacheName(String cacheName) {
		Assert.hasText(cacheName);
		this.cacheNames = Collections.singleton(cacheName);
	}

	public void setCacheNames(String[] cacheNames) {
		Assert.notEmpty(cacheNames);
		this.cacheNames = new LinkedHashSet<String>(cacheNames.length);
		for (String string : cacheNames) {
			this.cacheNames.add(string);
		}
	}

	public void setCondition(String condition) {
		Assert.notNull(condition);
		this.condition = condition;
	}

	public void setKey(String key) {
		Assert.notNull(key);
		this.key = key;
	}

	public void setName(String name) {
		Assert.hasText(name);
		this.name = name;
	}

	/**
	 * This implementation compares the {@code toString()} results.
	 * @see #toString()
	 */
	@Override
	public boolean equals(Object other) {
		return (other instanceof CacheOperation && toString().equals(other.toString()));
	}

	/**
	 * This implementation returns {@code toString()}'s hash code.
	 * @see #toString()
	 */
	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	/**
	 * Return an identifying description for this cache operation.
	 * <p>Has to be overridden in subclasses for correct {@code equals}
	 * and {@code hashCode} behavior. Alternatively, {@link #equals}
	 * and {@link #hashCode} can be overridden themselves.
	 */
	@Override
	public String toString() {
		return getOperationDescription().toString();
	}

	/**
	 * Return an identifying description for this caching operation.
	 * <p>Available to subclasses, for inclusion in their {@code toString()} result.
	 */
	protected StringBuilder getOperationDescription() {
		StringBuilder result = new StringBuilder();
		result.append(getClass().getSimpleName());
		result.append("[");
		result.append(this.name);
		result.append("] caches=");
		result.append(this.cacheNames);
		result.append(" | condition='");
		result.append(this.condition);
		result.append("' | key='");
		result.append(this.key);
		result.append("'");
		return result;
	}
}
