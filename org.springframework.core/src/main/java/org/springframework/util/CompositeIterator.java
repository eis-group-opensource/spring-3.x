/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Iterator that combines multiple other iterators.
 * This implementation maintains a list of iterators which are invoked in sequence until all iterators are exhausted.
 * @author Erwin Vervaet
 */
public class CompositeIterator<E> implements Iterator<E> {

	private List<Iterator<E>> iterators = new LinkedList<Iterator<E>>();

	private boolean inUse = false;

	/**
	 * Create a new composite iterator. Add iterators using the {@link #add(Iterator)} method.
	 */
	public CompositeIterator() {
	}

	/**
	 * Add given iterator to this composite.
	 */
	public void add(Iterator<E> iterator) {
		Assert.state(!inUse, "You can no longer add iterator to a composite iterator that's already in use");
		if (iterators.contains(iterator)) {
			throw new IllegalArgumentException("You cannot add the same iterator twice");
		}
		iterators.add(iterator);
	}

	public boolean hasNext() {
		inUse = true;
		for (Iterator<Iterator<E>> it = iterators.iterator(); it.hasNext();) {
			if (it.next().hasNext()) {
				return true;
			}
		}
		return false;
	}

	public E next() {
		inUse = true;
		for (Iterator<Iterator<E>> it = iterators.iterator(); it.hasNext();) {
			Iterator<E> iterator = it.next();
			if (iterator.hasNext()) {
				return iterator.next();
			}
		}
		throw new NoSuchElementException("Exhaused all iterators");
	}

	public void remove() {
		throw new UnsupportedOperationException("Remove is not supported");
	}
}