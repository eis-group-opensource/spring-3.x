/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core;

import java.util.Map;

/**
 * Common interface for a concurrent Map, as exposed by
 * {@link CollectionFactory#createConcurrentMap}. Mirrors
 * {@link java.util.concurrent.ConcurrentMap}, allowing to be backed by a
 * JDK ConcurrentHashMap as well as a backport-concurrent ConcurrentHashMap.
 *
 * <p>Check out the {@link java.util.concurrent.ConcurrentMap ConcurrentMap javadoc}
 * for details on the interface's methods.
 *
 * @author Juergen Hoeller
 * @since 2.5
 * @deprecated as of Spring 3.0, since standard {@link java.util.concurrent.ConcurrentMap}
 * is available on Java 5+ anyway
 */
@Deprecated
public interface ConcurrentMap extends Map {

	Object putIfAbsent(Object key, Object value);

	boolean remove(Object key, Object value);

	boolean replace(Object key, Object oldValue, Object newValue);

	Object replace(Object key, Object value);

}
