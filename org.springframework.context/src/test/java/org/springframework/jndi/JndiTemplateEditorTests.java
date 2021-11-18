/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/


package org.springframework.jndi;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Rod Johnson
 * @author Chris Beams
 */
public class JndiTemplateEditorTests {

	@Test
	public void testNullIsIllegalArgument() {
		try {
			new JndiTemplateEditor().setAsText(null);
			fail("Null is illegal");
		}
		catch (IllegalArgumentException ex) {
			// OK
		}
	}

	@Test
	public void testEmptyStringMeansNullEnvironment() {
		JndiTemplateEditor je = new JndiTemplateEditor();
		je.setAsText("");
		JndiTemplate jt = (JndiTemplate) je.getValue();
		assertTrue(jt.getEnvironment() == null);
	}

	@Test
	public void testCustomEnvironment() {
		JndiTemplateEditor je = new JndiTemplateEditor();
		// These properties are meaningless for JNDI, but we don't worry about that:
		// the underlying JNDI implementation will throw exceptions when the user tries
		// to look anything up
		je.setAsText("jndiInitialSomethingOrOther=org.springframework.myjndi.CompleteRubbish\nfoo=bar");
		JndiTemplate jt = (JndiTemplate) je.getValue();
		assertTrue(jt.getEnvironment().size() == 2);
		assertTrue(jt.getEnvironment().getProperty("jndiInitialSomethingOrOther").equals("org.springframework.myjndi.CompleteRubbish"));
		assertTrue(jt.getEnvironment().getProperty("foo").equals("bar"));
	}

}
