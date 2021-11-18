/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.interceptor;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;

import org.springframework.util.StopWatch;

/**
 * Simple AOP Alliance <code>MethodInterceptor</code> for performance monitoring.
 * This interceptor has no effect on the intercepted method call.
 *
 * <p>Uses a <code>StopWatch</code> for the actual performance measuring.
 *
 * @author Rod Johnson
 * @author Dmitriy Kopylenko
 * @author Rob Harrop
 * @see org.springframework.util.StopWatch
 * @see JamonPerformanceMonitorInterceptor
 */
public class PerformanceMonitorInterceptor extends AbstractMonitoringInterceptor {

	/**
	 * Create a new PerformanceMonitorInterceptor with a static logger.
	 */
	public PerformanceMonitorInterceptor() {
	}

	/**
	 * Create a new PerformanceMonitorInterceptor with a dynamic or static logger,
	 * according to the given flag.
	 * @param useDynamicLogger whether to use a dynamic logger or a static logger
	 * @see #setUseDynamicLogger
	 */
	public PerformanceMonitorInterceptor(boolean useDynamicLogger) {
		setUseDynamicLogger(useDynamicLogger);
	}


	@Override
	protected Object invokeUnderTrace(MethodInvocation invocation, Log logger) throws Throwable {
		String name = createInvocationTraceName(invocation);
		StopWatch stopWatch = new StopWatch(name);
		stopWatch.start(name);
		try {
			return invocation.proceed();
		}
		finally {
			stopWatch.stop();
			logger.trace(stopWatch.shortSummary());
		}
	}

}
