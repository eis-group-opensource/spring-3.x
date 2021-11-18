/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.parsing;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit tests for {@link PassThroughSourceExtractor}.
 * 
 * @author Rick Evans
 * @author Chris Beams
 */
public final class PassThroughSourceExtractorTests {

	@Test
	public void testPassThroughContract() throws Exception {
		Object source  = new Object();
		Object extractedSource = new PassThroughSourceExtractor().extractSource(source, null);
		assertSame("The contract of PassThroughSourceExtractor states that the supplied " +
				"source object *must* be returned as-is", source, extractedSource);
	}

	@Test
	public void testPassThroughContractEvenWithNull() throws Exception {
		Object extractedSource = new PassThroughSourceExtractor().extractSource(null, null);
		assertNull("The contract of PassThroughSourceExtractor states that the supplied " +
				"source object *must* be returned as-is (even if null)", extractedSource);
	}

}
