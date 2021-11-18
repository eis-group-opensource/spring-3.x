/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scheduling.config;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.springframework.beans.DirectFieldAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.ScheduledMethodRunnable;

/**
 * @author Mark Fisher
 */
@SuppressWarnings("unchecked")
public class ScheduledTasksBeanDefinitionParserTests {

	private ApplicationContext context;

	private ScheduledTaskRegistrar registrar;

	private Object testBean;


	@Before
	public void setup() {
		this.context = new ClassPathXmlApplicationContext(
				"scheduledTasksContext.xml", ScheduledTasksBeanDefinitionParserTests.class);
		this.registrar = (ScheduledTaskRegistrar) this.context.getBeansOfType(
				ScheduledTaskRegistrar.class).values().iterator().next();
		this.testBean = this.context.getBean("testBean");
	}

	@Test
	public void checkScheduler() {
		Object schedulerBean = this.context.getBean("testScheduler");
		Object schedulerRef = new DirectFieldAccessor(this.registrar).getPropertyValue("taskScheduler");
		assertEquals(schedulerBean, schedulerRef);
	}

	@Test
	public void checkTarget() {
		Map<Runnable, Long> tasks = (Map<Runnable, Long>) new DirectFieldAccessor(
				this.registrar).getPropertyValue("fixedRateTasks");
		Runnable runnable = tasks.keySet().iterator().next();
		assertEquals(ScheduledMethodRunnable.class, runnable.getClass());
		Object targetObject = ((ScheduledMethodRunnable) runnable).getTarget();
		Method targetMethod = ((ScheduledMethodRunnable) runnable).getMethod();
		assertEquals(this.testBean, targetObject);
		assertEquals("test", targetMethod.getName());
	}

	@Test
	public void fixedRateTasks() {
		Map<Runnable, Long> tasks = (Map<Runnable, Long>) new DirectFieldAccessor(
				this.registrar).getPropertyValue("fixedRateTasks");
		assertEquals(2, tasks.size());
		Collection<Long> values = tasks.values();
		assertTrue(values.contains(new Long(1000)));
		assertTrue(values.contains(new Long(2000)));
	}

	@Test
	public void fixedDelayTasks() {
		Map<Runnable, Long> tasks = (Map<Runnable, Long>) new DirectFieldAccessor(
				this.registrar).getPropertyValue("fixedDelayTasks");
		assertEquals(1, tasks.size());
		Long value = tasks.values().iterator().next();
		assertEquals(new Long(3000), value);
	}

	@Test
	public void cronTasks() {
		Map<Runnable, String> tasks = (Map<Runnable, String>) new DirectFieldAccessor(
				this.registrar).getPropertyValue("cronTasks");
		assertEquals(1, tasks.size());
		String expression = tasks.values().iterator().next();
		assertEquals("*/4 * 9-17 * * MON-FRI", expression);		
	}

	@Test
	public void triggerTasks() {
		Map<Runnable, Trigger> tasks = (Map<Runnable, Trigger>) new DirectFieldAccessor(
				this.registrar).getPropertyValue("triggerTasks");
		assertEquals(1, tasks.size());
		Trigger trigger = tasks.values().iterator().next();
		assertEquals(TestTrigger.class, trigger.getClass());		
	}


	static class TestBean {

		public void test() {
		}
	}


	static class TestTrigger implements Trigger {

		public Date nextExecutionTime(TriggerContext triggerContext) {
			return null;
		}
	}

}
