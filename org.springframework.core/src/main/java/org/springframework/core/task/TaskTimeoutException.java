/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.task;

/**
 * Exception thrown when a {@link AsyncTaskExecutor} rejects to accept
 * a given task for execution because of the specified timeout.
 *
 * @author Juergen Hoeller
 * @since 2.0.3
 * @see AsyncTaskExecutor#execute(Runnable, long)
 * @see TaskRejectedException
 */
public class TaskTimeoutException extends TaskRejectedException {

	/**
	 * Create a new <code>TaskTimeoutException</code>
	 * with the specified detail message and no root cause.
	 * @param msg the detail message
	 */
	public TaskTimeoutException(String msg) {
		super(msg);
	}

	/**
	 * Create a new <code>TaskTimeoutException</code>
	 * with the specified detail message and the given root cause.
	 * @param msg the detail message
	 * @param cause the root cause (usually from using an underlying
	 * API such as the <code>java.util.concurrent</code> package)
	 * @see java.util.concurrent.RejectedExecutionException
	 */
	public TaskTimeoutException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
