/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.aspectj;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Test;


/**
 * Additional parameter name discover tests that need Java 5.
 * Yes this will re-run the tests from the superclass, but that
 * doesn't matter in the grand scheme of things...
 *
 * @author Adrian Colyer
 * @author Chris Beams
 */
public final class AspectJAdviceParameterNameDiscoverAnnotationTests
		extends AspectJAdviceParameterNameDiscovererTests {

	@Retention(RetentionPolicy.RUNTIME)
	@interface MyAnnotation {}
	
	public void pjpAndAnAnnotation(ProceedingJoinPoint pjp, MyAnnotation ann) {}

	@Test
	public void testAnnotationBinding() {
		assertParameterNames(getMethod("pjpAndAnAnnotation"),
				"execution(* *(..)) && @annotation(ann)",
				new String[] {"thisJoinPoint","ann"});
	}

}
