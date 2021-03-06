/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scheduling.timer;

import static org.junit.Assert.*;

import java.util.Timer;

import org.junit.Test;

/**
 * Unit tests for the {@link TimerTaskExecutor} class.
 *
 * @author Rick Evans
 * @author Chris Beams
 */
public final class TimerTaskExecutorTests {

	@Test(expected=IllegalArgumentException.class)
	public void testExecuteChokesWithNullTimer() throws Exception {
		TimerTaskExecutor executor = new TimerTaskExecutor();
		executor.execute(new NoOpRunnable());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testExecuteChokesWithNullTask() throws Exception {
		TimerTaskExecutor executor = new TimerTaskExecutor(new Timer());
		executor.execute(null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testExecuteChokesWithNegativeDelay() throws Exception {
		TimerTaskExecutor executor = new TimerTaskExecutor(new Timer());
		executor.setDelay(-10);
		executor.execute(new NoOpRunnable());
	}

	@Test
	public void testExecuteReallyDoesScheduleTheSuppliedTask() throws Exception {
		final Object monitor = new Object();

		RunAwareRunnable task = new RunAwareRunnable(monitor);

		TimerTaskExecutor executor = new TimerTaskExecutor(new Timer());
		executor.execute(task);

		synchronized (monitor) {
			monitor.wait(5000);
		}

		assertTrue("Supplied task (a Runnable) is not being invoked.", task.isRunWasCalled());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCtorWithNullTimer() throws Exception {
		new TimerTaskExecutor(null);
	}

	@Test
	public void testCreateTimerMethodIsCalledIfNoTimerIsExplicitlySupplied() throws Exception {
		CreationAwareTimerTaskExecutor executor = new CreationAwareTimerTaskExecutor();
		executor.afterPropertiesSet();
		assertTrue("If no Timer is set explicitly, then the protected createTimer() " +
				"method must be called to create the Timer (it obviously isn't being called).",
				executor.isCreateTimerWasCalled());
	}

	@Test
	public void testCreateTimerMethodIsNotCalledIfTimerIsExplicitlySupplied() throws Exception {
		CreationAwareTimerTaskExecutor executor = new CreationAwareTimerTaskExecutor();
		executor.setTimer(new Timer());
		executor.afterPropertiesSet();
		assertFalse("If a Timer is set explicitly, then the protected createTimer() " +
				"method must not be called to create the Timer (it obviously is being called, in error).",
				executor.isCreateTimerWasCalled());
	}

	@Test
	public void testThatTheDestroyCallbackCancelsTheTimerIfNoTimerIsExplicitlySupplied() throws Exception {

		final CancelAwareTimer timer = new CancelAwareTimer();

		TimerTaskExecutor executor = new TimerTaskExecutor() {

			protected Timer createTimer() {
				return timer;
			}
		};
		executor.afterPropertiesSet();
		executor.destroy();
		assertTrue("When the Timer used is created by the TimerTaskExecutor because " +
				"no Timer was set explicitly, then the destroy() callback must cancel() said Timer (it obviously isn't doing this).",
				timer.isCancelWasCalled());
	}

	@Test
	public void testThatTheDestroyCallbackDoesNotCancelTheTimerIfTheTimerWasSuppliedExplictly() throws Exception {
		TimerTaskExecutor executor = new TimerTaskExecutor();
		CancelAwareTimer timer = new CancelAwareTimer();
		executor.setTimer(timer);
		executor.afterPropertiesSet();
		executor.destroy();
		assertFalse("When the Timer used is not created by the TimerTaskExecutor because " +
				"it Timer was set explicitly, then the destroy() callback must NOT cancel() said Timer (it obviously is, in error).",
				timer.isCancelWasCalled());
	}


	private final static class CreationAwareTimerTaskExecutor extends TimerTaskExecutor {

		private boolean createTimerWasCalled = false;


		public boolean isCreateTimerWasCalled() {
			return this.createTimerWasCalled;
		}

		protected Timer createTimer() {
			this.createTimerWasCalled = true;
			return super.createTimer();
		}

	}

	private static class CancelAwareTimer extends Timer {

		private boolean cancelWasCalled;


		public boolean isCancelWasCalled() {
			return this.cancelWasCalled;
		}


		public void cancel() {
			this.cancelWasCalled = true;
			super.cancel();
		}
	}

	private static class RunAwareRunnable implements Runnable {
		private boolean runWasCalled;
		private final Object monitor;

		public RunAwareRunnable(Object monitor) {
			this.monitor = monitor;
		}


		public boolean isRunWasCalled() {
			return this.runWasCalled;
		}


		public void run() {
			this.runWasCalled = true;
			synchronized (monitor) {
				monitor.notifyAll();
			}
		}
	}

	private static final class NoOpRunnable implements Runnable {

		public void run() {
			// explicit no-op
		}
	}

}
