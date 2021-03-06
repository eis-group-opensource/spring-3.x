/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scheduling.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionException;

import org.springframework.core.task.TaskRejectedException;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.scheduling.SchedulingTaskExecutor;

/**
 * Adapter that takes a JDK 1.5 <code>java.util.concurrent.Executor</code> and
 * exposes a Spring {@link org.springframework.core.task.TaskExecutor} for it.
 * Also detects an extended <code>java.util.concurrent.ExecutorService</code>, adapting
 * the {@link org.springframework.core.task.AsyncTaskExecutor} interface accordingly.
 *
 * <p>Note that there is a pre-built {@link ThreadPoolTaskExecutor} that allows for
 * defining a JDK 1.5 {@link java.util.concurrent.ThreadPoolExecutor} in bean style,
 * exposing it as a Spring {@link org.springframework.core.task.TaskExecutor} directly.
 * This is a convenient alternative to a raw ThreadPoolExecutor definition with
 * a separate definition of the present adapter class.
 *
 * @author Juergen Hoeller
 * @since 2.0
 * @see java.util.concurrent.Executor
 * @see java.util.concurrent.ExecutorService
 * @see java.util.concurrent.ThreadPoolExecutor
 * @see java.util.concurrent.Executors
 * @see ThreadPoolTaskExecutor
 */
public class ConcurrentTaskExecutor implements SchedulingTaskExecutor {

	private Executor concurrentExecutor;

	private TaskExecutorAdapter adaptedExecutor;


	/**
	 * Create a new ConcurrentTaskExecutor,
	 * using a single thread executor as default.
	 * @see java.util.concurrent.Executors#newSingleThreadExecutor()
	 */
	public ConcurrentTaskExecutor() {
		setConcurrentExecutor(null);
	}

	/**
	 * Create a new ConcurrentTaskExecutor,
	 * using the given JDK 1.5 concurrent executor.
	 * @param concurrentExecutor the JDK 1.5 concurrent executor to delegate to
	 */
	public ConcurrentTaskExecutor(Executor concurrentExecutor) {
		setConcurrentExecutor(concurrentExecutor);
	}


	/**
	 * Specify the JDK 1.5 concurrent executor to delegate to.
	 */
	public final void setConcurrentExecutor(Executor concurrentExecutor) {
		this.concurrentExecutor =
				(concurrentExecutor != null ? concurrentExecutor : Executors.newSingleThreadExecutor());
		this.adaptedExecutor = new TaskExecutorAdapter(this.concurrentExecutor);
	}

	/**
	 * Return the JDK 1.5 concurrent executor that this adapter delegates to.
	 */
	public final Executor getConcurrentExecutor() {
		return this.concurrentExecutor;
	}


	public void execute(Runnable task) {
		this.adaptedExecutor.execute(task);
	}

	public void execute(Runnable task, long startTimeout) {
		this.adaptedExecutor.execute(task, startTimeout);
	}

	public Future<?> submit(Runnable task) {
		return this.adaptedExecutor.submit(task);
	}

	public <T> Future<T> submit(Callable<T> task) {
		return this.adaptedExecutor.submit(task);
	}

	/**
	 * This task executor prefers short-lived work units.
	 */
	public boolean prefersShortLivedTasks() {
		return true;
	}

}
