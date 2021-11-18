/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.transaction.interceptor;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import junit.framework.TestCase;

import org.springframework.beans.FatalBeanException;

/**
 * Unit tests for the {@link RollbackRuleAttribute} class.
 *
 * @author Rod Johnson
 * @author Rick Evans
 * @author Chris Beams
 * @since 09.04.2003
 */
public class RollbackRuleTests extends TestCase {

	public void testFoundImmediatelyWithString() {
		RollbackRuleAttribute rr = new RollbackRuleAttribute(java.lang.Exception.class.getName());
		assertTrue(rr.getDepth(new Exception()) == 0);
	}
	
	public void testFoundImmediatelyWithClass() {
		RollbackRuleAttribute rr = new RollbackRuleAttribute(Exception.class);
		assertTrue(rr.getDepth(new Exception()) == 0);
	}
	
	public void testNotFound() {
		RollbackRuleAttribute rr = new RollbackRuleAttribute(java.io.IOException.class.getName());
		assertTrue(rr.getDepth(new MyRuntimeException("")) == -1);
	}
	
	public void testAncestry() {
		RollbackRuleAttribute rr = new RollbackRuleAttribute(java.lang.Exception.class.getName());
		// Exception -> Runtime -> NestedRuntime -> MyRuntimeException
		assertThat(rr.getDepth(new MyRuntimeException("")), equalTo(3));
	}

	public void testAlwaysTrueForThrowable() {
		RollbackRuleAttribute rr = new RollbackRuleAttribute(java.lang.Throwable.class.getName());
		assertTrue(rr.getDepth(new MyRuntimeException("")) > 0);
		assertTrue(rr.getDepth(new IOException()) > 0);
		assertTrue(rr.getDepth(new FatalBeanException(null,null)) > 0);
		assertTrue(rr.getDepth(new RuntimeException()) > 0);
	}

	public void testCtorArgMustBeAThrowableClassWithNonThrowableType() {
		try {
			new RollbackRuleAttribute(StringBuffer.class);
			fail("Cannot construct a RollbackRuleAttribute with a non-Throwable type");
		}
		catch (IllegalArgumentException expected) {
		}
	}

	public void testCtorArgMustBeAThrowableClassWithNullThrowableType() {
		try {
			new RollbackRuleAttribute((Class) null);
			fail("Cannot construct a RollbackRuleAttribute with a null-Throwable type");
		}
		catch (IllegalArgumentException expected) {
		}
	}

	public void testCtorArgExceptionStringNameVersionWithNull() {
		try {
			new RollbackRuleAttribute((String) null);
			fail("Cannot construct a RollbackRuleAttribute with a null-Throwable type");
		}
		catch (IllegalArgumentException expected) {
		}
	}

}
