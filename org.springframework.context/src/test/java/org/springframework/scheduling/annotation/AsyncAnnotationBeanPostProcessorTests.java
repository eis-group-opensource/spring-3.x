/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scheduling.annotation;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import org.junit.Test;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author Mark Fisher
 * @author Juergen Hoeller
 */
public class AsyncAnnotationBeanPostProcessorTests {

	@Test
	public void proxyCreated() {
		StaticApplicationContext context = new StaticApplicationContext();
		BeanDefinition processorDefinition = new RootBeanDefinition(AsyncAnnotationBeanPostProcessor.class);
		BeanDefinition targetDefinition = new RootBeanDefinition(AsyncAnnotationBeanPostProcessorTests.TestBean.class);
		context.registerBeanDefinition("postProcessor", processorDefinition);
		context.registerBeanDefinition("target", targetDefinition);
		context.refresh();
		Object target = context.getBean("target");
		assertTrue(AopUtils.isAopProxy(target));
		context.close();
	}

	@Test
	public void invokedAsynchronously() {
		StaticApplicationContext context = new StaticApplicationContext();
		BeanDefinition processorDefinition = new RootBeanDefinition(AsyncAnnotationBeanPostProcessor.class);
		BeanDefinition targetDefinition = new RootBeanDefinition(AsyncAnnotationBeanPostProcessorTests.TestBean.class);
		context.registerBeanDefinition("postProcessor", processorDefinition);
		context.registerBeanDefinition("target", targetDefinition);
		context.refresh();
		ITestBean testBean = (ITestBean) context.getBean("target");
		testBean.test();
		Thread mainThread = Thread.currentThread();
		testBean.await(3000);
		Thread asyncThread = testBean.getThread();
		assertNotSame(mainThread, asyncThread);
		context.close();
	}

	@Test
	public void threadNamePrefix() {
		StaticApplicationContext context = new StaticApplicationContext();
		BeanDefinition processorDefinition = new RootBeanDefinition(AsyncAnnotationBeanPostProcessor.class);
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setThreadNamePrefix("testExecutor");
		executor.afterPropertiesSet();
		processorDefinition.getPropertyValues().add("executor", executor);
		BeanDefinition targetDefinition = new RootBeanDefinition(AsyncAnnotationBeanPostProcessorTests.TestBean.class);
		context.registerBeanDefinition("postProcessor", processorDefinition);
		context.registerBeanDefinition("target", targetDefinition);
		context.refresh();
		ITestBean testBean = (ITestBean) context.getBean("target");
		testBean.test();
		testBean.await(3000);
		Thread asyncThread = testBean.getThread();
		assertTrue(asyncThread.getName().startsWith("testExecutor"));
		context.close();
	}

	@Test
	public void configuredThroughNamespace() {
		GenericXmlApplicationContext context = new GenericXmlApplicationContext();
		context.load(new ClassPathResource("taskNamespaceTests.xml", getClass()));
		context.refresh();
		ITestBean testBean = (ITestBean) context.getBean("target");
		testBean.test();
		testBean.await(3000);
		Thread asyncThread = testBean.getThread();
		assertTrue(asyncThread.getName().startsWith("testExecutor"));
		context.close();
	}


	private static interface ITestBean {

		Thread getThread();

		void test();

		void await(long timeout);
	}


	public static class TestBean implements ITestBean {

		private Thread thread;

		private final CountDownLatch latch = new CountDownLatch(1);

		public Thread getThread() {
			return this.thread;
		}

		@Async
		public void test() {
			this.thread = Thread.currentThread();
			this.latch.countDown();
		}

		public void await(long timeout) {
			try {
				this.latch.await(timeout, TimeUnit.MILLISECONDS);
			}
			catch (Exception e) {
				Thread.currentThread().interrupt();
			}
		}
	}

}
