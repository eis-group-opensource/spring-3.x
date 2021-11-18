/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.aspectj.annotation;

/**
 * Tests for ReflectiveAtAspectJAdvisorFactory. 
 * Tests are inherited: we only set the test fixture here.
 *
 * @author Rod Johnson
 * @since 2.0
 */
public final class ReflectiveAspectJAdvisorFactoryTests extends AbstractAspectJAdvisorFactoryTests {

	@Override
	protected AspectJAdvisorFactory getFixture() {
		return new ReflectiveAspectJAdvisorFactory();
	}

}
