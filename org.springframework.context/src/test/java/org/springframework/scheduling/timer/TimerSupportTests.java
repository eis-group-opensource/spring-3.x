/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scheduling.timer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import junit.framework.TestCase;

import org.springframework.scheduling.TestMethodInvokingTask;

/**
 * @author Juergen Hoeller
 * @since 20.02.2004
 */
public class TimerSupportTests extends TestCase {

	public void testTimerFactoryBean() throws Exception {
		final TestTimerTask timerTask0 = new TestTimerTask();

		TestMethodInvokingTask task1 = new TestMethodInvokingTask();
		MethodInvokingTimerTaskFactoryBean mittfb = new MethodInvokingTimerTaskFactoryBean();
		mittfb.setTargetObject(task1);
		mittfb.setTargetMethod("doSomething");
		mittfb.afterPropertiesSet();
		final TimerTask timerTask1 = (TimerTask) mittfb.getObject();

		final TestRunnable timerTask2 = new TestRunnable();

		ScheduledTimerTask[] tasks = new ScheduledTimerTask[3];
		tasks[0] = new ScheduledTimerTask(timerTask0, 0, 10, false);
		tasks[1] = new ScheduledTimerTask(timerTask1, 10, 20, true);
		tasks[2] = new ScheduledTimerTask(timerTask2, 20);

		final List success = new ArrayList(3);
		final Timer timer = new Timer(true) {
			public void schedule(TimerTask task, long delay, long period) {
				if (task == timerTask0 && delay == 0 && period == 10) {
					success.add(Boolean.TRUE);
				}
			}
			public void scheduleAtFixedRate(TimerTask task, long delay, long period) {
				if (task == timerTask1 && delay == 10 && period == 20) {
					success.add(Boolean.TRUE);
				}
			}
			public void schedule(TimerTask task, long delay) {
				if (task instanceof DelegatingTimerTask && delay == 20) {
					success.add(Boolean.TRUE);
				}
			}
			public void cancel() {
				success.add(Boolean.TRUE);
			}
		};

		TimerFactoryBean timerFactoryBean = new TimerFactoryBean() {
			protected Timer createTimer(String name, boolean daemon) {
				return timer;
			}
		};
		try {
			timerFactoryBean.setScheduledTimerTasks(tasks);
			timerFactoryBean.afterPropertiesSet();
			assertTrue(timerFactoryBean.getObject() instanceof Timer);
			timerTask0.run();
			timerTask1.run();
			timerTask2.run();
		}
		finally {
			timerFactoryBean.destroy();
		}

		assertTrue("Correct Timer invocations", success.size() == 4);
		assertTrue("TimerTask0 works", timerTask0.counter == 1);
		assertTrue("TimerTask1 works", task1.counter == 1);
		assertTrue("TimerTask2 works", timerTask2.counter == 1);
	}

	public void testPlainTimerFactoryBean() {
		TimerFactoryBean tfb = new TimerFactoryBean();
		tfb.afterPropertiesSet();
		tfb.destroy();
	}


	private static class TestTimerTask extends TimerTask {

		private int counter = 0;

		public void run() {
			counter++;
		}
	}


	private static class TestRunnable implements Runnable {

		private int counter = 0;

		public void run() {
			counter++;
		}
	}

}
