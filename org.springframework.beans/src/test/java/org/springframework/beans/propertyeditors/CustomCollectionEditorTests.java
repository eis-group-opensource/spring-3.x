/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.propertyeditors;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

/**
 * Unit tests for the {@link CustomCollectionEditor} class.
 *
 * @author Rick Evans
 * @author Chris Beams
 */
public final class CustomCollectionEditorTests {

	@Test(expected=IllegalArgumentException.class)
	public void testCtorWithNullCollectionType() throws Exception {
		new CustomCollectionEditor(null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCtorWithNonCollectionType() throws Exception {
		new CustomCollectionEditor(String.class);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testWithCollectionTypeThatDoesNotExposeAPublicNoArgCtor() throws Exception {
		CustomCollectionEditor editor = new CustomCollectionEditor(CollectionTypeWithNoNoArgCtor.class);
		editor.setValue("1");
	}

	@Test
	public void testSunnyDaySetValue() throws Exception {
		CustomCollectionEditor editor = new CustomCollectionEditor(ArrayList.class);
		editor.setValue(new int[] {0, 1, 2});
		Object value = editor.getValue();
		assertNotNull(value);
		assertTrue(value instanceof ArrayList);
		List<?> list = (List<?>) value;
		assertEquals("There must be 3 elements in the converted collection", 3, list.size());
		assertEquals(new Integer(0), list.get(0));
		assertEquals(new Integer(1), list.get(1));
		assertEquals(new Integer(2), list.get(2));
	}

	@Test
	public void testWhenTargetTypeIsExactlyTheCollectionInterfaceUsesFallbackCollectionType() throws Exception {
		CustomCollectionEditor editor = new CustomCollectionEditor(Collection.class);
		editor.setValue("0, 1, 2");
		Collection<?> value = (Collection<?>) editor.getValue();
		assertNotNull(value);
		assertEquals("There must be 1 element in the converted collection", 1, value.size());
		assertEquals("0, 1, 2", value.iterator().next());
	}

	@Test
	public void testSunnyDaySetAsTextYieldsSingleValue() throws Exception {
		CustomCollectionEditor editor = new CustomCollectionEditor(ArrayList.class);
		editor.setValue("0, 1, 2");
		Object value = editor.getValue();
		assertNotNull(value);
		assertTrue(value instanceof ArrayList);
		List<?> list = (List<?>) value;
		assertEquals("There must be 1 element in the converted collection", 1, list.size());
		assertEquals("0, 1, 2", list.get(0));
	}


	@SuppressWarnings("serial")
	private static final class CollectionTypeWithNoNoArgCtor extends ArrayList<Object> {
		public CollectionTypeWithNoNoArgCtor(String anArg) {
		}
	}

}
