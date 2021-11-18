/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.support;

import junit.framework.TestCase;
import org.springframework.core.io.ClassPathResource;

import test.beans.TestBean;

/**
 * @author Rob Harrop
 */
public class PropertiesBeanDefinitionReaderTests extends TestCase {

	private DefaultListableBeanFactory beanFactory;

	private PropertiesBeanDefinitionReader reader;

	protected void setUp() throws Exception {
		this.beanFactory = new DefaultListableBeanFactory();
		this.reader = new PropertiesBeanDefinitionReader(beanFactory);
	}

	public void testWithSimpleConstructorArg() {
		this.reader.loadBeanDefinitions(new ClassPathResource("simpleConstructorArg.properties", getClass()));
		TestBean bean = (TestBean)this.beanFactory.getBean("testBean");
		assertEquals("Rob Harrop", bean.getName());
	}

	public void testWithConstructorArgRef() throws Exception {
		this.reader.loadBeanDefinitions(new ClassPathResource("refConstructorArg.properties", getClass()));
		TestBean rob = (TestBean)this.beanFactory.getBean("rob");
		TestBean sally = (TestBean)this.beanFactory.getBean("sally");
		assertEquals(sally, rob.getSpouse());
	}

	public void testWithMultipleConstructorsArgs() throws Exception {
		this.reader.loadBeanDefinitions(new ClassPathResource("multiConstructorArgs.properties", getClass()));
		TestBean bean = (TestBean)this.beanFactory.getBean("testBean");
		assertEquals("Rob Harrop", bean.getName());
		assertEquals(23, bean.getAge());
	}
}
