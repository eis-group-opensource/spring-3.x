/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scripting.support;

import junit.framework.TestCase;

/**
 * Unit tests for the StaticScriptSource class.
 *
 * @author Rick Evans
 */
public final class StaticScriptSourceTests extends TestCase {

	private static final String SCRIPT_TEXT = "print($hello) if $true;";


	public void testCreateWithNullScript() throws Exception {
		try {
			new StaticScriptSource(null);
			fail("Must have failed when passed a null script string.");
		}
		catch (IllegalArgumentException expected) {
		}
	}

	public void testCreateWithEmptyScript() throws Exception {
		try {
			new StaticScriptSource("");
			fail("Must have failed when passed an empty script string.");
		}
		catch (IllegalArgumentException expected) {
		}
	}

	public void testCreateWithWhitespaceOnlyScript() throws Exception {
		try {
			new StaticScriptSource("   \n\n\t  \t\n");
			fail("Must have failed when passed a whitespace-only script string.");
		}
		catch (IllegalArgumentException expected) {
		}
	}

	public void testIsModifiedIsTrueByDefault() throws Exception {
		StaticScriptSource source = new StaticScriptSource(SCRIPT_TEXT);
		assertTrue("Script must be flagged as 'modified' when first created.", source.isModified());
	}

	public void testGettingScriptTogglesIsModified() throws Exception {
		StaticScriptSource source = new StaticScriptSource(SCRIPT_TEXT);
		source.getScriptAsString();
		assertFalse("Script must be flagged as 'not modified' after script is read.", source.isModified());
	}

	public void testGettingScriptViaToStringDoesNotToggleIsModified() throws Exception {
		StaticScriptSource source = new StaticScriptSource(SCRIPT_TEXT);
		boolean isModifiedState = source.isModified();
		source.toString();
		assertEquals("Script's 'modified' flag must not change after script is read via toString().", isModifiedState, source.isModified());
	}

	public void testIsModifiedToggledWhenDifferentScriptIsSet() throws Exception {
		StaticScriptSource source = new StaticScriptSource(SCRIPT_TEXT);
		source.setScript("use warnings;");
		assertTrue("Script must be flagged as 'modified' when different script is passed in.", source.isModified());
	}

	public void testIsModifiedNotToggledWhenSameScriptIsSet() throws Exception {
		StaticScriptSource source = new StaticScriptSource(SCRIPT_TEXT);
		source.setScript(SCRIPT_TEXT);
		assertFalse("Script must not be flagged as 'modified' when same script is passed in.", source.isModified());
	}

}
