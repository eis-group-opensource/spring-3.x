/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.xml;

import junit.framework.Assert;
import junit.framework.TestCase;
import test.beans.TestBean;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Rob Harrop
 * @author Juergen Hoeller
 */
public class AutowireWithExclusionTests extends TestCase {

	public void testByTypeAutowireWithAutoSelfExclusion() throws Exception {
		CountingFactory.reset();
		XmlBeanFactory beanFactory = getBeanFactory("autowire-with-exclusion.xml");
		beanFactory.preInstantiateSingletons();
		TestBean rob = (TestBean) beanFactory.getBean("rob");
		TestBean sally = (TestBean) beanFactory.getBean("sally");
		assertEquals(sally, rob.getSpouse());
		Assert.assertEquals(1, CountingFactory.getFactoryBeanInstanceCount());
	}

	public void testByTypeAutowireWithExclusion() throws Exception {
		CountingFactory.reset();
		XmlBeanFactory beanFactory = getBeanFactory("autowire-with-exclusion.xml");
		beanFactory.preInstantiateSingletons();
		TestBean rob = (TestBean) beanFactory.getBean("rob");
		assertEquals("props1", rob.getSomeProperties().getProperty("name"));
		Assert.assertEquals(1, CountingFactory.getFactoryBeanInstanceCount());
	}

	public void testByTypeAutowireWithExclusionInParentFactory() throws Exception {
		CountingFactory.reset();
		XmlBeanFactory parent = getBeanFactory("autowire-with-exclusion.xml");
		parent.preInstantiateSingletons();
		DefaultListableBeanFactory child = new DefaultListableBeanFactory(parent);
		RootBeanDefinition robDef = new RootBeanDefinition(TestBean.class, RootBeanDefinition.AUTOWIRE_BY_TYPE);
		robDef.getPropertyValues().add("spouse", new RuntimeBeanReference("sally"));
		child.registerBeanDefinition("rob2", robDef);
		TestBean rob = (TestBean) child.getBean("rob2");
		assertEquals("props1", rob.getSomeProperties().getProperty("name"));
		Assert.assertEquals(1, CountingFactory.getFactoryBeanInstanceCount());
	}

	public void testByTypeAutowireWithPrimaryInParentFactory() throws Exception {
		CountingFactory.reset();
		XmlBeanFactory parent = getBeanFactory("autowire-with-exclusion.xml");
		parent.getBeanDefinition("props1").setPrimary(true);
		parent.preInstantiateSingletons();
		DefaultListableBeanFactory child = new DefaultListableBeanFactory(parent);
		RootBeanDefinition robDef = new RootBeanDefinition(TestBean.class, RootBeanDefinition.AUTOWIRE_BY_TYPE);
		robDef.getPropertyValues().add("spouse", new RuntimeBeanReference("sally"));
		child.registerBeanDefinition("rob2", robDef);
		RootBeanDefinition propsDef = new RootBeanDefinition(PropertiesFactoryBean.class);
		propsDef.getPropertyValues().add("properties", "name=props3");
		child.registerBeanDefinition("props3", propsDef);
		TestBean rob = (TestBean) child.getBean("rob2");
		assertEquals("props1", rob.getSomeProperties().getProperty("name"));
		Assert.assertEquals(1, CountingFactory.getFactoryBeanInstanceCount());
	}

	public void testByTypeAutowireWithPrimaryOverridingParentFactory() throws Exception {
		CountingFactory.reset();
		XmlBeanFactory parent = getBeanFactory("autowire-with-exclusion.xml");
		parent.preInstantiateSingletons();
		DefaultListableBeanFactory child = new DefaultListableBeanFactory(parent);
		RootBeanDefinition robDef = new RootBeanDefinition(TestBean.class, RootBeanDefinition.AUTOWIRE_BY_TYPE);
		robDef.getPropertyValues().add("spouse", new RuntimeBeanReference("sally"));
		child.registerBeanDefinition("rob2", robDef);
		RootBeanDefinition propsDef = new RootBeanDefinition(PropertiesFactoryBean.class);
		propsDef.getPropertyValues().add("properties", "name=props3");
		propsDef.setPrimary(true);
		child.registerBeanDefinition("props3", propsDef);
		TestBean rob = (TestBean) child.getBean("rob2");
		assertEquals("props3", rob.getSomeProperties().getProperty("name"));
		Assert.assertEquals(1, CountingFactory.getFactoryBeanInstanceCount());
	}

	public void testByTypeAutowireWithPrimaryInParentAndChild() throws Exception {
		CountingFactory.reset();
		XmlBeanFactory parent = getBeanFactory("autowire-with-exclusion.xml");
		parent.getBeanDefinition("props1").setPrimary(true);
		parent.preInstantiateSingletons();
		DefaultListableBeanFactory child = new DefaultListableBeanFactory(parent);
		RootBeanDefinition robDef = new RootBeanDefinition(TestBean.class, RootBeanDefinition.AUTOWIRE_BY_TYPE);
		robDef.getPropertyValues().add("spouse", new RuntimeBeanReference("sally"));
		child.registerBeanDefinition("rob2", robDef);
		RootBeanDefinition propsDef = new RootBeanDefinition(PropertiesFactoryBean.class);
		propsDef.getPropertyValues().add("properties", "name=props3");
		propsDef.setPrimary(true);
		child.registerBeanDefinition("props3", propsDef);
		TestBean rob = (TestBean) child.getBean("rob2");
		assertEquals("props3", rob.getSomeProperties().getProperty("name"));
		Assert.assertEquals(1, CountingFactory.getFactoryBeanInstanceCount());
	}

	public void testByTypeAutowireWithInclusion() throws Exception {
		CountingFactory.reset();
		XmlBeanFactory beanFactory = getBeanFactory("autowire-with-inclusion.xml");
		beanFactory.preInstantiateSingletons();
		TestBean rob = (TestBean) beanFactory.getBean("rob");
		assertEquals("props1", rob.getSomeProperties().getProperty("name"));
		Assert.assertEquals(1, CountingFactory.getFactoryBeanInstanceCount());
	}

	public void testByTypeAutowireWithSelectiveInclusion() throws Exception {
		CountingFactory.reset();
		XmlBeanFactory beanFactory = getBeanFactory("autowire-with-selective-inclusion.xml");
		beanFactory.preInstantiateSingletons();
		TestBean rob = (TestBean) beanFactory.getBean("rob");
		assertEquals("props1", rob.getSomeProperties().getProperty("name"));
		Assert.assertEquals(1, CountingFactory.getFactoryBeanInstanceCount());
	}

	public void testConstructorAutowireWithAutoSelfExclusion() throws Exception {
		XmlBeanFactory beanFactory = getBeanFactory("autowire-constructor-with-exclusion.xml");
		TestBean rob = (TestBean) beanFactory.getBean("rob");
		TestBean sally = (TestBean) beanFactory.getBean("sally");
		assertEquals(sally, rob.getSpouse());
		TestBean rob2 = (TestBean) beanFactory.getBean("rob");
		assertEquals(rob, rob2);
		assertNotSame(rob, rob2);
		assertEquals(rob.getSpouse(), rob2.getSpouse());
		assertNotSame(rob.getSpouse(), rob2.getSpouse());
	}

	public void testConstructorAutowireWithExclusion() throws Exception {
		XmlBeanFactory beanFactory = getBeanFactory("autowire-constructor-with-exclusion.xml");
		TestBean rob = (TestBean) beanFactory.getBean("rob");
		assertEquals("props1", rob.getSomeProperties().getProperty("name"));
	}

	private XmlBeanFactory getBeanFactory(String configPath) {
		return new XmlBeanFactory(new ClassPathResource(configPath, getClass()));
	}

}
