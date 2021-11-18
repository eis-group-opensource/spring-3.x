/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.scope;

import static org.junit.Assert.assertSame;
import static test.util.TestResourceUtils.qualifiedResource;

import org.junit.Test;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.Resource;

/**
 * @author Mark Fisher
 * @author Chris Beams
 */
public final class ScopedProxyAutowireTests {
	
	private static final Class<?> CLASS = ScopedProxyAutowireTests.class;
	
	private static final Resource SCOPED_AUTOWIRE_TRUE_CONTEXT = qualifiedResource(CLASS, "scopedAutowireTrue.xml");
	private static final Resource SCOPED_AUTOWIRE_FALSE_CONTEXT = qualifiedResource(CLASS, "scopedAutowireFalse.xml");

	@Test
	public void testScopedProxyInheritsAutowireCandidateFalse() {
		XmlBeanFactory bf = new XmlBeanFactory(SCOPED_AUTOWIRE_FALSE_CONTEXT);
		TestBean autowired = (TestBean) bf.getBean("autowired");
		TestBean unscoped = (TestBean) bf.getBean("unscoped");
		assertSame(unscoped, autowired.getChild());
	}

	@Test
	public void testScopedProxyReplacesAutowireCandidateTrue() {
		XmlBeanFactory bf = new XmlBeanFactory(SCOPED_AUTOWIRE_TRUE_CONTEXT);
		TestBean autowired = (TestBean) bf.getBean("autowired");
		TestBean scoped = (TestBean) bf.getBean("scoped");
		assertSame(scoped, autowired.getChild());
	}


	static class TestBean {

		private TestBean child;

		public void setChild(TestBean child) {
			this.child = child;
		}

		public TestBean getChild() {
			return this.child;
		}
	}

}
