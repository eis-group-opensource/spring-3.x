/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scheduling.backportconcurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import edu.emory.mathcs.backport.java.util.concurrent.Executor;
import edu.emory.mathcs.backport.java.util.concurrent.Executors;
import edu.emory.mathcs.backport.java.util.concurrent.RejectedExecutionException;

import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.SchedulingTaskExecutor;

/**
 * Adapter that takes a JSR-166 backport
 * <code>edu.emory.mathcs.backport.java.util.concurrent.Executor</code> and
 * exposes a Spring {@link org.springframework.core.task.TaskExecutor} for it.
 *
 * <p><b>NOTE:</b> This class implements Spring's
 * {@link org.springframework.core.task.TaskExecutor} interface (and hence implicitly
 * the standard Java 5 {@link java.util.concurrent.Executor} interface) as well as
 * the JSR-166 {@link edu.emory.mathcs.backport.java.util.concurrent.Executor}
 * interface, with the former being the primary interface, the other just
 * serving as secondary convenience. For this reason, the exception handling
 * follows the TaskExecutor contract rather than the backport Executor contract, in
 * particular regarding the {@link org.springframework.core.task.TaskRejectedException}.
 *
 * <p>Note that there is a pre-built {@link ThreadPoolTaskExecutor} that allows for
 * defining a JSR-166 backport
 * {@link edu.emory.mathcs.backport.java.util.concurrent.ThreadPoolExecutor} in bean
 * style, exposing it as a Spring {@link org.springframework.core.task.TaskExecutor}
 * directly. This is a convenient alternative to a raw ThreadPoolExecutor
 * definition with a separate definition of the present adapter class.
 *
 * @author Juergen Hoeller
 * @since 2.0.3
 * @see edu.emory.mathcs.backport.java.util.concurrent.Executor
 * @see edu.emory.mathcs.backport.java.util.concurrent.ThreadPoolExecutor
 * @see edu.emory.mathcs.backport.java.util.concurrent.Executors
 * @see ThreadPoolTaskExecutor
 */
public class ConcurrentTaskExecutor implements SchedulingTaskExecutor, Executor {

	private Executor concurrentExecutor;


	/**
	 * Create a new ConcurrentTaskExecutor,
	 * using a single thread executor as default.
	 * @see edu.emory.mathcs.backport.java.util.concurrent.Executors#newSingleThreadExecutor()
	 */
	public ConcurrentTaskExecutor() {
		setConcurrentExecutor(null);
	}

	/**
	 * Create a new ConcurrentTaskExecutor,
	 * using the given JSR-166 backport concurrent executor.
	 * @param concurrentExecutor the JSR-166 backport concurrent executor to delegate to
	 */
	public ConcurrentTaskExecutor(Executor concurrentExecutor) {
		setConcurrentExecutor(concurrentExecutor);
	}


	/**
	 * Specify the JSR-166 backport concurrent executor to delegate to.
	 */
	public final void setConcurrentExecutor(Executor concurrentExecutor) {
		this.concurrentExecutor =
				(concurrentExecutor != null ? concurrentExecutor : Executors.newSingleThreadExecutor());
	}

	/**
	 * Return the JSR-166 backport concurrent executor that this adapter
	 * delegates to.
	 */
	public final Executor getConcurrentExecutor() {
		return this.concurrentExecutor;
	}


	/**
	 * Delegates to the specified JSR-166 backport concurrent executor.
	 * @see edu.emory.mathcs.backport.java.util.concurrent.Executor#execute(Runnable)
	 */
	public void execute(Runnable task) {
		try {
			this.concurrentExecutor.execute(task);
		}
		catch (RejectedExecutionException ex) {
			throw new TaskRejectedException(
					"Executor [" + this.concurrentExecutor + "] did not accept task: " + task, ex);
		}
	}

	public void execute(Runnable task, long startTimeout) {
		execute(task);
	}

	public Future<?> submit(Runnable task) {
		FutureTask<Object> future = new FutureTask<Object>(task, null);
		execute(future);
		return future;
	}

	public <T> Future<T> submit(Callable<T> task) {
		FutureTask<T> future = new FutureTask<T>(task);
		execute(future);
		return future;
	}

	/**
	 * This task executor prefers short-lived work units.
	 */
	public boolean prefersShortLivedTasks() {
		return true;
	}

}
