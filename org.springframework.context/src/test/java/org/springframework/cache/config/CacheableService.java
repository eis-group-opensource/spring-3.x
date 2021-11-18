/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.cache.config;

/**
 * Basic service interface.
 * 
 * @author Costin Leau
 */
public interface CacheableService<T> {

	T cache(Object arg1);

	void invalidate(Object arg1);

	void evictEarly(Object arg1);

	void evictAll(Object arg1);

	void evictWithException(Object arg1);

	void evict(Object arg1, Object arg2);

	void invalidateEarly(Object arg1, Object arg2);

	T conditional(int field);

	T key(Object arg1, Object arg2);

	T name(Object arg1);

	T nullValue(Object arg1);

	T update(Object arg1);

	T conditionalUpdate(Object arg2);

	Number nullInvocations();

	T rootVars(Object arg1);

	T throwChecked(Object arg1) throws Exception;

	T throwUnchecked(Object arg1);

	// multi annotations
	T multiCache(Object arg1);
	
	T multiEvict(Object arg1);

	T multiCacheAndEvict(Object arg1);
	
	T multiConditionalCacheAndEvict(Object arg1);

	T multiUpdate(Object arg1);
}