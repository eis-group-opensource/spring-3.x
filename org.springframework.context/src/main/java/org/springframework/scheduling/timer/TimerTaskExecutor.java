/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scheduling.timer;

import java.util.Timer;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * {@link org.springframework.core.task.TaskExecutor} implementation that uses a
 * single {@link Timer} for executing all tasks, effectively resulting in
 * serialized asynchronous execution on a single thread.
 *
 * @author Juergen Hoeller
 * @since 2.0
 * @see java.util.Timer
 * @deprecated as of Spring 3.0, in favor of the <code>scheduling.concurrent</code>
 * package which is based on Java 5's <code>java.util.concurrent.ExecutorService</code>
 */
@Deprecated
public class TimerTaskExecutor implements SchedulingTaskExecutor, BeanNameAware, InitializingBean, DisposableBean {

	protected final Log logger = LogFactory.getLog(getClass());

	private Timer timer;

	private long delay = 0;

	private String beanName;

	private boolean timerInternal = false;


	/**
	 * Create a new TimerTaskExecutor that needs to be further configured and initialized.
	 * @see #setTimer
	 * @see #afterPropertiesSet
	 */
	public TimerTaskExecutor() {
	}

	/**
	 * Create a new TimerTaskExecutor for the given {@link Timer}.
	 * @param timer the {@link Timer} to wrap
	 */
	public TimerTaskExecutor(Timer timer) {
		Assert.notNull(timer, "Timer must not be null");
		this.timer = timer;
	}


	/**
	 * Set the {@link Timer} to use for this {@link TimerTaskExecutor}, for example
	 * a shared {@link Timer} instance defined by a {@link TimerFactoryBean}.
	 * <p>If not specified, a default internal {@link Timer} instance will be used.
	 * @param timer the {@link Timer} to use for this {@link TimerTaskExecutor}
	 * @see TimerFactoryBean
	 */
	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	/**
	 * Set the delay to use for scheduling tasks passed into the plain
	 * {@link #execute(Runnable)} method. Default is 0.
	 * <p>Note that calls to {@link #execute(Runnable, long)} will use the
	 * given timeout as delay if it is lower than the general delay.
	 * @param delay the delay in milliseconds before the task is to be executed
	 */
	public void setDelay(long delay) {
		this.delay = delay;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}


	public void afterPropertiesSet() {
		if (this.timer == null) {
			logger.info("Initializing Timer");
			this.timer = createTimer();
			this.timerInternal = true;
		}
	}

	/**
	 * Create a new {@link Timer} instance. Called by <code>afterPropertiesSet</code>
	 * if no {@link Timer} has been specified explicitly.
	 * <p>The default implementation creates a plain non-daemon {@link Timer}.
	 * If overridden, subclasses must take care to ensure that a non-null
	 * {@link Timer} is returned from the execution of this method.
	 * @see #afterPropertiesSet
	 * @see java.util.Timer#Timer(String, boolean)
	 */
	protected Timer createTimer() {
		if (StringUtils.hasText(this.beanName)) {
			return new Timer(this.beanName);
		}
		else {
			return new Timer();
		}
	}

	/**
	 * Return the underlying Timer behind this TimerTaskExecutor.
	 */
	protected final Timer getTimer() {
		Assert.notNull(this.timer, "Timer not initialized yet");
		return this.timer;
	}


	/**
	 * Schedules the given {@link Runnable} on this executor's {@link Timer} instance,
	 * wrapping it in a {@link DelegatingTimerTask}.
	 * @param task the task to be executed
	 */
	public void execute(Runnable task) {
		getTimer().schedule(new DelegatingTimerTask(task), this.delay);
	}

	public void execute(Runnable task, long startTimeout) {
		long actualDelay = (startTimeout < this.delay ? startTimeout : this.delay);
		getTimer().schedule(new DelegatingTimerTask(task), actualDelay);
	}

	public Future<?> submit(Runnable task) {
		FutureTask<Object> future = new FutureTask<Object>(task, null);
		execute(future);
		return future;
	}

	public <T> Future<T> submit(Callable<T> task) {
		FutureTask<T> future = new FutureTask<T>(task);
		execute(future);
		return future;
	}

	/**
	 * This task executor prefers short-lived work units.
	 */
	public boolean prefersShortLivedTasks() {
		return true;
	}


	/**
	 * Cancel the {@link Timer} on bean factory shutdown, stopping all scheduled tasks.
	 * @see java.util.Timer#cancel()
	 */
	public void destroy() {
		if (this.timerInternal) {
			logger.info("Cancelling Timer");
			this.timer.cancel();
		}
	}

}
