/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.propertyeditors;

import junit.framework.TestCase;

import java.beans.PropertyEditor;

/**
 * Unit tests for the {@link CharArrayPropertyEditor} class.
 *
 * @author Rick Evans
 */
public final class CharArrayPropertyEditorTests extends TestCase {

	public void testSunnyDaySetAsText() throws Exception {
		final String text = "Hideous towns make me throw... up";

		PropertyEditor charEditor = new CharArrayPropertyEditor();
		charEditor.setAsText(text);

		Object value = charEditor.getValue();
		assertNotNull(value);
		assertTrue(value instanceof char[]);
		char[] chars = (char[]) value;
		for (int i = 0; i < text.length(); ++i) {
			assertEquals("char[] differs at index '" + i + "'", text.charAt(i), chars[i]);
		}
		assertEquals(text, charEditor.getAsText());
	}

	public void testGetAsTextReturnsEmptyStringIfValueIsNull() throws Exception {
		PropertyEditor charEditor = new CharArrayPropertyEditor();
		assertEquals("", charEditor.getAsText());

		charEditor.setAsText(null);
		assertEquals("", charEditor.getAsText());
	}

}
