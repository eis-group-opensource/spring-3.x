/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scheduling;

/**
 * Extension of the Runnable interface, adding special callbacks
 * for long-running operations.
 *
 * <p>This interface closely corresponds to the CommonJ Work interface,
 * but is kept separate to avoid a required CommonJ dependency.
 *
 * <p>Scheduling-capable TaskExecutors are encouraged to check a submitted
 * Runnable, detecting whether this interface is implemented and reacting
 * as appropriately as they are able to.
 *
 * @author Juergen Hoeller
 * @since 2.0
 * @see commonj.work.Work
 * @see org.springframework.core.task.TaskExecutor
 * @see SchedulingTaskExecutor
 * @see org.springframework.scheduling.commonj.WorkManagerTaskExecutor
 */
public interface SchedulingAwareRunnable extends Runnable {

	/**
	 * Return whether the Runnable's operation is long-lived
	 * (<code>true</code>) versus short-lived (<code>false</code>).
	 * <p>In the former case, the task will not allocate a thread from the thread
	 * pool (if any) but rather be considered as long-running background thread.
	 * <p>This should be considered a hint. Of course TaskExecutor implementations
	 * are free to ignore this flag and the SchedulingAwareRunnable interface overall.
	 */
	boolean isLongLived();

}
