/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.aspectj.annotation;

import static org.junit.Assert.*;
import org.aspectj.lang.reflect.PerClauseKind;
import org.junit.Test;

import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.annotation.AbstractAspectJAdvisorFactoryTests.ExceptionAspect;

import test.aop.PerTargetAspect;


/**
 * @since 2.0
 * @author Rod Johnson
 * @author Chris Beams
 */
public final class AspectMetadataTests {

	@Test(expected=IllegalArgumentException.class)
	public void testNotAnAspect() {
		new AspectMetadata(String.class,"someBean");
	}
	
	@Test
	public void testSingletonAspect() {
		AspectMetadata am = new AspectMetadata(ExceptionAspect.class,"someBean");
		assertFalse(am.isPerThisOrPerTarget());
		assertSame(Pointcut.TRUE, am.getPerClausePointcut());
		assertEquals(PerClauseKind.SINGLETON, am.getAjType().getPerClause().getKind());
	}
	
	@Test
	public void testPerTargetAspect() {
		AspectMetadata am = new AspectMetadata(PerTargetAspect.class,"someBean");
		assertTrue(am.isPerThisOrPerTarget());
		assertNotSame(Pointcut.TRUE, am.getPerClausePointcut());
		assertEquals(PerClauseKind.PERTARGET, am.getAjType().getPerClause().getKind());
	}
	
	@Test
	public void testPerThisAspect() {
		AspectMetadata am = new AspectMetadata(PerThisAspect.class,"someBean");
		assertTrue(am.isPerThisOrPerTarget());
		assertNotSame(Pointcut.TRUE, am.getPerClausePointcut());
		assertEquals(PerClauseKind.PERTHIS, am.getAjType().getPerClause().getKind());
	}
}
