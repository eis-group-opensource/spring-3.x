/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;

import test.beans.DerivedTestBean;
import test.beans.TestBean;

/**
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 04.07.2006
 */
public final class SharedBeanRegistryTests {

	@Test
	public void testSingletons() {
		DefaultSingletonBeanRegistry beanRegistry = new DefaultSingletonBeanRegistry();

		TestBean tb = new TestBean();
		beanRegistry.registerSingleton("tb", tb);
		assertSame(tb, beanRegistry.getSingleton("tb"));

		TestBean tb2 = (TestBean) beanRegistry.getSingleton("tb2", new ObjectFactory<Object>() {
			public Object getObject() throws BeansException {
				return new TestBean();
			}
		});
		assertSame(tb2, beanRegistry.getSingleton("tb2"));

		assertSame(tb, beanRegistry.getSingleton("tb"));
		assertSame(tb2, beanRegistry.getSingleton("tb2"));
		assertEquals(2, beanRegistry.getSingletonCount());
		assertEquals(2, beanRegistry.getSingletonNames().length);
		assertTrue(Arrays.asList(beanRegistry.getSingletonNames()).contains("tb"));
		assertTrue(Arrays.asList(beanRegistry.getSingletonNames()).contains("tb2"));

		beanRegistry.destroySingletons();
		assertEquals(0, beanRegistry.getSingletonCount());
		assertEquals(0, beanRegistry.getSingletonNames().length);
	}

	@Test
	public void testDisposableBean() {
		DefaultSingletonBeanRegistry beanRegistry = new DefaultSingletonBeanRegistry();

		DerivedTestBean tb = new DerivedTestBean();
		beanRegistry.registerSingleton("tb", tb);
		beanRegistry.registerDisposableBean("tb", tb);
		assertSame(tb, beanRegistry.getSingleton("tb"));

		assertSame(tb, beanRegistry.getSingleton("tb"));
		assertEquals(1, beanRegistry.getSingletonCount());
		assertEquals(1, beanRegistry.getSingletonNames().length);
		assertTrue(Arrays.asList(beanRegistry.getSingletonNames()).contains("tb"));
		assertFalse(tb.wasDestroyed());

		beanRegistry.destroySingletons();
		assertEquals(0, beanRegistry.getSingletonCount());
		assertEquals(0, beanRegistry.getSingletonNames().length);
		assertTrue(tb.wasDestroyed());
	}

}
