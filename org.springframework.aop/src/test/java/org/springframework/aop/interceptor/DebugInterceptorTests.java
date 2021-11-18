/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.interceptor;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.junit.Test;

/**
 * Unit tests for the {@link DebugInterceptor} class.
 *
 * @author Rick Evans
 * @author Chris Beams
 */
public final class DebugInterceptorTests {

	@Test
	public void testSunnyDayPathLogsCorrectly() throws Throwable {
		Log log = createMock(Log.class);
		
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);

		expect(log.isTraceEnabled()).andReturn(true);
		log.trace(isA(String.class));
		expect(methodInvocation.proceed()).andReturn(null);
		log.trace(isA(String.class));

		replay(methodInvocation);
		replay(log);

		DebugInterceptor interceptor = new StubDebugInterceptor(log);
		interceptor.invoke(methodInvocation);
		checkCallCountTotal(interceptor);

		verify(methodInvocation);
		verify(log);
	}

	@Test
	public void testExceptionPathStillLogsCorrectly() throws Throwable {
		Log log = createMock(Log.class);
		
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);

		expect(log.isTraceEnabled()).andReturn(true);
		log.trace(isA(String.class));
		IllegalArgumentException exception = new IllegalArgumentException();
		expect(methodInvocation.proceed()).andThrow(exception);
		log.trace(isA(String.class), eq(exception));

		replay(methodInvocation);
		replay(log);

		DebugInterceptor interceptor = new StubDebugInterceptor(log);
		try {
			interceptor.invoke(methodInvocation);
			fail("Must have propagated the IllegalArgumentException.");
		} catch (IllegalArgumentException expected) {
		}
		checkCallCountTotal(interceptor);

		verify(methodInvocation);
		verify(log);
	}

	private void checkCallCountTotal(DebugInterceptor interceptor) {
		assertEquals("Intercepted call count not being incremented correctly", 1, interceptor.getCount());
	}


	@SuppressWarnings("serial")
	private static final class StubDebugInterceptor extends DebugInterceptor {

		private final Log log;


		public StubDebugInterceptor(Log log) {
			super(true);
			this.log = log;
		}

		protected Log getLoggerForInvocation(MethodInvocation invocation) {
			return log;
		}

	}

}
