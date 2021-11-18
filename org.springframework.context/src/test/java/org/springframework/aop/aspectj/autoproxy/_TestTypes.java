/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.aspectj.autoproxy;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.aspectj.lang.ProceedingJoinPoint;


/**
 * Definitions of testing types for use in within this package.
 * Wherever possible, test types should be defined local to the java
 * file that makes use of them.  In some cases however, a test type may
 * need to be shared across tests.  Such types reside here, with the
 * intention of reducing the surface area of java files within this
 * package.  This allows developers to think about tests first, and deal
 * with these second class testing artifacts on an as-needed basis.
 * 
 * Types here should be defined as package-private top level classes in
 * order to avoid needing to fully qualify, e.g.: _TestTypes$Foo.
 *
 * @author Chris Beams
 */
final class _TestTypes { }


/**
 * @author Adrian Colyer
 * @since 2.0
 */
interface AnnotatedTestBean {

	String doThis();

	String doThat();

	String doTheOther();

	String[] doArray();

}


/**
 * @author Adrian Colyer
 * @since 2.0
 */
@Retention(RetentionPolicy.RUNTIME)
@interface TestAnnotation {
	String value() ;
}


/**
 * @author Adrian Colyer
 * @since 2.0
 */
class AnnotatedTestBeanImpl implements AnnotatedTestBean {

	@TestAnnotation("this value")
	public String doThis() {
		return "doThis";
	}

	@TestAnnotation("that value")
	public String doThat() {
		return "doThat";
	}

	@TestAnnotation("array value")
	public String[] doArray() {
		return new String[] {"doThis", "doThat"};
	}

	// not annotated
	public String doTheOther() {
		return "doTheOther";
	}

}


/**
 * @author Adrian Colyer
 */
class AnnotationBindingTestAspect {

	public String doWithAnnotation(ProceedingJoinPoint pjp, TestAnnotation testAnnotation) throws Throwable {
		return testAnnotation.value();
	}

}
