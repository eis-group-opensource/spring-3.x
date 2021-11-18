/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.cache.interceptor;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * Interface used by CacheInterceptor. Implementations know
 * how to source cache operation attributes, whether from configuration,
 * metadata attributes at source level, or anywhere else.
 *
 * @author Costin Leau
 * @since 3.1
 */
public interface CacheOperationSource {

	/**
	 * Return the collection of cache operations for this method,
	 * or {@code null} if the method contains no "cacheable" annotations.
	 * @param method the method to introspect
	 * @param targetClass the target class (may be {@code null},
	 * in which case the declaring class of the method must be used)
	 * @return all cache operations for this method, or {@code null} if
	 * none found
	 */
	Collection<CacheOperation> getCacheOperations(Method method, Class<?> targetClass);

}
