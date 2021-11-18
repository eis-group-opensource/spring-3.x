/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.interceptor;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.junit.Test;

/**
 * Unit tests for the {@link SimpleTraceInterceptor} class.
 *
 * @author Rick Evans
 * @author Chris Beams
 */
public final class SimpleTraceInterceptorTests {

	@Test
	public void testSunnyDayPathLogsCorrectly() throws Throwable {
		Log log = createMock(Log.class);
		MethodInvocation mi = createMock(MethodInvocation.class);

		Method toString = String.class.getMethod("toString", new Class[]{});

		expect(mi.getMethod()).andReturn(toString);
		expect(mi.getThis()).andReturn(this);
		log.trace(isA(String.class));
		expect(mi.proceed()).andReturn(null);
		log.trace(isA(String.class));

		replay(mi, log);

		SimpleTraceInterceptor interceptor = new SimpleTraceInterceptor(true);
		interceptor.invokeUnderTrace(mi, log);

		verify(mi, log);
	}

	public void testExceptionPathStillLogsCorrectly() throws Throwable {
		Log log = createMock(Log.class);
		MethodInvocation mi = createMock(MethodInvocation.class);

		Method toString = String.class.getMethod("toString", new Class[]{});

		expect(mi.getMethod()).andReturn(toString);
		expect(mi.getThis()).andReturn(this);
		log.trace(isA(String.class));
		IllegalArgumentException exception = new IllegalArgumentException();
		expect(mi.proceed()).andThrow(exception);
		log.trace(isA(String.class));

		replay(mi, log);

		final SimpleTraceInterceptor interceptor = new SimpleTraceInterceptor(true);

		try {
			interceptor.invokeUnderTrace(mi, log);
			fail("Must have propagated the IllegalArgumentException.");
		} catch (IllegalArgumentException expected) {
		}

		verify(mi, log);
	}

}
