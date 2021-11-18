/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scheduling.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import junit.framework.AssertionFailedError;

import org.easymock.MockControl;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.task.NoOpRunnable;

/**
 * @author Rick Evans
 * @author Juergen Hoeller
 */
public class ScheduledExecutorFactoryBeanTests {

	@Test
	public void testThrowsExceptionIfPoolSizeIsLessThanZero() throws Exception {
		try {
			ScheduledExecutorFactoryBean factory = new ScheduledExecutorFactoryBean();
			factory.setPoolSize(-1);
			factory.setScheduledExecutorTasks(new ScheduledExecutorTask[]{
				new NoOpScheduledExecutorTask()
			});
			factory.afterPropertiesSet();
			fail("Pool size less than zero");
		}
		catch (IllegalArgumentException expected) {
		}
	}

	@Test
	public void testShutdownNowIsPropagatedToTheExecutorOnDestroy() throws Exception {
		MockControl mockScheduledExecutorService = MockControl.createNiceControl(ScheduledExecutorService.class);
		final ScheduledExecutorService executor = (ScheduledExecutorService) mockScheduledExecutorService.getMock();
		executor.shutdownNow();
		mockScheduledExecutorService.setReturnValue(null);
		mockScheduledExecutorService.replay();

		ScheduledExecutorFactoryBean factory = new ScheduledExecutorFactoryBean() {
			protected ScheduledExecutorService createExecutor(int poolSize, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
				return executor;
			}
		};
		factory.setScheduledExecutorTasks(new ScheduledExecutorTask[]{
			new NoOpScheduledExecutorTask()
		});
		factory.afterPropertiesSet();
		factory.destroy();

		mockScheduledExecutorService.verify();
	}

	@Test
	public void testShutdownIsPropagatedToTheExecutorOnDestroy() throws Exception {
		MockControl mockScheduledExecutorService = MockControl.createNiceControl(ScheduledExecutorService.class);
		final ScheduledExecutorService executor = (ScheduledExecutorService) mockScheduledExecutorService.getMock();
		executor.shutdown();
		mockScheduledExecutorService.setVoidCallable();
		mockScheduledExecutorService.replay();

		ScheduledExecutorFactoryBean factory = new ScheduledExecutorFactoryBean() {
			protected ScheduledExecutorService createExecutor(int poolSize, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
				return executor;
			}
		};
		factory.setScheduledExecutorTasks(new ScheduledExecutorTask[]{
			new NoOpScheduledExecutorTask()
		});
		factory.setWaitForTasksToCompleteOnShutdown(true);
		factory.afterPropertiesSet();
		factory.destroy();

		mockScheduledExecutorService.verify();
	}

	@Test
	public void testOneTimeExecutionIsSetUpAndFiresCorrectly() throws Exception {
		MockControl mockRunnable = MockControl.createControl(Runnable.class);
		Runnable runnable = (Runnable) mockRunnable.getMock();
		runnable.run();
		mockRunnable.setVoidCallable();
		mockRunnable.replay();

		ScheduledExecutorFactoryBean factory = new ScheduledExecutorFactoryBean();
		factory.setScheduledExecutorTasks(new ScheduledExecutorTask[]{
			new ScheduledExecutorTask(runnable)
		});
		factory.afterPropertiesSet();
		pauseToLetTaskStart(1);
		factory.destroy();

		mockRunnable.verify();
	}

	@Test
	public void testFixedRepeatedExecutionIsSetUpAndFiresCorrectly() throws Exception {
		MockControl mockRunnable = MockControl.createControl(Runnable.class);
		Runnable runnable = (Runnable) mockRunnable.getMock();
		runnable.run();
		mockRunnable.setVoidCallable();
		runnable.run();
		mockRunnable.setVoidCallable();
		mockRunnable.replay();

		ScheduledExecutorTask task = new ScheduledExecutorTask(runnable);
		task.setPeriod(500);
		task.setFixedRate(true);

		ScheduledExecutorFactoryBean factory = new ScheduledExecutorFactoryBean();
		factory.setScheduledExecutorTasks(new ScheduledExecutorTask[]{task});
		factory.afterPropertiesSet();
		pauseToLetTaskStart(2);
		factory.destroy();

		mockRunnable.verify();
	}

	@Test
	public void testFixedRepeatedExecutionIsSetUpAndFiresCorrectlyAfterException() throws Exception {
		MockControl mockRunnable = MockControl.createControl(Runnable.class);
		Runnable runnable = (Runnable) mockRunnable.getMock();
		runnable.run();
		mockRunnable.setThrowable(new IllegalStateException());
		runnable.run();
		mockRunnable.setThrowable(new IllegalStateException());
		mockRunnable.replay();

		ScheduledExecutorTask task = new ScheduledExecutorTask(runnable);
		task.setPeriod(500);
		task.setFixedRate(true);

		ScheduledExecutorFactoryBean factory = new ScheduledExecutorFactoryBean();
		factory.setScheduledExecutorTasks(new ScheduledExecutorTask[]{task});
		factory.setContinueScheduledExecutionAfterException(true);
		factory.afterPropertiesSet();
		pauseToLetTaskStart(2);
		factory.destroy();

		mockRunnable.verify();
	}

	@Ignore
	@Test
	public void testWithInitialDelayRepeatedExecutionIsSetUpAndFiresCorrectly() throws Exception {
		MockControl mockRunnable = MockControl.createControl(Runnable.class);
		Runnable runnable = (Runnable) mockRunnable.getMock();
		runnable.run();
		mockRunnable.setVoidCallable();
		runnable.run();
		mockRunnable.setVoidCallable();
		mockRunnable.replay();

		ScheduledExecutorTask task = new ScheduledExecutorTask(runnable);
		task.setPeriod(500);
		task.setDelay(3000); // nice long wait...

		ScheduledExecutorFactoryBean factory = new ScheduledExecutorFactoryBean();
		factory.setScheduledExecutorTasks(new ScheduledExecutorTask[] {task});
		factory.afterPropertiesSet();
		pauseToLetTaskStart(1);
		// invoke destroy before tasks have even been scheduled...
		factory.destroy();

		try {
			mockRunnable.verify();
			fail("Mock must never have been called");
		}
		catch (AssertionFailedError expected) {
		}
	}

	@Ignore
	@Test
	public void testWithInitialDelayRepeatedExecutionIsSetUpAndFiresCorrectlyAfterException() throws Exception {
		MockControl mockRunnable = MockControl.createControl(Runnable.class);
		Runnable runnable = (Runnable) mockRunnable.getMock();
		runnable.run();
		mockRunnable.setThrowable(new IllegalStateException());
		runnable.run();
		mockRunnable.setThrowable(new IllegalStateException());
		mockRunnable.replay();

		ScheduledExecutorTask task = new ScheduledExecutorTask(runnable);
		task.setPeriod(500);
		task.setDelay(3000); // nice long wait...

		ScheduledExecutorFactoryBean factory = new ScheduledExecutorFactoryBean();
		factory.setScheduledExecutorTasks(new ScheduledExecutorTask[] {task});
		factory.setContinueScheduledExecutionAfterException(true);
		factory.afterPropertiesSet();
		pauseToLetTaskStart(1);
		// invoke destroy before tasks have even been scheduled...
		factory.destroy();

		try {
			mockRunnable.verify();
			fail("Mock must never have been called");
		}
		catch (AssertionFailedError expected) {
		}
	}

	@Test
	public void testSettingThreadFactoryToNullForcesUseOfDefaultButIsOtherwiseCool() throws Exception {
		ScheduledExecutorFactoryBean factory = new ScheduledExecutorFactoryBean() {
			protected ScheduledExecutorService createExecutor(int poolSize, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
				assertNotNull("Bah; the setThreadFactory(..) method must use a default ThreadFactory if a null arg is passed in.");
				return super.createExecutor(poolSize, threadFactory, rejectedExecutionHandler);
			}
		};
		factory.setScheduledExecutorTasks(new ScheduledExecutorTask[]{
			new NoOpScheduledExecutorTask()
		});
		factory.setThreadFactory(null); // the null must not propagate
		factory.afterPropertiesSet();
		factory.destroy();
	}

	@Test
	public void testSettingRejectedExecutionHandlerToNullForcesUseOfDefaultButIsOtherwiseCool() throws Exception {
		ScheduledExecutorFactoryBean factory = new ScheduledExecutorFactoryBean() {
			protected ScheduledExecutorService createExecutor(int poolSize, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
				assertNotNull("Bah; the setRejectedExecutionHandler(..) method must use a default RejectedExecutionHandler if a null arg is passed in.");
				return super.createExecutor(poolSize, threadFactory, rejectedExecutionHandler);
			}
		};
		factory.setScheduledExecutorTasks(new ScheduledExecutorTask[]{
			new NoOpScheduledExecutorTask()
		});
		factory.setRejectedExecutionHandler(null); // the null must not propagate
		factory.afterPropertiesSet();
		factory.destroy();
	}

	@Test
	public void testObjectTypeReportsCorrectType() throws Exception {
		ScheduledExecutorFactoryBean factory = new ScheduledExecutorFactoryBean();
		assertEquals(ScheduledExecutorService.class, factory.getObjectType());
	}


	private static void pauseToLetTaskStart(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		}
		catch (InterruptedException ignored) {
		}
	}


	private static class NoOpScheduledExecutorTask extends ScheduledExecutorTask {

		public NoOpScheduledExecutorTask() {
			super(new NoOpRunnable());
		}
	}

}
