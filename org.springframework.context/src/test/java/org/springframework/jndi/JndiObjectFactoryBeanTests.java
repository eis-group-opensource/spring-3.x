/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jndi;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import javax.naming.Context;
import javax.naming.NamingException;

import org.junit.Test;
import org.springframework.beans.DerivedTestBean;
import org.springframework.beans.ITestBean;
import org.springframework.beans.TestBean;
import org.springframework.mock.jndi.ExpectedLookupTemplate;

/**
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public class JndiObjectFactoryBeanTests {

	@Test
	public void testNoJndiName() throws NamingException {
		JndiObjectFactoryBean jof = new JndiObjectFactoryBean();
		try {
			jof.afterPropertiesSet();
			fail("Should have thrown IllegalArgumentException");
		}
		catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testLookupWithFullNameAndResourceRefTrue() throws Exception {
		JndiObjectFactoryBean jof = new JndiObjectFactoryBean();
		Object o = new Object();
		jof.setJndiTemplate(new ExpectedLookupTemplate("java:comp/env/foo", o));
		jof.setJndiName("java:comp/env/foo");
		jof.setResourceRef(true);
		jof.afterPropertiesSet();
		assertTrue(jof.getObject() == o);
	}

	@Test
	public void testLookupWithFullNameAndResourceRefFalse() throws Exception {
		JndiObjectFactoryBean jof = new JndiObjectFactoryBean();
		Object o = new Object();
		jof.setJndiTemplate(new ExpectedLookupTemplate("java:comp/env/foo", o));
		jof.setJndiName("java:comp/env/foo");
		jof.setResourceRef(false);
		jof.afterPropertiesSet();
		assertTrue(jof.getObject() == o);
	}

	@Test
	public void testLookupWithSchemeNameAndResourceRefTrue() throws Exception {
		JndiObjectFactoryBean jof = new JndiObjectFactoryBean();
		Object o = new Object();
		jof.setJndiTemplate(new ExpectedLookupTemplate("java:foo", o));
		jof.setJndiName("java:foo");
		jof.setResourceRef(true);
		jof.afterPropertiesSet();
		assertTrue(jof.getObject() == o);
	}

	@Test
	public void testLookupWithSchemeNameAndResourceRefFalse() throws Exception {
		JndiObjectFactoryBean jof = new JndiObjectFactoryBean();
		Object o = new Object();
		jof.setJndiTemplate(new ExpectedLookupTemplate("java:foo", o));
		jof.setJndiName("java:foo");
		jof.setResourceRef(false);
		jof.afterPropertiesSet();
		assertTrue(jof.getObject() == o);
	}

	@Test
	public void testLookupWithShortNameAndResourceRefTrue() throws Exception {
		JndiObjectFactoryBean jof = new JndiObjectFactoryBean();
		Object o = new Object();
		jof.setJndiTemplate(new ExpectedLookupTemplate("java:comp/env/foo", o));
		jof.setJndiName("foo");
		jof.setResourceRef(true);
		jof.afterPropertiesSet();
		assertTrue(jof.getObject() == o);
	}

	@Test
	public void testLookupWithShortNameAndResourceRefFalse() throws Exception {
		JndiObjectFactoryBean jof = new JndiObjectFactoryBean();
		Object o = new Object();
		jof.setJndiTemplate(new ExpectedLookupTemplate("java:comp/env/foo", o));
		jof.setJndiName("foo");
		jof.setResourceRef(false);
		try {
			jof.afterPropertiesSet();
			fail("Should have thrown NamingException");
		}
		catch (NamingException ex) {
			// expected
		}
	}

	@Test
	public void testLookupWithArbitraryNameAndResourceRefFalse() throws Exception {
		JndiObjectFactoryBean jof = new JndiObjectFactoryBean();
		Object o = new Object();
		jof.setJndiTemplate(new ExpectedLookupTemplate("foo", o));
		jof.setJndiName("foo");
		jof.setResourceRef(false);
		jof.afterPropertiesSet();
		assertTrue(jof.getObject() == o);
	}

	@Test
	public void testLookupWithExpectedTypeAndMatch() throws Exception {
		JndiObjectFactoryBean jof = new JndiObjectFactoryBean();
		String s = "";
		jof.setJndiTemplate(new ExpectedLookupTemplate("foo", s));
		jof.setJndiName("foo");
		jof.setExpectedType(String.class);
		jof.afterPropertiesSet();
		assertTrue(jof.getObject() == s);
	}

	@Test
	public void testLookupWithExpectedTypeAndNoMatch() throws Exception {
		JndiObjectFactoryBean jof = new JndiObjectFactoryBean();
		Object o = new Object();
		jof.setJndiTemplate(new ExpectedLookupTemplate("foo", o));
		jof.setJndiName("foo");
		jof.setExpectedType(String.class);
		try {
			jof.afterPropertiesSet();
			fail("Should have thrown NamingException");
		}
		catch (NamingException ex) {
			assertTrue(ex.getMessage().indexOf("java.lang.String") != -1);
		}
	}

	@Test
	public void testLookupWithDefaultObject() throws Exception {
		JndiObjectFactoryBean jof = new JndiObjectFactoryBean();
		String s = "";
		jof.setJndiTemplate(new ExpectedLookupTemplate("foo", s));
		jof.setJndiName("myFoo");
		jof.setExpectedType(String.class);
		jof.setDefaultObject("myString");
		jof.afterPropertiesSet();
		assertEquals("myString", jof.getObject());
	}

	@Test
	public void testLookupWithDefaultObjectAndExpectedType() throws Exception {
		JndiObjectFactoryBean jof = new JndiObjectFactoryBean();
		String s = "";
		jof.setJndiTemplate(new ExpectedLookupTemplate("foo", s));
		jof.setJndiName("myFoo");
		jof.setExpectedType(String.class);
		jof.setDefaultObject("myString");
		jof.afterPropertiesSet();
		assertEquals("myString", jof.getObject());
	}

	@Test
	public void testLookupWithDefaultObjectAndExpectedTypeNoMatch() throws Exception {
		JndiObjectFactoryBean jof = new JndiObjectFactoryBean();
		String s = "";
		jof.setJndiTemplate(new ExpectedLookupTemplate("foo", s));
		jof.setJndiName("myFoo");
		jof.setExpectedType(String.class);
		jof.setDefaultObject(Boolean.TRUE);
		try {
			jof.afterPropertiesSet();
			fail("Should have thrown IllegalArgumentException");
		}
		catch (IllegalArgumentException ex) {
			// expected
		}
	}

	@Test
	public void testLookupWithProxyInterface() throws Exception {
		JndiObjectFactoryBean jof = new JndiObjectFactoryBean();
		TestBean tb = new TestBean();
		jof.setJndiTemplate(new ExpectedLookupTemplate("foo", tb));
		jof.setJndiName("foo");
		jof.setProxyInterface(ITestBean.class);
		jof.afterPropertiesSet();
		assertTrue(jof.getObject() instanceof ITestBean);
		ITestBean proxy = (ITestBean) jof.getObject();
		assertEquals(0, tb.getAge());
		proxy.setAge(99);
		assertEquals(99, tb.getAge());
	}

	@Test
	public void testLookupWithProxyInterfaceAndDefaultObject() throws Exception {
		JndiObjectFactoryBean jof = new JndiObjectFactoryBean();
		TestBean tb = new TestBean();
		jof.setJndiTemplate(new ExpectedLookupTemplate("foo", tb));
		jof.setJndiName("myFoo");
		jof.setProxyInterface(ITestBean.class);
		jof.setDefaultObject(Boolean.TRUE);
		try {
			jof.afterPropertiesSet();
			fail("Should have thrown IllegalArgumentException");
		}
		catch (IllegalArgumentException ex) {
			// expected
		}
	}

	@Test
	public void testLookupWithProxyInterfaceAndLazyLookup() throws Exception {
		JndiObjectFactoryBean jof = new JndiObjectFactoryBean();
		final TestBean tb = new TestBean();
		jof.setJndiTemplate(new JndiTemplate() {
			public Object lookup(String name) {
				if ("foo".equals(name)) {
					tb.setName("tb");
					return tb;
				}
				return null;
			}
		});
		jof.setJndiName("foo");
		jof.setProxyInterface(ITestBean.class);
		jof.setLookupOnStartup(false);
		jof.afterPropertiesSet();
		assertTrue(jof.getObject() instanceof ITestBean);
		ITestBean proxy = (ITestBean) jof.getObject();
		assertNull(tb.getName());
		assertEquals(0, tb.getAge());
		proxy.setAge(99);
		assertEquals("tb", tb.getName());
		assertEquals(99, tb.getAge());
	}

	@Test
	public void testLookupWithProxyInterfaceWithNotCache() throws Exception {
		JndiObjectFactoryBean jof = new JndiObjectFactoryBean();
		final TestBean tb = new TestBean();
		jof.setJndiTemplate(new JndiTemplate() {
			public Object lookup(String name) {
				if ("foo".equals(name)) {
					tb.setName("tb");
					tb.setAge(tb.getAge() + 1);
					return tb;
				}
				return null;
			}
		});
		jof.setJndiName("foo");
		jof.setProxyInterface(ITestBean.class);
		jof.setCache(false);
		jof.afterPropertiesSet();
		assertTrue(jof.getObject() instanceof ITestBean);
		ITestBean proxy = (ITestBean) jof.getObject();
		assertEquals("tb", tb.getName());
		assertEquals(1, tb.getAge());
		proxy.returnsThis();
		assertEquals(2, tb.getAge());
		proxy.haveBirthday();
		assertEquals(4, tb.getAge());
	}

	@Test
	public void testLookupWithProxyInterfaceWithLazyLookupAndNotCache() throws Exception {
		JndiObjectFactoryBean jof = new JndiObjectFactoryBean();
		final TestBean tb = new TestBean();
		jof.setJndiTemplate(new JndiTemplate() {
			public Object lookup(String name) {
				if ("foo".equals(name)) {
					tb.setName("tb");
					tb.setAge(tb.getAge() + 1);
					return tb;
				}
				return null;
			}
		});
		jof.setJndiName("foo");
		jof.setProxyInterface(ITestBean.class);
		jof.setLookupOnStartup(false);
		jof.setCache(false);
		jof.afterPropertiesSet();
		assertTrue(jof.getObject() instanceof ITestBean);
		ITestBean proxy = (ITestBean) jof.getObject();
		assertNull(tb.getName());
		assertEquals(0, tb.getAge());
		proxy.returnsThis();
		assertEquals("tb", tb.getName());
		assertEquals(1, tb.getAge());
		proxy.returnsThis();
		assertEquals(2, tb.getAge());
		proxy.haveBirthday();
		assertEquals(4, tb.getAge());
	}

	@Test
	public void testLazyLookupWithoutProxyInterface() throws NamingException {
		JndiObjectFactoryBean jof = new JndiObjectFactoryBean();
		jof.setJndiName("foo");
		jof.setLookupOnStartup(false);
		try {
			jof.afterPropertiesSet();
			fail("Should have thrown IllegalStateException");
		}
		catch (IllegalStateException ex) {
			// expected
		}
	}

	@Test
	public void testNotCacheWithoutProxyInterface() throws NamingException {
		JndiObjectFactoryBean jof = new JndiObjectFactoryBean();
		jof.setJndiName("foo");
		jof.setCache(false);
		jof.setLookupOnStartup(false);
		try {
			jof.afterPropertiesSet();
			fail("Should have thrown IllegalStateException");
		}
		catch (IllegalStateException ex) {
			// expected
		}
	}

	@Test
	public void testLookupWithProxyInterfaceAndExpectedTypeAndMatch() throws Exception {
		JndiObjectFactoryBean jof = new JndiObjectFactoryBean();
		TestBean tb = new TestBean();
		jof.setJndiTemplate(new ExpectedLookupTemplate("foo", tb));
		jof.setJndiName("foo");
		jof.setExpectedType(TestBean.class);
		jof.setProxyInterface(ITestBean.class);
		jof.afterPropertiesSet();
		assertTrue(jof.getObject() instanceof ITestBean);
		ITestBean proxy = (ITestBean) jof.getObject();
		assertEquals(0, tb.getAge());
		proxy.setAge(99);
		assertEquals(99, tb.getAge());
	}

	@Test
	public void testLookupWithProxyInterfaceAndExpectedTypeAndNoMatch() {
		JndiObjectFactoryBean jof = new JndiObjectFactoryBean();
		TestBean tb = new TestBean();
		jof.setJndiTemplate(new ExpectedLookupTemplate("foo", tb));
		jof.setJndiName("foo");
		jof.setExpectedType(DerivedTestBean.class);
		jof.setProxyInterface(ITestBean.class);
		try {
			jof.afterPropertiesSet();
			fail("Should have thrown NamingException");
		}
		catch (NamingException ex) {
			assertTrue(ex.getMessage().indexOf("org.springframework.beans.DerivedTestBean") != -1);
		}
	}

	@Test
	public void testLookupWithExposeAccessContext() throws Exception {
		JndiObjectFactoryBean jof = new JndiObjectFactoryBean();
		TestBean tb = new TestBean();
		final Context mockCtx = createMock(Context.class);
		expect(mockCtx.lookup("foo")).andReturn(tb);
		mockCtx.close();
		expectLastCall().times(2);
		replay(mockCtx);
		jof.setJndiTemplate(new JndiTemplate() {
			protected Context createInitialContext() {
				return mockCtx;
			}
		});
		jof.setJndiName("foo");
		jof.setProxyInterface(ITestBean.class);
		jof.setExposeAccessContext(true);
		jof.afterPropertiesSet();
		assertTrue(jof.getObject() instanceof ITestBean);
		ITestBean proxy = (ITestBean) jof.getObject();
		assertEquals(0, tb.getAge());
		proxy.setAge(99);
		assertEquals(99, tb.getAge());
		proxy.equals(proxy);
		proxy.hashCode();
		proxy.toString();
		verify(mockCtx);
	}

}
