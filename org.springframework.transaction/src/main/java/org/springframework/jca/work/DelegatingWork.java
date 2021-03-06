/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jca.work;

import javax.resource.spi.work.Work;

import org.springframework.util.Assert;

/**
 * Simple Work adapter that delegates to a given Runnable.
 *
 * @author Juergen Hoeller
 * @since 2.0.3
 * @see javax.resource.spi.work.Work
 * @see Runnable
 */
public class DelegatingWork implements Work {

	private final Runnable delegate;


	/**
	 * Create a new DelegatingWork.
	 * @param delegate the Runnable implementation to delegate to
	 */
	public DelegatingWork(Runnable delegate) {
		Assert.notNull(delegate, "Delegate must not be null");
		this.delegate = delegate;
	}

	/**
	 * Return the wrapped Runnable implementation.
	 */
	public final Runnable getDelegate() {
		return this.delegate;
	}


	/**
	 * Delegates execution to the underlying Runnable.
	 */
	public void run() {
		this.delegate.run();
	}

	/**
	 * This implementation is empty, since we expect the Runnable
	 * to terminate based on some specific shutdown signal.
	 */
	public void release() {
	}

}
