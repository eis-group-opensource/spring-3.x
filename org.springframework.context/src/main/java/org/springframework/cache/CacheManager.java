/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.cache;

import java.util.Collection;

/**
 * A manager for a set of {@link Cache}s.
 *
 * @author Costin Leau
 * @since 3.1
 */
public interface CacheManager {

	/**
	 * Return the cache associated with the given name.
	 * @param name cache identifier (must not be {@code null})
	 * @return associated cache, or {@code null} if none is found
	 */
	Cache getCache(String name);

	/**
	 * Return a collection of the caches known by this cache manager.
	 * @return names of caches known by the cache manager.
	 */
	Collection<String> getCacheNames();

}
