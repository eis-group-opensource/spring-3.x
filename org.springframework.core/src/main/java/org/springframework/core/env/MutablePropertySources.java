/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.env;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Default implementation of the {@link PropertySources} interface.
 * Allows manipulation of contained property sources and provides a constructor
 * for copying an existing {@code PropertySources} instance.
 *
 * <p>Where <em>precedence</em> is mentioned in methods such as {@link #addFirst}
 * and {@link #addLast}, this is with regard to the order in which property sources
 * will be searched when resolving a given property with a {@link PropertyResolver}.
 *
 * @author Chris Beams
 * @since 3.1
 * @see PropertySourcesPropertyResolver
 */
public class MutablePropertySources implements PropertySources {

	static final String NON_EXISTENT_PROPERTY_SOURCE_MESSAGE = "PropertySource named [%s] does not exist";
	static final String ILLEGAL_RELATIVE_ADDITION_MESSAGE = "PropertySource named [%s] cannot be added relative to itself";

	private final Log logger;

	private final LinkedList<PropertySource<?>> propertySourceList = new LinkedList<PropertySource<?>>();


	/**
	 * Create a new {@link MutablePropertySources} object.
	 */
	public MutablePropertySources() {
		this.logger = LogFactory.getLog(this.getClass());
	}

	/**
	 * Create a new {@code MutablePropertySources} from the given propertySources
	 * object, preserving the original order of contained {@code PropertySource} objects.
	 */
	public MutablePropertySources(PropertySources propertySources) {
		this();
		for (PropertySource<?> propertySource : propertySources) {
			this.addLast(propertySource);
		}
	}

	/**
	 * Create a new {@link MutablePropertySources} object and inheriting the given logger,
	 * usually from an enclosing {@link Environment}.
	 */
	MutablePropertySources(Log logger) {
		this.logger = logger;
	}


	public boolean contains(String name) {
		return this.propertySourceList.contains(PropertySource.named(name));
	}

	public PropertySource<?> get(String name) {
		return this.propertySourceList.get(this.propertySourceList.indexOf(PropertySource.named(name)));
	}

	public Iterator<PropertySource<?>> iterator() {
		return this.propertySourceList.iterator();
	}

	/**
	 * Add the given property source object with highest precedence.
	 */
	public void addFirst(PropertySource<?> propertySource) {
		logger.debug(String.format("Adding [%s] PropertySource with highest search precedence",
				propertySource.getName()));
		removeIfPresent(propertySource);
		this.propertySourceList.addFirst(propertySource);
	}

	/**
	 * Add the given property source object with lowest precedence.
	 */
	public void addLast(PropertySource<?> propertySource) {
		logger.debug(String.format("Adding [%s] PropertySource with lowest search precedence",
				propertySource.getName()));
		removeIfPresent(propertySource);
		this.propertySourceList.addLast(propertySource);
	}

	/**
	 * Add the given property source object with precedence immediately higher
	 * than the named relative property source.
	 */
	public void addBefore(String relativePropertySourceName, PropertySource<?> propertySource) {
		logger.debug(String.format("Adding [%s] PropertySource with search precedence immediately higher than [%s]",
				propertySource.getName(), relativePropertySourceName));
		assertLegalRelativeAddition(relativePropertySourceName, propertySource);
		removeIfPresent(propertySource);
		int index = assertPresentAndGetIndex(relativePropertySourceName);
		addAtIndex(index, propertySource);
	}

	/**
	 * Add the given property source object with precedence immediately lower than
	 * than the named relative property source.
	 */
	public void addAfter(String relativePropertySourceName, PropertySource<?> propertySource) {
		logger.debug(String.format("Adding [%s] PropertySource with search precedence immediately lower than [%s]",
				propertySource.getName(), relativePropertySourceName));
		assertLegalRelativeAddition(relativePropertySourceName, propertySource);
		removeIfPresent(propertySource);
		int index = assertPresentAndGetIndex(relativePropertySourceName);
		addAtIndex(index + 1, propertySource);
	}

	/**
	 * Return the precedence of the given property source, {@code -1} if not found.
	 */
	public int precedenceOf(PropertySource<?> propertySource) {
		return this.propertySourceList.indexOf(propertySource);
	}

	/**
	 * Remove and return the property source with the given name, {@code null} if not found.
	 * @param name the name of the property source to find and remove
	 */
	public PropertySource<?> remove(String name) {
		logger.debug(String.format("Removing [%s] PropertySource", name));
		int index = this.propertySourceList.indexOf(PropertySource.named(name));
		if (index >= 0) {
			return this.propertySourceList.remove(index);
		}
		return null;
	}

	/**
	 * Replace the property source with the given name with the given property source object.
	 * @param name the name of the property source to find and replace
	 * @param propertySource the replacement property source
	 * @throws IllegalArgumentException if no property source with the given name is present
	 * @see #contains
	 */
	public void replace(String name, PropertySource<?> propertySource) {
		logger.debug(String.format("Replacing [%s] PropertySource with [%s]",
				name, propertySource.getName()));
		int index = assertPresentAndGetIndex(name);
		this.propertySourceList.set(index, propertySource);
	}

	/**
	 * Return the number of {@link PropertySource} objects contained.
	 */
	public int size() {
		return this.propertySourceList.size();
	}

	@Override
	public synchronized String toString() {
		String[] names = new String[this.size()];
		for (int i=0; i < size(); i++) {
			names[i] = this.propertySourceList.get(i).getName();
		}
		return String.format("[%s]", StringUtils.arrayToCommaDelimitedString(names));
	}

	/**
	 * Ensure that the given property source is not being added relative to itself.
	 */
	protected void assertLegalRelativeAddition(String relativePropertySourceName, PropertySource<?> propertySource) {
		String newPropertySourceName = propertySource.getName();
		Assert.isTrue(!relativePropertySourceName.equals(newPropertySourceName),
				String.format(ILLEGAL_RELATIVE_ADDITION_MESSAGE, newPropertySourceName));
	}

	/**
	 * Log the removal of the given propertySource if it is present.
	 */
	protected void removeIfPresent(PropertySource<?> propertySource) {
		if (this.propertySourceList.contains(propertySource)) {
			this.propertySourceList.remove(propertySource);
		}
	}

	/**
	 * Add the given property source at a particular index in the list.
	 */
	private void addAtIndex(int index, PropertySource<?> propertySource) {
		removeIfPresent(propertySource);
		this.propertySourceList.add(index, propertySource);
	}

	/**
	 * Assert that the named property source is present and return its index.
	 * @throws IllegalArgumentException if the named property source is not present
	 */
	private int assertPresentAndGetIndex(String propertySourceName) {
		int index = this.propertySourceList.indexOf(PropertySource.named(propertySourceName));
		Assert.isTrue(index >= 0, String.format(NON_EXISTENT_PROPERTY_SOURCE_MESSAGE, propertySourceName));
		return index;
	}

}
