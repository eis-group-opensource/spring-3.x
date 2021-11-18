/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.env;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Composite {@link PropertySource} implementation that iterates over a set of
 * {@link PropertySource} instances. Necessary in cases where multiple property sources
 * share the same name, e.g. when multiple values are supplied to {@code @PropertySource}.
 *
 * @author Chris Beams
 * @since 3.1.1
 */
public class CompositePropertySource extends PropertySource<Object> {

	private Set<PropertySource<?>> propertySources = new LinkedHashSet<PropertySource<?>>();


	/**
	 * Create a new {@code CompositePropertySource}.
	 *
	 * @param name the name of the property source
	 */
	public CompositePropertySource(String name) {
		super(name);
	}


	@Override
	public Object getProperty(String name) {
		for (PropertySource<?> propertySource : this.propertySources) {
			Object candidate = propertySource.getProperty(name);
			if (candidate != null) {
				return candidate;
			}
		}
		return null;
	}

	public void addPropertySource(PropertySource<?> propertySource) {
		this.propertySources.add(propertySource);
	}

	@Override
	public String toString() {
		return String.format("%s [name='%s', propertySources=%s]",
				this.getClass().getSimpleName(), this.name, this.propertySources);
	}
}
