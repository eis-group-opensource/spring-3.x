/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.ejb.config;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.ITestBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ejb.access.LocalStatelessSessionProxyFactoryBean;
import org.springframework.ejb.access.SimpleRemoteStatelessSessionProxyFactoryBean;
import org.springframework.jndi.JndiObjectFactoryBean;

/**
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @author Chris Beams
 * @author Oliver Gierke
 */
public class JeeNamespaceHandlerTests {

	private ConfigurableListableBeanFactory beanFactory;

	@Before
	public void setUp() throws Exception {
		GenericApplicationContext ctx = new GenericApplicationContext();
		new XmlBeanDefinitionReader(ctx).loadBeanDefinitions(
				new ClassPathResource("jeeNamespaceHandlerTests.xml", getClass()));
		ctx.refresh();
		this.beanFactory = ctx.getBeanFactory();
		this.beanFactory.getBeanNamesForType(ITestBean.class);
	}

	@Test
	public void testSimpleDefinition() throws Exception {
		BeanDefinition beanDefinition = this.beanFactory.getMergedBeanDefinition("simple");
		assertEquals(JndiObjectFactoryBean.class.getName(), beanDefinition.getBeanClassName());
		assertPropertyValue(beanDefinition, "jndiName", "jdbc/MyDataSource");
		assertPropertyValue(beanDefinition, "resourceRef", "true");
	}

	@Test
	public void testComplexDefinition() throws Exception {
		BeanDefinition beanDefinition = this.beanFactory.getMergedBeanDefinition("complex");
		assertEquals(JndiObjectFactoryBean.class.getName(), beanDefinition.getBeanClassName());
		assertPropertyValue(beanDefinition, "jndiName", "jdbc/MyDataSource");
		assertPropertyValue(beanDefinition, "resourceRef", "true");
		assertPropertyValue(beanDefinition, "cache", "true");
		assertPropertyValue(beanDefinition, "lookupOnStartup", "true");
		assertPropertyValue(beanDefinition, "exposeAccessContext", "true");
		assertPropertyValue(beanDefinition, "expectedType", "com.myapp.DefaultFoo");
		assertPropertyValue(beanDefinition, "proxyInterface", "com.myapp.Foo");
		assertPropertyValue(beanDefinition, "defaultObject", "myValue");
	}

	@Test
	public void testWithEnvironment() throws Exception {
		BeanDefinition beanDefinition = this.beanFactory.getMergedBeanDefinition("withEnvironment");
		assertPropertyValue(beanDefinition, "jndiEnvironment", "foo=bar");
		assertPropertyValue(beanDefinition, "defaultObject", new RuntimeBeanReference("myBean"));
	}

	@Test
	public void testWithReferencedEnvironment() throws Exception {
		BeanDefinition beanDefinition = this.beanFactory.getMergedBeanDefinition("withReferencedEnvironment");
		assertPropertyValue(beanDefinition, "jndiEnvironment", new RuntimeBeanReference("myEnvironment"));
		assertFalse(beanDefinition.getPropertyValues().contains("environmentRef"));
	}

	@Test
	public void testSimpleLocalSlsb() throws Exception {
		BeanDefinition beanDefinition = this.beanFactory.getMergedBeanDefinition("simpleLocalEjb");
		assertEquals(LocalStatelessSessionProxyFactoryBean.class.getName(), beanDefinition.getBeanClassName());
		assertPropertyValue(beanDefinition, "businessInterface", ITestBean.class.getName());
		assertPropertyValue(beanDefinition, "jndiName", "ejb/MyLocalBean");
	}

	@Test
	public void testSimpleRemoteSlsb() throws Exception {
		BeanDefinition beanDefinition = this.beanFactory.getMergedBeanDefinition("simpleRemoteEjb");
		assertEquals(SimpleRemoteStatelessSessionProxyFactoryBean.class.getName(), beanDefinition.getBeanClassName());
		assertPropertyValue(beanDefinition, "businessInterface", ITestBean.class.getName());
		assertPropertyValue(beanDefinition, "jndiName", "ejb/MyRemoteBean");
	}

	@Test
	public void testComplexLocalSlsb() throws Exception {
		BeanDefinition beanDefinition = this.beanFactory.getMergedBeanDefinition("complexLocalEjb");
		assertEquals(LocalStatelessSessionProxyFactoryBean.class.getName(), beanDefinition.getBeanClassName());
		assertPropertyValue(beanDefinition, "businessInterface", ITestBean.class.getName());
		assertPropertyValue(beanDefinition, "jndiName", "ejb/MyLocalBean");
		assertPropertyValue(beanDefinition, "cacheHome", "true");
		assertPropertyValue(beanDefinition, "lookupHomeOnStartup", "true");
		assertPropertyValue(beanDefinition, "resourceRef", "true");
		assertPropertyValue(beanDefinition, "jndiEnvironment", "foo=bar");
	}

	@Test
	public void testComplexRemoteSlsb() throws Exception {
		BeanDefinition beanDefinition = this.beanFactory.getMergedBeanDefinition("complexRemoteEjb");
		assertEquals(SimpleRemoteStatelessSessionProxyFactoryBean.class.getName(), beanDefinition.getBeanClassName());
		assertPropertyValue(beanDefinition, "businessInterface", ITestBean.class.getName());
		assertPropertyValue(beanDefinition, "jndiName", "ejb/MyRemoteBean");
		assertPropertyValue(beanDefinition, "cacheHome", "true");
		assertPropertyValue(beanDefinition, "lookupHomeOnStartup", "true");
		assertPropertyValue(beanDefinition, "resourceRef", "true");
		assertPropertyValue(beanDefinition, "jndiEnvironment", "foo=bar");
		assertPropertyValue(beanDefinition, "homeInterface", "org.springframework.beans.ITestBean");
		assertPropertyValue(beanDefinition, "refreshHomeOnConnectFailure", "true");
		assertPropertyValue(beanDefinition, "cacheSessionBean", "true");
	}

	@Test
	public void testLazyInitJndiLookup() throws Exception {
		BeanDefinition definition = this.beanFactory.getMergedBeanDefinition("lazyDataSource");
		assertTrue(definition.isLazyInit());
		definition = this.beanFactory.getMergedBeanDefinition("lazyLocalBean");
		assertTrue(definition.isLazyInit());
		definition = this.beanFactory.getMergedBeanDefinition("lazyRemoteBean");
		assertTrue(definition.isLazyInit());
	}

	private void assertPropertyValue(BeanDefinition beanDefinition, String propertyName, Object expectedValue) {
		assertEquals("Property '" + propertyName + "' incorrect",
				expectedValue, beanDefinition.getPropertyValues().getPropertyValue(propertyName).getValue());
	}

}
