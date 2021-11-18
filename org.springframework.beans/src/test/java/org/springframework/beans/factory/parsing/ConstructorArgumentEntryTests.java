/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.parsing;

import org.junit.Test;

/**
 * Unit tests for {@link ConstructorArgumentEntry}.
 *
 * @author Rick Evans
 * @author Chris Beams
 */
public final class ConstructorArgumentEntryTests {

	@Test(expected=IllegalArgumentException.class)
	public void testCtorBailsOnNegativeCtorIndexArgument() {
		new ConstructorArgumentEntry(-1);
	}

}
