/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.support;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.core.NestedRuntimeException;

import test.beans.ITestBean;
import test.beans.TestBean;

/**
 * @author Rod Johnson
 * @author Chris Beams
 */
public final class ClassFiltersTests {
	
	private ClassFilter exceptionFilter = new RootClassFilter(Exception.class);
	
	private ClassFilter itbFilter = new RootClassFilter(ITestBean.class);
	
	private ClassFilter hasRootCauseFilter = new RootClassFilter(NestedRuntimeException.class);

	@Test
	public void testUnion() {
		assertTrue(exceptionFilter.matches(RuntimeException.class));
		assertFalse(exceptionFilter.matches(TestBean.class));
		assertFalse(itbFilter.matches(Exception.class));
		assertTrue(itbFilter.matches(TestBean.class));
		ClassFilter union = ClassFilters.union(exceptionFilter, itbFilter);
		assertTrue(union.matches(RuntimeException.class));
		assertTrue(union.matches(TestBean.class));
	}
	
	@Test
	public void testIntersection() {
		assertTrue(exceptionFilter.matches(RuntimeException.class));
		assertTrue(hasRootCauseFilter.matches(NestedRuntimeException.class));
		ClassFilter intersection = ClassFilters.intersection(exceptionFilter, hasRootCauseFilter);
		assertFalse(intersection.matches(RuntimeException.class));
		assertFalse(intersection.matches(TestBean.class));
		assertTrue(intersection.matches(NestedRuntimeException.class));
	}

}
