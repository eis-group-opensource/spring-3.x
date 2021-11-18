/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.xml;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import org.junit.Test;
import test.beans.TestBean;

import org.springframework.core.io.ClassPathResource;

/**
 * @author Rob Harrop
 * @author Juergen Hoeller
 */
public class CollectionsWithDefaultTypesTests {

	private final XmlBeanFactory beanFactory;

	public CollectionsWithDefaultTypesTests() {
		this.beanFactory = new XmlBeanFactory(new ClassPathResource("collectionsWithDefaultTypes.xml", getClass()));
	}

	@Test
	public void testListHasDefaultType() throws Exception {
		TestBean bean = (TestBean) this.beanFactory.getBean("testBean");
		for (Object o : bean.getSomeList()) {
			assertEquals("Value type is incorrect", Integer.class, o.getClass());
		}
	}

	@Test
	public void testSetHasDefaultType() throws Exception {
		TestBean bean = (TestBean) this.beanFactory.getBean("testBean");
		for (Object o : bean.getSomeSet()) {
			assertEquals("Value type is incorrect", Integer.class, o.getClass());
		}
	}

	@Test
	public void testMapHasDefaultKeyAndValueType() throws Exception {
		TestBean bean = (TestBean) this.beanFactory.getBean("testBean");
		assertMap(bean.getSomeMap());
	}

	@Test
	public void testMapWithNestedElementsHasDefaultKeyAndValueType() throws Exception {
		TestBean bean = (TestBean) this.beanFactory.getBean("testBean2");
		assertMap(bean.getSomeMap());
	}

	private void assertMap(Map<?,?> map) {
		for (Map.Entry entry : map.entrySet()) {
			assertEquals("Key type is incorrect", Integer.class, entry.getKey().getClass());
			assertEquals("Value type is incorrect", Boolean.class, entry.getValue().getClass());
		}
	}

	@Test
	public void testBuildCollectionFromMixtureOfReferencesAndValues() throws Exception {
		MixedCollectionBean jumble = (MixedCollectionBean) this.beanFactory.getBean("jumble");
		assertTrue("Expected 3 elements, not " + jumble.getJumble().size(),
				jumble.getJumble().size() == 3);
		List l = (List) jumble.getJumble();
		assertTrue(l.get(0).equals("literal"));
		Integer[] array1 = (Integer[]) l.get(1);
		assertTrue(array1[0].equals(new Integer(2)));
		assertTrue(array1[1].equals(new Integer(4)));
		int[] array2 = (int[]) l.get(2);
		assertTrue(array2[0] == 3);
		assertTrue(array2[1] == 5);
	}

}
