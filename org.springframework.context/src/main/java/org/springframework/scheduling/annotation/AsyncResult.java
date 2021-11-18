/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scheduling.annotation;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * A pass-through <code>Future</code> handle that can be used for method signatures
 * which are declared with a Future return type for asynchronous execution.
 *
 * @author Juergen Hoeller
 * @since 3.0
 * @see org.springframework.scheduling.annotation.Async
 */
public class AsyncResult<V> implements Future<V> {

	private final V value;


	/**
	 * Create a new AsyncResult holder.
	 * @param value the value to pass through
	 */
	public AsyncResult(V value) {
		this.value = value;
	}

	public boolean cancel(boolean mayInterruptIfRunning) {
		return false;
	}

	public boolean isCancelled() {
		return false;
	}

	public boolean isDone() {
		return true;
	}

	public V get() {
		return this.value;
	}

	public V get(long timeout, TimeUnit unit) {
		return this.value;
	}

}
