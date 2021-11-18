/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.config;

import static org.junit.Assert.assertTrue;
import static test.util.TestResourceUtils.qualifiedResource;

import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;

/**
 * Tests that the &lt;aop:config/&gt; element can be used as a top level element.
 * 
 * @author Rob Harrop
 * @author Chris Beams
 */
public final class TopLevelAopTagTests {
	
	private static final Resource CONTEXT = qualifiedResource(TopLevelAopTagTests.class, "context.xml");

	@Test
	public void testParse() throws Exception {
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
		reader.loadBeanDefinitions(CONTEXT);

		assertTrue(beanFactory.containsBeanDefinition("testPointcut"));
	}

}
