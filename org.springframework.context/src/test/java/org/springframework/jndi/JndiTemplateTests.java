/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jndi;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import javax.naming.Context;
import javax.naming.NameNotFoundException;

import org.junit.Test;

/**
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 08.07.2003
 */
public class JndiTemplateTests {

	@Test
	public void testLookupSucceeds() throws Exception {
		Object o = new Object();
		String name = "foo";
		final Context context = createMock(Context.class);
		expect(context.lookup(name)).andReturn(o);
		context.close();
		replay(context);

		JndiTemplate jt = new JndiTemplate() {
			protected Context createInitialContext() {
				return context;
			}
		};

		Object o2 = jt.lookup(name);
		assertEquals(o, o2);
		verify(context);
	}

	@Test
	public void testLookupFails() throws Exception {
		NameNotFoundException ne = new NameNotFoundException();
		String name = "foo";
		final Context context = createMock(Context.class);
		expect(context.lookup(name)).andThrow(ne);
		context.close();
		replay(context);

		JndiTemplate jt = new JndiTemplate() {
			protected Context createInitialContext() {
				return context;
			}
		};

		try {
			jt.lookup(name);
			fail("Should have thrown NamingException");
		}
		catch (NameNotFoundException ex) {
			// Ok
		}
		verify(context);
	}

	@Test
	public void testLookupReturnsNull() throws Exception {
		String name = "foo";
		final Context context = createMock(Context.class);
		expect(context.lookup(name)).andReturn(null);
		context.close();
		replay(context);

		JndiTemplate jt = new JndiTemplate() {
			protected Context createInitialContext() {
				return context;
			}
		};

		try {
			jt.lookup(name);
			fail("Should have thrown NamingException");
		}
		catch (NameNotFoundException ex) {
			// Ok
		}
		verify(context);
	}

	@Test
	public void testLookupFailsWithTypeMismatch() throws Exception {
		Object o = new Object();
		String name = "foo";
		final Context context = createMock(Context.class);
		expect(context.lookup(name)).andReturn(o);
		context.close();
		replay(context);

		JndiTemplate jt = new JndiTemplate() {
			protected Context createInitialContext() {
				return context;
			}
		};

		try {
			jt.lookup(name, String.class);
			fail("Should have thrown TypeMismatchNamingException");
		}
		catch (TypeMismatchNamingException ex) {
			// Ok
		}
		verify(context);
	}

	@Test
	public void testBind() throws Exception {
		Object o = new Object();
		String name = "foo";
		final Context context = createMock(Context.class);
		context.bind(name, o);
		context.close();
		replay(context);

		JndiTemplate jt = new JndiTemplate() {
			protected Context createInitialContext() {
				return context;
			}
		};

		jt.bind(name, o);
		verify(context);
	}

	@Test
	public void testRebind() throws Exception {
		Object o = new Object();
		String name = "foo";
		final Context context = createMock(Context.class);
		context.rebind(name, o);
		context.close();
		replay(context);

		JndiTemplate jt = new JndiTemplate() {
			protected Context createInitialContext() {
				return context;
			}
		};

		jt.rebind(name, o);
		verify(context);
	}

	@Test
	public void testUnbind() throws Exception {
		String name = "something";
		final Context context = createMock(Context.class);
		context.unbind(name);
		context.close();
		replay(context);

		JndiTemplate jt = new JndiTemplate() {
			protected Context createInitialContext() {
				return context;
			}
		};

		jt.unbind(name);
		verify(context);
	}

}
