/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.cache.annotation;

import java.lang.reflect.AnnotatedElement;
import java.util.Collection;

import org.springframework.cache.interceptor.CacheOperation;

/**
 * Strategy interface for parsing known caching annotation types.
 * {@link AnnotationCacheOperationSource} delegates to such
 * parsers for supporting specific annotation types such as Spring's own
 * {@link Cacheable}, {@link CachePut} or {@link CacheEvict}.
 *
 * @author Costin Leau
 * @since 3.1
 */
public interface CacheAnnotationParser {

	/**
	 * Parses the cache definition for the given method or class,
	 * based on a known annotation type.
	 * <p>This essentially parses a known cache annotation into Spring's
	 * metadata attribute class. Returns {@code null} if the method/class
	 * is not cacheable.
	 * @param ae the annotated method or class
	 * @return CacheOperation the configured caching operation,
	 * or {@code null} if none was found
	 * @see AnnotationCacheOperationSource#determineCacheOperation
	 */
	Collection<CacheOperation> parseCacheAnnotations(AnnotatedElement ae);
}
