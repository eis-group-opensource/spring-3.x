/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.instrument.classloading.oc4j;

import org.junit.Test;

/**
 * Unit tests for the {@link OC4JLoadTimeWeaver} class.
 *
 * @author Rick Evans
 * @author Chris Beams
 */
public final class OC4JLoadTimeWeaverTests {

	@Test(expected=IllegalArgumentException.class)
	public void testCtorWithNullClassLoader() {
		new OC4JLoadTimeWeaver(null);
	}

}
