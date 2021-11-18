/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.interceptor;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;

/**
 * Performance monitor interceptor that uses <b>JAMon</b> library
 * to perform the performance measurement on the intercepted method
 * and output the stats.
 *
 * <p>This code is inspired by Thierry Templier's blog.
 * 
 * @author Dmitriy Kopylenko
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @since 1.1.3
 * @see com.jamonapi.MonitorFactory
 * @see PerformanceMonitorInterceptor
 */
public class JamonPerformanceMonitorInterceptor extends AbstractMonitoringInterceptor {

	private boolean trackAllInvocations = false;


	/**
	 * Create a new JamonPerformanceMonitorInterceptor with a static logger.
	 */
	public JamonPerformanceMonitorInterceptor() {
	}

	/**
	 * Create a new JamonPerformanceMonitorInterceptor with a dynamic or static logger,
	 * according to the given flag.
	 * @param useDynamicLogger whether to use a dynamic logger or a static logger
	 * @see #setUseDynamicLogger
	 */
	public JamonPerformanceMonitorInterceptor(boolean useDynamicLogger) {
		setUseDynamicLogger(useDynamicLogger);
	}

	/**
	 * Create a new JamonPerformanceMonitorInterceptor with a dynamic or static logger,
	 * according to the given flag.
	 * @param useDynamicLogger whether to use a dynamic logger or a static logger
	 * @param trackAllInvocations whether to track all invocations that go through
	 * this interceptor, or just invocations with trace logging enabled
	 * @see #setUseDynamicLogger
	 */
	public JamonPerformanceMonitorInterceptor(boolean useDynamicLogger, boolean trackAllInvocations) {
		setUseDynamicLogger(useDynamicLogger);
		setTrackAllInvocations(trackAllInvocations);
	}


	/**
	 * Set whether to track all invocations that go through this interceptor,
	 * or just invocations with trace logging enabled.
	 * <p>Default is "false": Only invocations with trace logging enabled will
	 * be monitored. Specify "true" to let JAMon track all invocations,
	 * gathering statistics even when trace logging is disabled.
	 */
	public void setTrackAllInvocations(boolean trackAllInvocations) {
		this.trackAllInvocations = trackAllInvocations;
	}


	/**
	 * Always applies the interceptor if the "trackAllInvocations" flag has been set;
	 * else just kicks in if the log is enabled.
	 * @see #setTrackAllInvocations
	 * @see #isLogEnabled
	 */
	@Override
	protected boolean isInterceptorEnabled(MethodInvocation invocation, Log logger) {
		return (this.trackAllInvocations || isLogEnabled(logger));
	}

	/**
	 * Wraps the invocation with a JAMon Monitor and writes the current
	 * performance statistics to the log (if enabled).
	 * @see com.jamonapi.MonitorFactory#start
	 * @see com.jamonapi.Monitor#stop
	 */
	@Override
	protected Object invokeUnderTrace(MethodInvocation invocation, Log logger) throws Throwable {
		String name = createInvocationTraceName(invocation);
		Monitor monitor = MonitorFactory.start(name);
		try {
			return invocation.proceed();
		}
		finally {
			monitor.stop();
			if (!this.trackAllInvocations || isLogEnabled(logger)) {
				logger.trace("JAMon performance statistics for method [" + name + "]:\n" + monitor);
			}
		}
	}

}
