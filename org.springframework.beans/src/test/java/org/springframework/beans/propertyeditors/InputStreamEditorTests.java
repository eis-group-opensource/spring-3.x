/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.propertyeditors;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Test;
import org.springframework.util.ClassUtils;

/**
 * Unit tests for the {@link InputStreamEditor} class.
 *
 * @author Rick Evans
 * @author Chris Beams
 */
public final class InputStreamEditorTests {

	@Test(expected=IllegalArgumentException.class)
	public void testCtorWithNullResourceEditor() throws Exception {
		new InputStreamEditor(null);
	}

	@Test
	public void testSunnyDay() throws Exception {
		InputStream stream = null;
		try {
			String resource = "classpath:" + ClassUtils.classPackageAsResourcePath(getClass()) + "/" + ClassUtils.getShortName(getClass()) + ".class";
			InputStreamEditor editor = new InputStreamEditor();
			editor.setAsText(resource);
			Object value = editor.getValue();
			assertNotNull(value);
			assertTrue(value instanceof InputStream);
			stream = (InputStream) value;
			assertTrue(stream.available() > 0);
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}

	@Test(expected=IllegalArgumentException.class)
	public void testWhenResourceDoesNotExist() throws Exception {
		String resource = "classpath:bingo!";
		InputStreamEditor editor = new InputStreamEditor();
		editor.setAsText(resource);
	}

	@Test
	public void testGetAsTextReturnsNullByDefault() throws Exception {
		assertNull(new InputStreamEditor().getAsText());
		String resource = "classpath:" + ClassUtils.classPackageAsResourcePath(getClass()) + "/" + ClassUtils.getShortName(getClass()) + ".class";
		InputStreamEditor editor = new InputStreamEditor();
		editor.setAsText(resource);
		assertNull(editor.getAsText());
	}

}
