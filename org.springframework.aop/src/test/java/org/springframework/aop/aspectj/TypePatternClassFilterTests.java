/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.aspectj;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import test.beans.CountingTestBean;
import test.beans.IOther;
import test.beans.ITestBean;
import test.beans.TestBean;
import test.beans.subpkg.DeepBean;

/**
 * Unit tests for the {@link TypePatternClassFilter} class.
 *
 * @author Rod Johnson
 * @author Rick Evans
 * @author Chris Beams
 */
public final class TypePatternClassFilterTests {

	@Test(expected=IllegalArgumentException.class)
	public void testInvalidPattern() {
		// should throw - pattern must be recognized as invalid
		new TypePatternClassFilter("-");
	}

	@Test
	public void testValidPatternMatching() {
		TypePatternClassFilter tpcf = new TypePatternClassFilter("test.beans.*");
		assertTrue("Must match: in package", tpcf.matches(TestBean.class));
		assertTrue("Must match: in package", tpcf.matches(ITestBean.class));
		assertTrue("Must match: in package", tpcf.matches(IOther.class));
		assertFalse("Must be excluded: in wrong package", tpcf.matches(DeepBean.class));
		assertFalse("Must be excluded: in wrong package", tpcf.matches(BeanFactory.class));
		assertFalse("Must be excluded: in wrong package", tpcf.matches(DefaultListableBeanFactory.class));
	}

	@Test
	public void testSubclassMatching() {
		TypePatternClassFilter tpcf = new TypePatternClassFilter("test.beans.ITestBean+");
		assertTrue("Must match: in package", tpcf.matches(TestBean.class));
		assertTrue("Must match: in package", tpcf.matches(ITestBean.class));
		assertTrue("Must match: in package", tpcf.matches(CountingTestBean.class));
		assertFalse("Must be excluded: not subclass", tpcf.matches(IOther.class));
		assertFalse("Must be excluded: not subclass", tpcf.matches(DefaultListableBeanFactory.class));
	}
	
	@Test
	public void testAndOrNotReplacement() {
		TypePatternClassFilter tpcf = new TypePatternClassFilter("java.lang.Object or java.lang.String");
		assertFalse("matches Number",tpcf.matches(Number.class));
		assertTrue("matches Object",tpcf.matches(Object.class));
		assertTrue("matchesString",tpcf.matches(String.class));
		tpcf = new TypePatternClassFilter("java.lang.Number+ and java.lang.Float");
		assertTrue("matches Float",tpcf.matches(Float.class));
		assertFalse("matches Double",tpcf.matches(Double.class));
		tpcf = new TypePatternClassFilter("java.lang.Number+ and not java.lang.Float");
		assertFalse("matches Float",tpcf.matches(Float.class));
		assertTrue("matches Double",tpcf.matches(Double.class));	
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSetTypePatternWithNullArgument() throws Exception {
	    new TypePatternClassFilter(null);
	}

	@Test(expected=IllegalStateException.class)
	public void testInvocationOfMatchesMethodBlowsUpWhenNoTypePatternHasBeenSet() throws Exception {
		new TypePatternClassFilter().matches(String.class);
	}

}
