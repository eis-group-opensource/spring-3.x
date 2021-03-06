/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.env;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.util.Assert;

/**
 * Read-only {@code Map<String, String>} implementation that is backed by system properties or environment
 * variables.
 *
 * <p>Used by {@link AbstractApplicationContext} when a {@link SecurityManager} prohibits access to {@link
 * System#getProperties()} or {@link System#getenv()}.
 *
 * @author Arjen Poutsma
 * @author Chris Beams
 * @since 3.0
 */
abstract class ReadOnlySystemAttributesMap implements Map<String, String> {

	public boolean containsKey(Object key) {
		return get(key) != null;
	}

	/**
	 * @param key the name of the system attribute to retrieve
	 * @throws IllegalArgumentException if given key is non-String
	 */
	public String get(Object key) {
		Assert.isInstanceOf(String.class, key,
			String.format("expected key [%s] to be of type String, got %s",
					key, key.getClass().getName()));

		return this.getSystemAttribute((String) key);
	}

	public boolean isEmpty() {
		return false;
	}

	/**
	 * Template method that returns the underlying system attribute.
	 *
	 * <p>Implementations typically call {@link System#getProperty(String)} or {@link System#getenv(String)} here.
	 */
	protected abstract String getSystemAttribute(String attributeName);

	// Unsupported

	public int size() {
		throw new UnsupportedOperationException();
	}

	public String put(String key, String value) {
		throw new UnsupportedOperationException();
	}

	public boolean containsValue(Object value) {
		throw new UnsupportedOperationException();
	}

	public String remove(Object key) {
		throw new UnsupportedOperationException();
	}

	public void clear() {
		throw new UnsupportedOperationException();
	}

	public Set<String> keySet() {
		throw new UnsupportedOperationException();
	}

	public void putAll(Map<? extends String, ? extends String> m) {
		throw new UnsupportedOperationException();
	}

	public Collection<String> values() {
		throw new UnsupportedOperationException();
	}

	public Set<Entry<String, String>> entrySet() {
		throw new UnsupportedOperationException();
	}

}
