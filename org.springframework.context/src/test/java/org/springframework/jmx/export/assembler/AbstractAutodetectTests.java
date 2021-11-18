/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export.assembler;

import junit.framework.TestCase;

import org.springframework.jmx.JmxTestBean;

/**
 * @author Rob Harrop
 */
public abstract class AbstractAutodetectTests extends TestCase {

	public void testAutodetect() throws Exception {
		JmxTestBean bean = new JmxTestBean();

		AutodetectCapableMBeanInfoAssembler assembler = getAssembler();
		assertTrue("The bean should be included", assembler.includeBean(bean.getClass(), "testBean"));
	}

	protected abstract AutodetectCapableMBeanInfoAssembler getAssembler();

}
