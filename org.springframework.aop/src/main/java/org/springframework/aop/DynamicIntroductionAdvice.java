/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop;

import org.aopalliance.aop.Advice;

/** 
 * Subinterface of AOP Alliance Advice that allows additional interfaces
 * to be implemented by an Advice, and available via a proxy using that
 * interceptor. This is a fundamental AOP concept called <b>introduction</b>.
 *
 * <p>Introductions are often <b>mixins</b>, enabling the building of composite
 * objects that can achieve many of the goals of multiple inheritance in Java.
 *
 * <p>Compared to {qlink IntroductionInfo}, this interface allows an advice to
 * implement a range of interfaces that is not necessarily known in advance.
 * Thus an {@link IntroductionAdvisor} can be used to specify which interfaces
 * will be exposed in an advised object.
 *
 * @author Rod Johnson
 * @since 1.1.1
 * @see IntroductionInfo
 * @see IntroductionAdvisor
 */
public interface DynamicIntroductionAdvice extends Advice {
	
	/**
	 * Does this introduction advice implement the given interface?
	 * @param intf the interface to check
	 * @return whether the advice implements the specified interface
	 */
	boolean implementsInterface(Class<?> intf);

}
