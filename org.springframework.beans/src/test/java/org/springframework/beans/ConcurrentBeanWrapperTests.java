/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

/**
 * @author Guillaume Poirier
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 08.03.2004
 */
public final class ConcurrentBeanWrapperTests {

	private final Log logger = LogFactory.getLog(getClass());

	private Set<TestRun> set = Collections.synchronizedSet(new HashSet<TestRun>());

	private Throwable ex = null;

	@Test
	public void testSingleThread() {
		for (int i = 0; i < 100; i++) {
			performSet();
		}
	}
	
	@Test
	public void testConcurrent() {
		for (int i = 0; i < 10; i++) {
			TestRun run = new TestRun(this);
			set.add(run);
			Thread t = new Thread(run);
			t.setDaemon(true);
			t.start();
		}
		logger.info("Thread creation over, " + set.size() + " still active.");
		synchronized (this) {
			while (!set.isEmpty() && ex == null) {
				try {
					wait();
				}
				catch (InterruptedException e) {
					logger.info(e.toString());
				}
				logger.info(set.size() + " threads still active.");
			}
		}
		if (ex != null) {
			fail(ex.getMessage());
		}
	}

	private static void performSet() {
		TestBean bean = new TestBean();

		Properties p = (Properties) System.getProperties().clone();

		assertTrue("The System properties must not be empty", p.size() != 0);

		for (Iterator<?> i = p.entrySet().iterator(); i.hasNext();) {
			i.next();
			if (Math.random() > 0.9) {
				i.remove();
			}
		}

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		try {
			p.store(buffer, null);
		}
		catch (IOException e) {
			// ByteArrayOutputStream does not throw
			// any IOException
		}
		String value = new String(buffer.toByteArray());

		BeanWrapperImpl wrapper = new BeanWrapperImpl(bean);
		wrapper.setPropertyValue("properties", value);
		assertEquals(p, bean.getProperties());
	}


	private static class TestRun implements Runnable {

		private ConcurrentBeanWrapperTests test;

		public TestRun(ConcurrentBeanWrapperTests test) {
			this.test = test;
		}

		public void run() {
			try {
				for (int i = 0; i < 100; i++) {
					performSet();
				}
			}
			catch (Throwable e) {
				test.ex = e;
			}
			finally {
				synchronized (test) {
					test.set.remove(this);
					test.notifyAll();
				}
			}
		}
	}


	private static class TestBean {

		private Properties properties;

		public Properties getProperties() {
			return properties;
		}

		public void setProperties(Properties properties) {
			this.properties = properties;
		}
	}

}
