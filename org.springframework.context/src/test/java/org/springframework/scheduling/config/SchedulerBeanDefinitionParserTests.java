/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scheduling.config;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import org.springframework.beans.DirectFieldAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @author Mark Fisher
 */
public class SchedulerBeanDefinitionParserTests {

	private ApplicationContext context;


	@Before
	public void setup() {
		this.context = new ClassPathXmlApplicationContext(
				"schedulerContext.xml", SchedulerBeanDefinitionParserTests.class);
	}

	@Test
	public void defaultScheduler() {
		ThreadPoolTaskScheduler scheduler = (ThreadPoolTaskScheduler) this.context.getBean("defaultScheduler");
		Integer size = (Integer) new DirectFieldAccessor(scheduler).getPropertyValue("poolSize");
		assertEquals(new Integer(1), size);
	}

	@Test
	public void customScheduler() {
		ThreadPoolTaskScheduler scheduler = (ThreadPoolTaskScheduler) this.context.getBean("customScheduler");
		Integer size = (Integer) new DirectFieldAccessor(scheduler).getPropertyValue("poolSize");
		assertEquals(new Integer(42), size);
	}

	@Test
	public void threadNamePrefix() {
		ThreadPoolTaskScheduler scheduler = (ThreadPoolTaskScheduler) this.context.getBean("customScheduler");
		assertEquals("customScheduler-", scheduler.getThreadNamePrefix());
	}

}
