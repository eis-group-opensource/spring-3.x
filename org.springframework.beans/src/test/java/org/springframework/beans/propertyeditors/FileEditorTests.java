/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.propertyeditors;

import static org.junit.Assert.*;

import java.beans.PropertyEditor;
import java.io.File;

import org.junit.Test;
import org.springframework.util.ClassUtils;

/**
 * @author Thomas Risberg
 * @author Chris Beams
 */
public final class FileEditorTests {

	@Test
	public void testClasspathFileName() throws Exception {
		PropertyEditor fileEditor = new FileEditor();
		fileEditor.setAsText("classpath:" + ClassUtils.classPackageAsResourcePath(getClass()) + "/"
				+ ClassUtils.getShortName(getClass()) + ".class");
		Object value = fileEditor.getValue();
		assertTrue(value instanceof File);
		File file = (File) value;
		assertTrue(file.exists());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testWithNonExistentResource() throws Exception {
		PropertyEditor propertyEditor = new FileEditor();
		propertyEditor.setAsText("classpath:no_way_this_file_is_found.doc");
	}

	@Test
	public void testWithNonExistentFile() throws Exception {
		PropertyEditor fileEditor = new FileEditor();
		fileEditor.setAsText("file:no_way_this_file_is_found.doc");
		Object value = fileEditor.getValue();
		assertTrue(value instanceof File);
		File file = (File) value;
		assertTrue(!file.exists());
	}

	@Test
	public void testAbsoluteFileName() throws Exception {
		PropertyEditor fileEditor = new FileEditor();
		fileEditor.setAsText("/no_way_this_file_is_found.doc");
		Object value = fileEditor.getValue();
		assertTrue(value instanceof File);
		File file = (File) value;
		assertTrue(!file.exists());
	}

	@Test
	public void testUnqualifiedFileNameFound() throws Exception {
		PropertyEditor fileEditor = new FileEditor();
		String fileName = ClassUtils.classPackageAsResourcePath(getClass()) + "/" + ClassUtils.getShortName(getClass())
				+ ".class";
		fileEditor.setAsText(fileName);
		Object value = fileEditor.getValue();
		assertTrue(value instanceof File);
		File file = (File) value;
		assertTrue(file.exists());
		String absolutePath = file.getAbsolutePath();
		if (File.separatorChar == '\\') {
			absolutePath = absolutePath.replace('\\', '/');
		}
		assertTrue(absolutePath.endsWith(fileName));
	}

	@Test
	public void testUnqualifiedFileNameNotFound() throws Exception {
		PropertyEditor fileEditor = new FileEditor();
		String fileName = ClassUtils.classPackageAsResourcePath(getClass()) + "/" + ClassUtils.getShortName(getClass())
				+ ".clazz";
		fileEditor.setAsText(fileName);
		Object value = fileEditor.getValue();
		assertTrue(value instanceof File);
		File file = (File) value;
		assertFalse(file.exists());
		String absolutePath = file.getAbsolutePath();
		if (File.separatorChar == '\\') {
			absolutePath = absolutePath.replace('\\', '/');
		}
		assertTrue(absolutePath.endsWith(fileName));
	}
}
