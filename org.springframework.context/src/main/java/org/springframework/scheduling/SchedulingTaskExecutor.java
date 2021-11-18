/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scheduling;

import org.springframework.core.task.AsyncTaskExecutor;

/**
 * A {@link org.springframework.core.task.TaskExecutor} extension exposing
 * scheduling characteristics that are relevant to potential task submitters.
 *
 * <p>Scheduling clients are encouraged to submit
 * {@link Runnable Runnables} that match the exposed preferences
 * of the <code>TaskExecutor</code> implementation in use.
 *
 * @author Juergen Hoeller
 * @since 2.0
 * @see SchedulingAwareRunnable
 * @see org.springframework.core.task.TaskExecutor
 * @see org.springframework.scheduling.commonj.WorkManagerTaskExecutor
 */
public interface SchedulingTaskExecutor extends AsyncTaskExecutor {

	/**
	 * Does this <code>TaskExecutor</code> prefer short-lived tasks over
	 * long-lived tasks?
	 * <p>A <code>SchedulingTaskExecutor</code> implementation can indicate
	 * whether it prefers submitted tasks to perform as little work as they
	 * can within a single task execution. For example, submitted tasks
	 * might break a repeated loop into individual subtasks which submit a
	 * follow-up task afterwards (if feasible).
	 * <p>This should be considered a hint. Of course <code>TaskExecutor</code>
	 * clients are free to ignore this flag and hence the
	 * <code>SchedulingTaskExecutor</code> interface overall. However, thread
	 * pools will usually indicated a preference for short-lived tasks, to be
	 * able to perform more fine-grained scheduling.
	 * @return <code>true</code> if this <code>TaskExecutor</code> prefers
	 * short-lived tasks
	 */
	boolean prefersShortLivedTasks();

}
