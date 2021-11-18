/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.propertyeditors;

import junit.framework.TestCase;

import java.beans.PropertyEditor;

/**
 * Unit tests for the {@link ByteArrayPropertyEditor} class.
 *
 * @author Rick Evans
 */
public final class ByteArrayPropertyEditorTests extends TestCase {

	public void testSunnyDaySetAsText() throws Exception {
		final String text = "Hideous towns make me throw... up";

		PropertyEditor byteEditor = new ByteArrayPropertyEditor();
		byteEditor.setAsText(text);

		Object value = byteEditor.getValue();
		assertNotNull(value);
		assertTrue(value instanceof byte[]);
		byte[] bytes = (byte[]) value;
		for (int i = 0; i < text.length(); ++i) {
			assertEquals("cyte[] differs at index '" + i + "'", text.charAt(i), bytes[i]);
		}
		assertEquals(text, byteEditor.getAsText());
	}

	public void testGetAsTextReturnsEmptyStringIfValueIsNull() throws Exception {
		PropertyEditor byteEditor = new ByteArrayPropertyEditor();
		assertEquals("", byteEditor.getAsText());
		
		byteEditor.setAsText(null);
		assertEquals("", byteEditor.getAsText());
	}

}
