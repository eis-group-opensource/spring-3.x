/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.support;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.springframework.beans.TestBean;
import org.springframework.context.ApplicationContext;

/**
 * @author Arjen Poutsma
 */
public class SimpleThreadScopeTest {

	private ApplicationContext applicationContext;

	@Before
	public void setUp() {
		applicationContext = new ClassPathXmlApplicationContext("simpleThreadScopeTests.xml", getClass());
	}

	@Test
	public void getFromScope() throws Exception {
		String name = "threadScopedObject";
		TestBean bean = (TestBean) this.applicationContext.getBean(name);
		assertNotNull(bean);
		assertSame(bean, this.applicationContext.getBean(name));
		TestBean bean2 = (TestBean) this.applicationContext.getBean(name);
		assertSame(bean, bean2);
	}

	@Test
	public void getMultipleInstances() throws Exception {
		final TestBean[] beans = new TestBean[2];
		Thread thread1 = new Thread(new Runnable() {
			public void run() {
				beans[0] = applicationContext.getBean("threadScopedObject", TestBean.class);
			}
		});
		Thread thread2 = new Thread(new Runnable() {
			public void run() {
				beans[1] = applicationContext.getBean("threadScopedObject", TestBean.class);
			}
		});
		thread1.start();
		thread2.start();

		Thread.sleep(200);

		assertNotNull(beans[0]);
		assertNotNull(beans[1]);

		assertNotSame(beans[0], beans[1]);
	}

}
