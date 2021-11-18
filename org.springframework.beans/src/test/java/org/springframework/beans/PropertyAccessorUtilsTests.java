/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

/**
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public final class PropertyAccessorUtilsTests {

	@Test
	public void testCanonicalPropertyName() {
		assertEquals("map", PropertyAccessorUtils.canonicalPropertyName("map"));
		assertEquals("map[key1]", PropertyAccessorUtils.canonicalPropertyName("map[key1]"));
		assertEquals("map[key1]", PropertyAccessorUtils.canonicalPropertyName("map['key1']"));
		assertEquals("map[key1]", PropertyAccessorUtils.canonicalPropertyName("map[\"key1\"]"));
		assertEquals("map[key1][key2]", PropertyAccessorUtils.canonicalPropertyName("map[key1][key2]"));
		assertEquals("map[key1][key2]", PropertyAccessorUtils.canonicalPropertyName("map['key1'][\"key2\"]"));
		assertEquals("map[key1].name", PropertyAccessorUtils.canonicalPropertyName("map[key1].name"));
		assertEquals("map[key1].name", PropertyAccessorUtils.canonicalPropertyName("map['key1'].name"));
		assertEquals("map[key1].name", PropertyAccessorUtils.canonicalPropertyName("map[\"key1\"].name"));
	}

	@Test
	public void testCanonicalPropertyNames() {
		String[] original =
				new String[] {"map", "map[key1]", "map['key1']", "map[\"key1\"]", "map[key1][key2]",
											"map['key1'][\"key2\"]", "map[key1].name", "map['key1'].name", "map[\"key1\"].name"};
		String[] canonical =
				new String[] {"map", "map[key1]", "map[key1]", "map[key1]", "map[key1][key2]",
											"map[key1][key2]", "map[key1].name", "map[key1].name", "map[key1].name"};

		assertTrue(Arrays.equals(canonical, PropertyAccessorUtils.canonicalPropertyNames(original)));
	}

}
