/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.xml;

import junit.framework.TestCase;

import org.junit.Test;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * Unit tests for the {@link DelegatingEntityResolver} class.
 *
 * @author Rick Evans
 * @author Chris Beams
 */
public final class DelegatingEntityResolverTests {

	@Test(expected=IllegalArgumentException.class)
	public void testCtorWhereDtdEntityResolverIsNull() throws Exception {
		new DelegatingEntityResolver(null, new NoOpEntityResolver());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCtorWhereSchemaEntityResolverIsNull() throws Exception {
		new DelegatingEntityResolver(new NoOpEntityResolver(), null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCtorWhereEntityResolversAreBothNull() throws Exception {
		new DelegatingEntityResolver(null, null);
	}


	private static final class NoOpEntityResolver implements EntityResolver {
		public InputSource resolveEntity(String publicId, String systemId) {
			return null;
		}
	}

}
