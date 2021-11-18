/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.support;

import org.junit.Test;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public class GenericApplicationContextTests {

	@Test
	public void nullBeanRegistration() {
			DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
			bf.registerSingleton("nullBean", null);
			new GenericApplicationContext(bf).refresh();
	}

}
