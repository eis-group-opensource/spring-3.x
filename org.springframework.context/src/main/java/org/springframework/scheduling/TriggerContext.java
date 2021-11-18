/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scheduling;

import java.util.Date;

/**
 * Context object encapsulating last execution times and last completion time
 * of a given task.
 *
 * @author Juergen Hoeller
 * @since 3.0
 */
public interface TriggerContext {

	/**
	 * Return the last <i>scheduled</i> execution time of the task,
	 * or <code>null</code> if not scheduled before.
	 */
	Date lastScheduledExecutionTime();

	/**
	 * Return the last <i>actual</i> execution time of the task,
	 * or <code>null</code> if not scheduled before.
	 */
	Date lastActualExecutionTime();

	/**
	 * Return the last completion time of the task,
	 * or <code>null</code> if not scheduled before.
	 */
	Date lastCompletionTime();

}
