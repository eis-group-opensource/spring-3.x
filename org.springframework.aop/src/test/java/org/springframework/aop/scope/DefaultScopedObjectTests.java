/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.scope;

import static org.easymock.EasyMock.*;

import org.junit.Test;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * Unit tests for the {@link DefaultScopedObject} class.
 *
 * @author Rick Evans
 * @author Chris Beams
 */
public final class DefaultScopedObjectTests {

	private static final String GOOD_BEAN_NAME = "foo";


	@Test(expected=IllegalArgumentException.class)
	public void testCtorWithNullBeanFactory() throws Exception {
		new DefaultScopedObject(null, GOOD_BEAN_NAME);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCtorWithNullTargetBeanName() throws Exception {
		testBadTargetBeanName(null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCtorWithEmptyTargetBeanName() throws Exception {
		testBadTargetBeanName("");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCtorWithJustWhitespacedTargetBeanName() throws Exception {
		testBadTargetBeanName("   ");
	}


	private static void testBadTargetBeanName(final String badTargetBeanName) {
		ConfigurableBeanFactory factory = createMock(ConfigurableBeanFactory.class);
		replay(factory);

		new DefaultScopedObject(factory, badTargetBeanName);

		verify(factory);
	}

}
