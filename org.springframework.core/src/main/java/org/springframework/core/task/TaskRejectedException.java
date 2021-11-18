/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.task;

import java.util.concurrent.RejectedExecutionException;

/**
 * Exception thrown when a {@link TaskExecutor} rejects to accept
 * a given task for execution.
 *
 * @author Juergen Hoeller
 * @since 2.0.1
 * @see TaskExecutor#execute(Runnable)
 * @see TaskTimeoutException
 */
public class TaskRejectedException extends RejectedExecutionException {

	/**
	 * Create a new <code>TaskRejectedException</code>
	 * with the specified detail message and no root cause.
	 * @param msg the detail message
	 */
	public TaskRejectedException(String msg) {
		super(msg);
	}

	/**
	 * Create a new <code>TaskRejectedException</code>
	 * with the specified detail message and the given root cause.
	 * @param msg the detail message
	 * @param cause the root cause (usually from using an underlying
	 * API such as the <code>java.util.concurrent</code> package)
	 * @see java.util.concurrent.RejectedExecutionException
	 */
	public TaskRejectedException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
