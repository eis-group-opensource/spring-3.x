/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scheduling.backportconcurrent;

import junit.framework.TestCase;

import org.springframework.core.task.NoOpRunnable;

/**
 * @author Rick Evans
 * @author Juergen Hoeller
 */
public class ConcurrentTaskExecutorTests extends TestCase {

	public void testZeroArgCtorResultsInDefaultTaskExecutorBeingUsed() throws Exception {
		ConcurrentTaskExecutor executor = new ConcurrentTaskExecutor();
		// must not throw a NullPointerException
		executor.execute(new NoOpRunnable());
	}

	public void testPassingNullExecutorToCtorResultsInDefaultTaskExecutorBeingUsed() throws Exception {
		ConcurrentTaskExecutor executor = new ConcurrentTaskExecutor(null);
		// must not throw a NullPointerException
		executor.execute(new NoOpRunnable());
	}

	public void testPassingNullExecutorToSetterResultsInDefaultTaskExecutorBeingUsed() throws Exception {
		ConcurrentTaskExecutor executor = new ConcurrentTaskExecutor();
		executor.setConcurrentExecutor(null);
		// must not throw a NullPointerException
		executor.execute(new NoOpRunnable());
	}

}
