/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jndi;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Logger;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.sql.DataSource;

import org.junit.Test;

import org.springframework.mock.jndi.SimpleNamingContext;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

import static org.junit.Assert.*;


/**
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public class SimpleNamingContextTests {

	@Test
	public void testNamingContextBuilder() throws NamingException {
		SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
		InitialContextFactory factory = builder.createInitialContextFactory(null);

		DataSource ds = new StubDataSource();
		builder.bind("java:comp/env/jdbc/myds", ds);
		Object obj = new Object();
		builder.bind("myobject", obj);

		Context context1 = factory.getInitialContext(null);
		assertTrue("Correct DataSource registered", context1.lookup("java:comp/env/jdbc/myds") == ds);
		assertTrue("Correct Object registered", context1.lookup("myobject") == obj);

		Hashtable<String, String> env2 = new Hashtable<String, String>();
		env2.put("key1", "value1");
		Context context2 = factory.getInitialContext(env2);
		assertTrue("Correct DataSource registered", context2.lookup("java:comp/env/jdbc/myds") == ds);
		assertTrue("Correct Object registered", context2.lookup("myobject") == obj);
		assertTrue("Correct environment", context2.getEnvironment() != env2);
		assertTrue("Correct key1", "value1".equals(context2.getEnvironment().get("key1")));

		Integer i = new Integer(0);
		context1.rebind("myinteger", i);
		String s = "";
		context2.bind("mystring", s);

		Context context3 = (Context) context2.lookup("");
		context3.rename("java:comp/env/jdbc/myds", "jdbc/myds");
		context3.unbind("myobject");

		assertTrue("Correct environment", context3.getEnvironment() != context2.getEnvironment());
		context3.addToEnvironment("key2", "value2");
		assertTrue("key2 added", "value2".equals(context3.getEnvironment().get("key2")));
		context3.removeFromEnvironment("key1");
		assertTrue("key1 removed", context3.getEnvironment().get("key1") == null);

		assertTrue("Correct DataSource registered", context1.lookup("jdbc/myds") == ds);
		try {
			context1.lookup("myobject");
			fail("Should have thrown NameNotFoundException");
		}
		catch (NameNotFoundException ex) {
			// expected
		}
		assertTrue("Correct Integer registered", context1.lookup("myinteger") == i);
		assertTrue("Correct String registered", context1.lookup("mystring") == s);

		assertTrue("Correct DataSource registered", context2.lookup("jdbc/myds") == ds);
		try {
			context2.lookup("myobject");
			fail("Should have thrown NameNotFoundException");
		}
		catch (NameNotFoundException ex) {
			// expected
		}
		assertTrue("Correct Integer registered", context2.lookup("myinteger") == i);
		assertTrue("Correct String registered", context2.lookup("mystring") == s);

		assertTrue("Correct DataSource registered", context3.lookup("jdbc/myds") == ds);
		try {
			context3.lookup("myobject");
			fail("Should have thrown NameNotFoundException");
		}
		catch (NameNotFoundException ex) {
			// expected
		}
		assertTrue("Correct Integer registered", context3.lookup("myinteger") == i);
		assertTrue("Correct String registered", context3.lookup("mystring") == s);

		Map<String, Binding> bindingMap = new HashMap<String, Binding>();
		NamingEnumeration<?> bindingEnum = context3.listBindings("");
		while (bindingEnum.hasMoreElements()) {
			Binding binding = (Binding) bindingEnum.nextElement();
			bindingMap.put(binding.getName(), binding);
		}
		assertTrue("Correct jdbc subcontext", bindingMap.get("jdbc").getObject() instanceof Context);
		assertTrue("Correct jdbc subcontext", SimpleNamingContext.class.getName().equals(bindingMap.get("jdbc").getClassName()));

		Context jdbcContext = (Context) context3.lookup("jdbc");
		jdbcContext.bind("mydsX", ds);
		Map<String, Binding> subBindingMap = new HashMap<String, Binding>();
		NamingEnumeration<?> subBindingEnum = jdbcContext.listBindings("");
		while (subBindingEnum.hasMoreElements()) {
			Binding binding = (Binding) subBindingEnum.nextElement();
			subBindingMap.put(binding.getName(), binding);
		}

		assertTrue("Correct DataSource registered", ds.equals(subBindingMap.get("myds").getObject()));
		assertTrue("Correct DataSource registered", StubDataSource.class.getName().equals(subBindingMap.get("myds").getClassName()));
		assertTrue("Correct DataSource registered", ds.equals(subBindingMap.get("mydsX").getObject()));
		assertTrue("Correct DataSource registered", StubDataSource.class.getName().equals(subBindingMap.get("mydsX").getClassName()));
		assertTrue("Correct Integer registered", i.equals(bindingMap.get("myinteger").getObject()));
		assertTrue("Correct Integer registered", Integer.class.getName().equals(bindingMap.get("myinteger").getClassName()));
		assertTrue("Correct String registered", s.equals(bindingMap.get("mystring").getObject()));
		assertTrue("Correct String registered", String.class.getName().equals(bindingMap.get("mystring").getClassName()));

		context1.createSubcontext("jdbc").bind("sub/subds", ds);

		Map<String, String> pairMap = new HashMap<String, String>();
		NamingEnumeration<?> pairEnum = context2.list("jdbc");
		while (pairEnum.hasMore()) {
			NameClassPair pair = (NameClassPair) pairEnum.next();
			pairMap.put(pair.getName(), pair.getClassName());
		}
		assertTrue("Correct sub subcontext", SimpleNamingContext.class.getName().equals(pairMap.get("sub")));

		Context subContext = (Context) context2.lookup("jdbc/sub");
		Map<String, String> subPairMap = new HashMap<String, String>();
		NamingEnumeration<?> subPairEnum = subContext.list("");
		while (subPairEnum.hasMoreElements()) {
			NameClassPair pair = (NameClassPair) subPairEnum.next();
			subPairMap.put(pair.getName(), pair.getClassName());
		}

		assertTrue("Correct DataSource registered", StubDataSource.class.getName().equals(subPairMap.get("subds")));
		assertTrue("Correct DataSource registered", StubDataSource.class.getName().equals(pairMap.get("myds")));
		assertTrue("Correct DataSource registered", StubDataSource.class.getName().equals(pairMap.get("mydsX")));

		pairMap.clear();
		pairEnum = context1.list("jdbc/");
		while (pairEnum.hasMore()) {
			NameClassPair pair = (NameClassPair) pairEnum.next();
			pairMap.put(pair.getName(), pair.getClassName());
		}
		assertTrue("Correct DataSource registered", StubDataSource.class.getName().equals(pairMap.get("myds")));
		assertTrue("Correct DataSource registered", StubDataSource.class.getName().equals(pairMap.get("mydsX")));
	}

	/**
	 * Demonstrates how emptyActivatedContextBuilder() method can be
	 * used repeatedly, and how it affects creating a new InitialContext()
	 */
	@Test
	public void testCreateInitialContext() throws Exception {
		SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
		String name = "foo";
		Object o = new Object();
		builder.bind(name, o);
		// Check it affects JNDI
		Context ctx = new InitialContext();
		assertTrue(ctx.lookup(name) == o);
		// Check it returns mutable contexts
		ctx.unbind(name);
		try {
			ctx = new InitialContext();
			ctx.lookup(name);
			fail("Should have thrown NamingException");
		}
		catch (NamingException ex) {
			// expected
		}

		// Check the same call will work again, but the context is empty
		builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
		try {
			ctx = new InitialContext();
			ctx.lookup(name);
			fail("Should have thrown NamingException");
		}
		catch (NamingException ex) {
			// expected
		}
		Object o2 = new Object();
		builder.bind(name, o2);
		assertEquals(ctx.lookup(name), o2);
	}

}

class StubDataSource implements DataSource {

	public Connection getConnection() throws SQLException {
		return null;
	}

	public Connection getConnection(String username, String password) throws SQLException {
		return null;
	}

	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	public void setLogWriter(PrintWriter arg0) throws SQLException {

	}

	public void setLoginTimeout(int arg0) throws SQLException {

	}

	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		return false;
	}

	public <T> T unwrap(Class<T> arg0) throws SQLException {
		return null;
	}

	public Logger getParentLogger() {
		return null;
	}
}
