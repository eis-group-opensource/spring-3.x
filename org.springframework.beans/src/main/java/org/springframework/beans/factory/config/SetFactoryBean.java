/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.config;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.TypeConverter;
import org.springframework.core.GenericCollectionTypeResolver;

/**
 * Simple factory for shared Set instances. Allows for central setup
 * of Sets via the "set" element in XML bean definitions.
 *
 * @author Juergen Hoeller
 * @since 09.12.2003
 * @see ListFactoryBean
 * @see MapFactoryBean
 */
public class SetFactoryBean extends AbstractFactoryBean<Set> {

	private Set sourceSet;

	private Class targetSetClass;


	/**
	 * Set the source Set, typically populated via XML "set" elements.
	 */
	public void setSourceSet(Set sourceSet) {
		this.sourceSet = sourceSet;
	}

	/**
	 * Set the class to use for the target Set. Can be populated with a fully
	 * qualified class name when defined in a Spring application context.
	 * <p>Default is a linked HashSet, keeping the registration order.
	 * @see java.util.LinkedHashSet
	 */
	public void setTargetSetClass(Class targetSetClass) {
		if (targetSetClass == null) {
			throw new IllegalArgumentException("'targetSetClass' must not be null");
		}
		if (!Set.class.isAssignableFrom(targetSetClass)) {
			throw new IllegalArgumentException("'targetSetClass' must implement [java.util.Set]");
		}
		this.targetSetClass = targetSetClass;
	}


	@Override
	public Class<Set> getObjectType() {
		return Set.class;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Set createInstance() {
		if (this.sourceSet == null) {
			throw new IllegalArgumentException("'sourceSet' is required");
		}
		Set result = null;
		if (this.targetSetClass != null) {
			result = (Set) BeanUtils.instantiateClass(this.targetSetClass);
		}
		else {
			result = new LinkedHashSet(this.sourceSet.size());
		}
		Class valueType = null;
		if (this.targetSetClass != null) {
			valueType = GenericCollectionTypeResolver.getCollectionType(this.targetSetClass);
		}
		if (valueType != null) {
			TypeConverter converter = getBeanTypeConverter();
			for (Object elem : this.sourceSet) {
				result.add(converter.convertIfNecessary(elem, valueType));
			}
		}
		else {
			result.addAll(this.sourceSet);
		}
		return result;
	}

}
