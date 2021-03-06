/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.support;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Mark Fisher
 * @author Chris Beams
 */
public class ApplicationContextLifecycleTests {

	@Test
	public void testBeansStart() {
		AbstractApplicationContext context = new ClassPathXmlApplicationContext("lifecycleTests.xml", getClass());
		context.start();
		LifecycleTestBean bean1 = (LifecycleTestBean) context.getBean("bean1");
		LifecycleTestBean bean2 = (LifecycleTestBean) context.getBean("bean2");
		LifecycleTestBean bean3 = (LifecycleTestBean) context.getBean("bean3");
		LifecycleTestBean bean4 = (LifecycleTestBean) context.getBean("bean4");
		String error = "bean was not started";
		assertTrue(error, bean1.isRunning());
		assertTrue(error, bean2.isRunning());
		assertTrue(error, bean3.isRunning());
		assertTrue(error, bean4.isRunning());
	}

	@Test
	public void testBeansStop() {
		AbstractApplicationContext context = new ClassPathXmlApplicationContext("lifecycleTests.xml", getClass());
		context.start();
		LifecycleTestBean bean1 = (LifecycleTestBean) context.getBean("bean1");
		LifecycleTestBean bean2 = (LifecycleTestBean) context.getBean("bean2");
		LifecycleTestBean bean3 = (LifecycleTestBean) context.getBean("bean3");
		LifecycleTestBean bean4 = (LifecycleTestBean) context.getBean("bean4");
		String startError = "bean was not started";
		assertTrue(startError, bean1.isRunning());
		assertTrue(startError, bean2.isRunning());
		assertTrue(startError, bean3.isRunning());
		assertTrue(startError, bean4.isRunning());
		context.stop();
		String stopError = "bean was not stopped";
		assertFalse(stopError, bean1.isRunning());
		assertFalse(stopError, bean2.isRunning());
		assertFalse(stopError, bean3.isRunning());
		assertFalse(stopError, bean4.isRunning());
	}

	@Test
	public void testStartOrder() {
		AbstractApplicationContext context = new ClassPathXmlApplicationContext("lifecycleTests.xml", getClass());
		context.start();
		LifecycleTestBean bean1 = (LifecycleTestBean) context.getBean("bean1");
		LifecycleTestBean bean2 = (LifecycleTestBean) context.getBean("bean2");
		LifecycleTestBean bean3 = (LifecycleTestBean) context.getBean("bean3");
		LifecycleTestBean bean4 = (LifecycleTestBean) context.getBean("bean4");
		String notStartedError = "bean was not started";
		assertTrue(notStartedError, bean1.getStartOrder() > 0);
		assertTrue(notStartedError, bean2.getStartOrder() > 0);
		assertTrue(notStartedError, bean3.getStartOrder() > 0);
		assertTrue(notStartedError, bean4.getStartOrder() > 0);
		String orderError = "dependent bean must start after the bean it depends on";
		assertTrue(orderError, bean2.getStartOrder() > bean1.getStartOrder());
		assertTrue(orderError, bean3.getStartOrder() > bean2.getStartOrder());
		assertTrue(orderError, bean4.getStartOrder() > bean2.getStartOrder());
	}

	@Test
	public void testStopOrder() {
		AbstractApplicationContext context = new ClassPathXmlApplicationContext("lifecycleTests.xml", getClass());
		context.start();
		context.stop();
		LifecycleTestBean bean1 = (LifecycleTestBean) context.getBean("bean1");
		LifecycleTestBean bean2 = (LifecycleTestBean) context.getBean("bean2");
		LifecycleTestBean bean3 = (LifecycleTestBean) context.getBean("bean3");
		LifecycleTestBean bean4 = (LifecycleTestBean) context.getBean("bean4");
		String notStoppedError = "bean was not stopped";
		assertTrue(notStoppedError, bean1.getStopOrder() > 0);
		assertTrue(notStoppedError, bean2.getStopOrder() > 0);
		assertTrue(notStoppedError, bean3.getStopOrder() > 0);
		assertTrue(notStoppedError, bean4.getStopOrder() > 0);
		String orderError = "dependent bean must stop before the bean it depends on";
		assertTrue(orderError, bean2.getStopOrder() < bean1.getStopOrder());
		assertTrue(orderError, bean3.getStopOrder() < bean2.getStopOrder());
		assertTrue(orderError, bean4.getStopOrder() < bean2.getStopOrder());
	}

}
