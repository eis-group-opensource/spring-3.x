/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scheduling.timer;

import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.util.Assert;

/**
 * Simple {@link java.util.TimerTask} adapter that delegates to a
 * given {@link java.lang.Runnable}.
 *
 * <p>This is often preferable to deriving from TimerTask, to be able to
 * implement an interface rather than extend an abstract base class.
 *
 * @author Juergen Hoeller
 * @since 1.2.4
 * @deprecated as of Spring 3.0, in favor of the <code>scheduling.concurrent</code>
 * package which is based on Java 5's <code>java.util.concurrent.ExecutorService</code>
 */
@Deprecated
public class DelegatingTimerTask extends TimerTask {

	private static final Log logger = LogFactory.getLog(DelegatingTimerTask.class);

	private final Runnable delegate;


	/**
	 * Create a new DelegatingTimerTask.
	 * @param delegate the Runnable implementation to delegate to
	 */
	public DelegatingTimerTask(Runnable delegate) {
		Assert.notNull(delegate, "Delegate must not be null");
		this.delegate = delegate;
	}

	/**
	 * Return the wrapped Runnable implementation.
	 */
	public final Runnable getDelegate() {
		return this.delegate;
	}


	/**
	 * Delegates execution to the underlying Runnable, catching any exception
	 * or error thrown in order to continue scheduled execution.
	 */
	@Override
	public void run() {
		try {
			this.delegate.run();
		}
		catch (Throwable ex) {
			logger.error("Unexpected exception thrown from Runnable: " + this.delegate, ex);
			// Do not throw the exception, else the main loop of the Timer might stop!
		}
	}

}
