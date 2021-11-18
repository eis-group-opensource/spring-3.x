/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scheduling.config;

import org.junit.Test;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * Tests ensuring that tasks scheduled using the <task:scheduled> element
 * are never marked lazy, even if the enclosing <beans> element declares
 * default-lazy-init="true". See  SPR-8498
 *
 * @author Mike Youngstrom
 * @author Chris Beams
 */
public class LazyScheduledTasksBeanDefinitionParserTests {

	@Test(timeout = 5000)
	public void checkTarget() {
		Task task =
			new GenericXmlApplicationContext(
					LazyScheduledTasksBeanDefinitionParserTests.class,
					"lazyScheduledTasksContext.xml")
				.getBean(Task.class);

		while (!task.executed) {
			try {
				Thread.sleep(10);
			} catch (Exception e) { /* Do Nothing */ }
		}
	}

	static class Task {
		volatile boolean executed = false;

		public void doWork() {
			executed = true;
		}
	}
}
