/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.interceptor;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.junit.Test;

/**
 * @author Rob Harrop
 * @author Rick Evans
 * @author Chris Beams
 */
public final class PerformanceMonitorInterceptorTests {

	@Test
	public void testSuffixAndPrefixAssignment() {
		PerformanceMonitorInterceptor interceptor = new PerformanceMonitorInterceptor();

		assertNotNull(interceptor.getPrefix());
		assertNotNull(interceptor.getSuffix());

		interceptor.setPrefix(null);
		interceptor.setSuffix(null);

		assertNotNull(interceptor.getPrefix());
		assertNotNull(interceptor.getSuffix());
	}

	@Test
	public void testSunnyDayPathLogsPerformanceMetricsCorrectly() throws Throwable {
		Log log = createMock(Log.class);
		MethodInvocation mi = createMock(MethodInvocation.class);

		Method toString = String.class.getMethod("toString", new Class[0]);

		expect(mi.getMethod()).andReturn(toString);
		expect(mi.proceed()).andReturn(null);
		log.trace(isA(String.class));

		replay(mi, log);

		PerformanceMonitorInterceptor interceptor = new PerformanceMonitorInterceptor(true);
		interceptor.invokeUnderTrace(mi, log);

		verify(mi, log);
	}

	@Test
	public void testExceptionPathStillLogsPerformanceMetricsCorrectly() throws Throwable {
		Log log = createMock(Log.class);
		MethodInvocation mi = createMock(MethodInvocation.class);

		Method toString = String.class.getMethod("toString", new Class[0]);

		expect(mi.getMethod()).andReturn(toString);
		expect(mi.proceed()).andThrow(new IllegalArgumentException());
		log.trace(isA(String.class));

		replay(mi, log);

		PerformanceMonitorInterceptor interceptor = new PerformanceMonitorInterceptor(true);
		try {
			interceptor.invokeUnderTrace(mi, log);
			fail("Must have propagated the IllegalArgumentException.");
		}
		catch (IllegalArgumentException expected) {
		}

		verify(mi, log);
	}

}
