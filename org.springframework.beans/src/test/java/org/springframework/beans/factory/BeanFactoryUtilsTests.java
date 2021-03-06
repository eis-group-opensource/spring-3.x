/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory;

import static org.junit.Assert.*;
import static test.util.TestResourceUtils.qualifiedResource;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.sf.cglib.proxy.NoOp;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.StaticListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.ObjectUtils;

import test.beans.DummyFactory;
import test.beans.ITestBean;
import test.beans.IndexedTestBean;
import test.beans.TestBean;

/**
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 04.07.2003
 */
public final class BeanFactoryUtilsTests {
	
	private static final Class<?> CLASS = BeanFactoryUtilsTests.class;
	private static final Resource ROOT_CONTEXT = qualifiedResource(CLASS, "root.xml"); 
	private static final Resource MIDDLE_CONTEXT = qualifiedResource(CLASS, "middle.xml"); 
	private static final Resource LEAF_CONTEXT = qualifiedResource(CLASS, "leaf.xml"); 
	private static final Resource DEPENDENT_BEANS_CONTEXT = qualifiedResource(CLASS, "dependentBeans.xml"); 

	private ConfigurableListableBeanFactory listableBeanFactory;

	private ConfigurableListableBeanFactory dependentBeansBF;

	@Before
	public void setUp() {
		// Interesting hierarchical factory to test counts.
		// Slow to read so we cache it.
		XmlBeanFactory grandParent = new XmlBeanFactory(ROOT_CONTEXT);
		XmlBeanFactory parent = new XmlBeanFactory(MIDDLE_CONTEXT, grandParent);
		XmlBeanFactory child = new XmlBeanFactory(LEAF_CONTEXT, parent);
		this.dependentBeansBF = new XmlBeanFactory(DEPENDENT_BEANS_CONTEXT);
		dependentBeansBF.preInstantiateSingletons();
		this.listableBeanFactory = child;
	}

	@Test
	public void testHierarchicalCountBeansWithNonHierarchicalFactory() {
		StaticListableBeanFactory lbf = new StaticListableBeanFactory();
		lbf.addBean("t1", new TestBean());
		lbf.addBean("t2", new TestBean());
		assertTrue(BeanFactoryUtils.countBeansIncludingAncestors(lbf) == 2);
	}

	/**
	 * Check that override doesn't count as two separate beans.
	 */
	@Test
	public void testHierarchicalCountBeansWithOverride() throws Exception {
		// Leaf count
		assertTrue(this.listableBeanFactory.getBeanDefinitionCount() == 1);
		// Count minus duplicate
		assertTrue("Should count 7 beans, not "
				+ BeanFactoryUtils.countBeansIncludingAncestors(this.listableBeanFactory),
			BeanFactoryUtils.countBeansIncludingAncestors(this.listableBeanFactory) == 7);
	}

	@Test
	public void testHierarchicalNamesWithNoMatch() throws Exception {
		List<String> names = Arrays.asList(BeanFactoryUtils.beanNamesForTypeIncludingAncestors(this.listableBeanFactory,
			NoOp.class));
		assertEquals(0, names.size());
	}

	@Test
	public void testHierarchicalNamesWithMatchOnlyInRoot() throws Exception {
		List<String> names = Arrays.asList(BeanFactoryUtils.beanNamesForTypeIncludingAncestors(this.listableBeanFactory,
			IndexedTestBean.class));
		assertEquals(1, names.size());
		assertTrue(names.contains("indexedBean"));
		// Distinguish from default ListableBeanFactory behavior
		assertTrue(listableBeanFactory.getBeanNamesForType(IndexedTestBean.class).length == 0);
	}

	@Test
	public void testGetBeanNamesForTypeWithOverride() throws Exception {
		List<String> names = Arrays.asList(BeanFactoryUtils.beanNamesForTypeIncludingAncestors(this.listableBeanFactory,
			ITestBean.class));
		// includes 2 TestBeans from FactoryBeans (DummyFactory definitions)
		assertEquals(4, names.size());
		assertTrue(names.contains("test"));
		assertTrue(names.contains("test3"));
		assertTrue(names.contains("testFactory1"));
		assertTrue(names.contains("testFactory2"));
	}

	@Test
	public void testNoBeansOfType() {
		StaticListableBeanFactory lbf = new StaticListableBeanFactory();
		lbf.addBean("foo", new Object());
		Map<?, ?> beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(lbf, ITestBean.class, true, false);
		assertTrue(beans.isEmpty());
	}

	@Test
	public void testFindsBeansOfTypeWithStaticFactory() {
		StaticListableBeanFactory lbf = new StaticListableBeanFactory();
		TestBean t1 = new TestBean();
		TestBean t2 = new TestBean();
		DummyFactory t3 = new DummyFactory();
		DummyFactory t4 = new DummyFactory();
		t4.setSingleton(false);
		lbf.addBean("t1", t1);
		lbf.addBean("t2", t2);
		lbf.addBean("t3", t3);
		lbf.addBean("t4", t4);

		Map<?, ?> beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(lbf, ITestBean.class, true, false);
		assertEquals(2, beans.size());
		assertEquals(t1, beans.get("t1"));
		assertEquals(t2, beans.get("t2"));

		beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(lbf, ITestBean.class, false, true);
		assertEquals(3, beans.size());
		assertEquals(t1, beans.get("t1"));
		assertEquals(t2, beans.get("t2"));
		assertEquals(t3.getObject(), beans.get("t3"));

		beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(lbf, ITestBean.class, true, true);
		assertEquals(4, beans.size());
		assertEquals(t1, beans.get("t1"));
		assertEquals(t2, beans.get("t2"));
		assertEquals(t3.getObject(), beans.get("t3"));
		assertTrue(beans.get("t4") instanceof TestBean);

		beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(lbf, DummyFactory.class, true, true);
		assertEquals(2, beans.size());
		assertEquals(t3, beans.get("&t3"));
		assertEquals(t4, beans.get("&t4"));

		beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(lbf, FactoryBean.class, true, true);
		assertEquals(2, beans.size());
		assertEquals(t3, beans.get("&t3"));
		assertEquals(t4, beans.get("&t4"));
	}

	@Test
	public void testFindsBeansOfTypeWithDefaultFactory() {
		Object test3 = this.listableBeanFactory.getBean("test3");
		Object test = this.listableBeanFactory.getBean("test");

		TestBean t1 = new TestBean();
		TestBean t2 = new TestBean();
		DummyFactory t3 = new DummyFactory();
		DummyFactory t4 = new DummyFactory();
		t4.setSingleton(false);
		this.listableBeanFactory.registerSingleton("t1", t1);
		this.listableBeanFactory.registerSingleton("t2", t2);
		this.listableBeanFactory.registerSingleton("t3", t3);
		this.listableBeanFactory.registerSingleton("t4", t4);

		Map<?, ?> beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(this.listableBeanFactory, ITestBean.class, true,
			false);
		assertEquals(6, beans.size());
		assertEquals(test3, beans.get("test3"));
		assertEquals(test, beans.get("test"));
		assertEquals(t1, beans.get("t1"));
		assertEquals(t2, beans.get("t2"));
		assertEquals(t3.getObject(), beans.get("t3"));
		assertTrue(beans.get("t4") instanceof TestBean);
		// t3 and t4 are found here as of Spring 2.0, since they are
		// pre-registered
		// singleton instances, while testFactory1 and testFactory are *not*
		// found
		// because they are FactoryBean definitions that haven't been
		// initialized yet.

		beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(this.listableBeanFactory, ITestBean.class, false, true);
		Object testFactory1 = this.listableBeanFactory.getBean("testFactory1");
		assertEquals(5, beans.size());
		assertEquals(test, beans.get("test"));
		assertEquals(testFactory1, beans.get("testFactory1"));
		assertEquals(t1, beans.get("t1"));
		assertEquals(t2, beans.get("t2"));
		assertEquals(t3.getObject(), beans.get("t3"));

		beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(this.listableBeanFactory, ITestBean.class, true, true);
		assertEquals(8, beans.size());
		assertEquals(test3, beans.get("test3"));
		assertEquals(test, beans.get("test"));
		assertEquals(testFactory1, beans.get("testFactory1"));
		assertTrue(beans.get("testFactory2") instanceof TestBean);
		assertEquals(t1, beans.get("t1"));
		assertEquals(t2, beans.get("t2"));
		assertEquals(t3.getObject(), beans.get("t3"));
		assertTrue(beans.get("t4") instanceof TestBean);

		beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(this.listableBeanFactory, DummyFactory.class, true, true);
		assertEquals(4, beans.size());
		assertEquals(this.listableBeanFactory.getBean("&testFactory1"), beans.get("&testFactory1"));
		assertEquals(this.listableBeanFactory.getBean("&testFactory2"), beans.get("&testFactory2"));
		assertEquals(t3, beans.get("&t3"));
		assertEquals(t4, beans.get("&t4"));

		beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(this.listableBeanFactory, FactoryBean.class, true, true);
		assertEquals(4, beans.size());
		assertEquals(this.listableBeanFactory.getBean("&testFactory1"), beans.get("&testFactory1"));
		assertEquals(this.listableBeanFactory.getBean("&testFactory2"), beans.get("&testFactory2"));
		assertEquals(t3, beans.get("&t3"));
		assertEquals(t4, beans.get("&t4"));
	}

	@Test
	public void testHierarchicalResolutionWithOverride() throws Exception {
		Object test3 = this.listableBeanFactory.getBean("test3");
		Object test = this.listableBeanFactory.getBean("test");

		Map<?, ?> beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(this.listableBeanFactory, ITestBean.class, true,
			false);
		assertEquals(2, beans.size());
		assertEquals(test3, beans.get("test3"));
		assertEquals(test, beans.get("test"));

		beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(this.listableBeanFactory, ITestBean.class, false, false);
		assertEquals(1, beans.size());
		assertEquals(test, beans.get("test"));

		beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(this.listableBeanFactory, ITestBean.class, false, true);
		Object testFactory1 = this.listableBeanFactory.getBean("testFactory1");
		assertEquals(2, beans.size());
		assertEquals(test, beans.get("test"));
		assertEquals(testFactory1, beans.get("testFactory1"));

		beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(this.listableBeanFactory, ITestBean.class, true, true);
		assertEquals(4, beans.size());
		assertEquals(test3, beans.get("test3"));
		assertEquals(test, beans.get("test"));
		assertEquals(testFactory1, beans.get("testFactory1"));
		assertTrue(beans.get("testFactory2") instanceof TestBean);

		beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(this.listableBeanFactory, DummyFactory.class, true, true);
		assertEquals(2, beans.size());
		assertEquals(this.listableBeanFactory.getBean("&testFactory1"), beans.get("&testFactory1"));
		assertEquals(this.listableBeanFactory.getBean("&testFactory2"), beans.get("&testFactory2"));

		beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(this.listableBeanFactory, FactoryBean.class, true, true);
		assertEquals(2, beans.size());
		assertEquals(this.listableBeanFactory.getBean("&testFactory1"), beans.get("&testFactory1"));
		assertEquals(this.listableBeanFactory.getBean("&testFactory2"), beans.get("&testFactory2"));
	}

	@Test
	public void testADependencies() {
		String[] deps = this.dependentBeansBF.getDependentBeans("a");
		assertTrue(ObjectUtils.isEmpty(deps));
	}

	@Test
	public void testBDependencies() {
		String[] deps = this.dependentBeansBF.getDependentBeans("b");
		assertTrue(Arrays.equals(new String[] { "c" }, deps));
	}

	@Test
	public void testCDependencies() {
		String[] deps = this.dependentBeansBF.getDependentBeans("c");
		assertTrue(Arrays.equals(new String[] { "int", "long" }, deps));
	}

	@Test
	public void testIntDependencies() {
		String[] deps = this.dependentBeansBF.getDependentBeans("int");
		assertTrue(Arrays.equals(new String[] { "buffer" }, deps));
	}

}
