/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.support;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.Mergeable;

/**
 * Tag collection class used to hold managed Set values, which may
 * include runtime bean references (to be resolved into bean objects).
 *
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @since 21.01.2004
 */
public class ManagedSet<E> extends LinkedHashSet<E> implements Mergeable, BeanMetadataElement {

	private Object source;

	private String elementTypeName;

	private boolean mergeEnabled;


	public ManagedSet() {
	}

	public ManagedSet(int initialCapacity) {
		super(initialCapacity);
	}


	/**
	 * Set the configuration source <code>Object</code> for this metadata element.
	 * <p>The exact type of the object will depend on the configuration mechanism used.
	 */
	public void setSource(Object source) {
		this.source = source;
	}

	public Object getSource() {
		return this.source;
	}

	/**
	 * Set the default element type name (class name) to be used for this set.
	 */
	public void setElementTypeName(String elementTypeName) {
		this.elementTypeName = elementTypeName;
	}

	/**
	 * Return the default element type name (class name) to be used for this set.
	 */
	public String getElementTypeName() {
		return this.elementTypeName;
	}

	/**
	 * Set whether merging should be enabled for this collection,
	 * in case of a 'parent' collection value being present.
	 */
	public void setMergeEnabled(boolean mergeEnabled) {
		this.mergeEnabled = mergeEnabled;
	}

	public boolean isMergeEnabled() {
		return this.mergeEnabled;
	}

	@SuppressWarnings("unchecked")
	public Set<E> merge(Object parent) {
		if (!this.mergeEnabled) {
			throw new IllegalStateException("Not allowed to merge when the 'mergeEnabled' property is set to 'false'");
		}
		if (parent == null) {
			return this;
		}
		if (!(parent instanceof Set)) {
			throw new IllegalArgumentException("Cannot merge with object of type [" + parent.getClass() + "]");
		}
		Set<E> merged = new ManagedSet<E>();
		merged.addAll((Set) parent);
		merged.addAll(this);
		return merged;
	}

}
