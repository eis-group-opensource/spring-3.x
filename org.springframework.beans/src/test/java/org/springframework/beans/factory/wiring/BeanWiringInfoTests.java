/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.wiring;

import junit.framework.TestCase;

/**
 * Unit tests for the BeanWiringInfo class.
 *
 * @author Rick Evans
 */
public final class BeanWiringInfoTests extends TestCase {

	public void testCtorWithNullBeanName() throws Exception {
		try {
			new BeanWiringInfo(null);
			fail("Must have thrown an IllegalArgumentException by this point (null argument).");
		}
		catch (IllegalArgumentException ex) {
		}
	}

	public void testCtorWithWhitespacedBeanName() throws Exception {
		try {
			new BeanWiringInfo("   \t");
			fail("Must have thrown an IllegalArgumentException by this point (bean name has only whitespace).");
		}
		catch (IllegalArgumentException ex) {
		}
	}

	public void testCtorWithEmptyBeanName() throws Exception {
		try {
			new BeanWiringInfo("");
			fail("Must have thrown an IllegalArgumentException by this point (bean name is empty).");
		}
		catch (IllegalArgumentException ex) {
		}
	}

	public void testCtorWithNegativeIllegalAutowiringValue() throws Exception {
		try {
			new BeanWiringInfo(-1, true);
			fail("Must have thrown an IllegalArgumentException by this point (out-of-range argument).");
		}
		catch (IllegalArgumentException ex) {
		}
	}

	public void testCtorWithPositiveOutOfRangeAutowiringValue() throws Exception {
		try {
			new BeanWiringInfo(123871, true);
			fail("Must have thrown an IllegalArgumentException by this point (out-of-range argument).");
		}
		catch (IllegalArgumentException ex) {
		}
	}

	public void testUsingAutowireCtorIndicatesAutowiring() throws Exception {
		BeanWiringInfo info = new BeanWiringInfo(BeanWiringInfo.AUTOWIRE_BY_NAME, true);
		assertTrue(info.indicatesAutowiring());
	}

	public void testUsingBeanNameCtorDoesNotIndicateAutowiring() throws Exception {
		BeanWiringInfo info = new BeanWiringInfo("fooService");
		assertFalse(info.indicatesAutowiring());
	}

	public void testNoDependencyCheckValueIsPreserved() throws Exception {
		BeanWiringInfo info = new BeanWiringInfo(BeanWiringInfo.AUTOWIRE_BY_NAME, true);
		assertTrue(info.getDependencyCheck());
	}

	public void testDependencyCheckValueIsPreserved() throws Exception {
		BeanWiringInfo info = new BeanWiringInfo(BeanWiringInfo.AUTOWIRE_BY_TYPE, false);
		assertFalse(info.getDependencyCheck());
	}

}
