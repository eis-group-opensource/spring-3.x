/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.xml;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import test.beans.ITestBean;
import test.beans.TestBean;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @author Arjen Poutsma
 */
public class SimplePropertyNamespaceHandlerTests {

	@Test
	public void simpleBeanConfigured() throws Exception {
		XmlBeanFactory beanFactory =
				new XmlBeanFactory(new ClassPathResource("simplePropertyNamespaceHandlerTests.xml", getClass()));
		ITestBean rob = (TestBean) beanFactory.getBean("rob");
		ITestBean sally = (TestBean) beanFactory.getBean("sally");
		assertEquals("Rob Harrop", rob.getName());
		assertEquals(24, rob.getAge());
		assertEquals(rob.getSpouse(), sally);
	}

	@Test
	public void innerBeanConfigured() throws Exception {
		XmlBeanFactory beanFactory =
				new XmlBeanFactory(new ClassPathResource("simplePropertyNamespaceHandlerTests.xml", getClass()));
		TestBean sally = (TestBean) beanFactory.getBean("sally2");
		ITestBean rob = sally.getSpouse();
		assertEquals("Rob Harrop", rob.getName());
		assertEquals(24, rob.getAge());
		assertEquals(rob.getSpouse(), sally);
	}

	@Test(expected = BeanDefinitionStoreException.class)
	public void withPropertyDefinedTwice() throws Exception {
		new XmlBeanFactory(new ClassPathResource("simplePropertyNamespaceHandlerTestsWithErrors.xml", getClass()));
	}

	@Test
	public void propertyWithNameEndingInRef() throws Exception {
		XmlBeanFactory beanFactory =
				new XmlBeanFactory(new ClassPathResource("simplePropertyNamespaceHandlerTests.xml", getClass()));
		ITestBean sally = (TestBean) beanFactory.getBean("derivedSally");
		assertEquals("r", sally.getSpouse().getName());
	}

}
