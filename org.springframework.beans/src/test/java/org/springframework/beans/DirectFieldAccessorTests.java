/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans;

import static org.junit.Assert.assertEquals;

import javax.swing.JPanel;
import javax.swing.JTextField;

import org.junit.Test;

/**
 * Unit tests for {@link DirectFieldAccessor}
 *
 * @author Jose Luis Martin
 * @author Chris Beams
 */
public class DirectFieldAccessorTests {

	@Test
	public void withShadowedField() throws Exception {
		@SuppressWarnings("serial")
		JPanel p = new JPanel() {
			@SuppressWarnings("unused")
			JTextField name = new JTextField();
		};

		DirectFieldAccessor dfa = new DirectFieldAccessor(p);
		assertEquals(JTextField.class, dfa.getPropertyType("name"));
	}
}
