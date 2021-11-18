/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.parsing;

import org.junit.Test;

/**
 * Unit tests for {@link PropertyEntry}.
 *
 * @author Rick Evans
 * @author Chris Beams
 */
public final class PropertyEntryTests {

	@Test(expected=IllegalArgumentException.class)
	public void testCtorBailsOnNullPropertyNameArgument() throws Exception {
		new PropertyEntry(null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCtorBailsOnEmptyPropertyNameArgument() throws Exception {
		new PropertyEntry("");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCtorBailsOnWhitespacedPropertyNameArgument() throws Exception {
		new PropertyEntry("\t   ");
	}

}
