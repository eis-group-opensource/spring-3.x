/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.interceptor;

import org.aopalliance.intercept.MethodInvocation;

/**
 * AOP Alliance <code>MethodInterceptor</code> that can be introduced in a chain
 * to display verbose information about intercepted invocations to the logger.
 *
 * <p>Logs full invocation details on method entry and method exit,
 * including invocation arguments and invocation count. This is only
 * intended for debugging purposes; use <code>SimpleTraceInterceptor</code>
 * or <code>CustomizableTraceInterceptor</code> for pure tracing purposes.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @see SimpleTraceInterceptor
 * @see CustomizableTraceInterceptor
 */
public class DebugInterceptor extends SimpleTraceInterceptor {

	private volatile long count;


	/**
	 * Create a new DebugInterceptor with a static logger.
	 */
	public DebugInterceptor() {
	}

	/**
	 * Create a new DebugInterceptor with dynamic or static logger,
	 * according to the given flag.
	 * @param useDynamicLogger whether to use a dynamic logger or a static logger
	 * @see #setUseDynamicLogger
	 */
	public DebugInterceptor(boolean useDynamicLogger) {
		setUseDynamicLogger(useDynamicLogger);
	}


	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		synchronized (this) {
			this.count++;
		}
		return super.invoke(invocation);
	}

	@Override
	protected String getInvocationDescription(MethodInvocation invocation) {
		return invocation + "; count=" + this.count;
	}


	/**
	 * Return the number of times this interceptor has been invoked.
	 */
	public long getCount() {
		return this.count;
	}

	/**
	 * Reset the invocation count to zero.
	 */
	public synchronized void resetCount() {
		this.count = 0;
	}

}
