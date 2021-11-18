/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scheduling.annotation;

import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.scheduling.annotation.AsyncResult;

/**
 * @author Juergen Hoeller
 */
public class AsyncExecutionTests {

	private static String originalThreadName;

	private static int listenerCalled = 0;

	private static int listenerConstructed = 0;


	@Test
	public void asyncMethods() throws Exception {
		originalThreadName = Thread.currentThread().getName();
		GenericApplicationContext context = new GenericApplicationContext();
		context.registerBeanDefinition("asyncTest", new RootBeanDefinition(AsyncMethodBean.class));
		context.registerBeanDefinition("autoProxyCreator", new RootBeanDefinition(DefaultAdvisorAutoProxyCreator.class));
		context.registerBeanDefinition("asyncAdvisor", new RootBeanDefinition(AsyncAnnotationAdvisor.class));
		context.refresh();
		AsyncMethodBean asyncTest = context.getBean("asyncTest", AsyncMethodBean.class);
		asyncTest.doNothing(5);
		asyncTest.doSomething(10);
		Future<String> future = asyncTest.returnSomething(20);
		assertEquals("20", future.get());
	}

	@Test
	public void asyncClass() throws Exception {
		originalThreadName = Thread.currentThread().getName();
		GenericApplicationContext context = new GenericApplicationContext();
		context.registerBeanDefinition("asyncTest", new RootBeanDefinition(AsyncClassBean.class));
		context.registerBeanDefinition("autoProxyCreator", new RootBeanDefinition(DefaultAdvisorAutoProxyCreator.class));
		context.registerBeanDefinition("asyncAdvisor", new RootBeanDefinition(AsyncAnnotationAdvisor.class));
		context.refresh();
		AsyncClassBean asyncTest = context.getBean("asyncTest", AsyncClassBean.class);
		asyncTest.doSomething(10);
		Future<String> future = asyncTest.returnSomething(20);
		assertEquals("20", future.get());
	}

	@Test
	public void asyncInterface() throws Exception {
		originalThreadName = Thread.currentThread().getName();
		GenericApplicationContext context = new GenericApplicationContext();
		context.registerBeanDefinition("asyncTest", new RootBeanDefinition(AsyncInterfaceBean.class));
		context.registerBeanDefinition("autoProxyCreator", new RootBeanDefinition(DefaultAdvisorAutoProxyCreator.class));
		context.registerBeanDefinition("asyncAdvisor", new RootBeanDefinition(AsyncAnnotationAdvisor.class));
		context.refresh();
		AsyncInterface asyncTest = context.getBean("asyncTest", AsyncInterface.class);
		asyncTest.doSomething(10);
		Future<String> future = asyncTest.returnSomething(20);
		assertEquals("20", future.get());
	}

	@Test
	public void asyncMethodsInInterface() throws Exception {
		originalThreadName = Thread.currentThread().getName();
		GenericApplicationContext context = new GenericApplicationContext();
		context.registerBeanDefinition("asyncTest", new RootBeanDefinition(AsyncMethodsInterfaceBean.class));
		context.registerBeanDefinition("autoProxyCreator", new RootBeanDefinition(DefaultAdvisorAutoProxyCreator.class));
		context.registerBeanDefinition("asyncAdvisor", new RootBeanDefinition(AsyncAnnotationAdvisor.class));
		context.refresh();
		AsyncMethodsInterface asyncTest = context.getBean("asyncTest", AsyncMethodsInterface.class);
		asyncTest.doNothing(5);
		asyncTest.doSomething(10);
		Future<String> future = asyncTest.returnSomething(20);
		assertEquals("20", future.get());
	}

	@Test
	public void asyncMethodListener() throws Exception {
		originalThreadName = Thread.currentThread().getName();
		listenerCalled = 0;
		GenericApplicationContext context = new GenericApplicationContext();
		context.registerBeanDefinition("asyncTest", new RootBeanDefinition(AsyncMethodListener.class));
		context.registerBeanDefinition("autoProxyCreator", new RootBeanDefinition(DefaultAdvisorAutoProxyCreator.class));
		context.registerBeanDefinition("asyncAdvisor", new RootBeanDefinition(AsyncAnnotationAdvisor.class));
		context.refresh();
		Thread.sleep(1000);
		assertEquals(1, listenerCalled);
	}

	@Test
	public void asyncClassListener() throws Exception {
		originalThreadName = Thread.currentThread().getName();
		listenerCalled = 0;
		listenerConstructed = 0;
		GenericApplicationContext context = new GenericApplicationContext();
		context.registerBeanDefinition("asyncTest", new RootBeanDefinition(AsyncClassListener.class));
		context.registerBeanDefinition("autoProxyCreator", new RootBeanDefinition(DefaultAdvisorAutoProxyCreator.class));
		context.registerBeanDefinition("asyncAdvisor", new RootBeanDefinition(AsyncAnnotationAdvisor.class));
		context.refresh();
		context.close();
		Thread.sleep(1000);
		assertEquals(2, listenerCalled);
		assertEquals(1, listenerConstructed);
	}

	@Test
	public void asyncPrototypeClassListener() throws Exception {
		originalThreadName = Thread.currentThread().getName();
		listenerCalled = 0;
		listenerConstructed = 0;
		GenericApplicationContext context = new GenericApplicationContext();
		RootBeanDefinition listenerDef = new RootBeanDefinition(AsyncClassListener.class);
		listenerDef.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		context.registerBeanDefinition("asyncTest", listenerDef);
		context.registerBeanDefinition("autoProxyCreator", new RootBeanDefinition(DefaultAdvisorAutoProxyCreator.class));
		context.registerBeanDefinition("asyncAdvisor", new RootBeanDefinition(AsyncAnnotationAdvisor.class));
		context.refresh();
		context.close();
		Thread.sleep(1000);
		assertEquals(2, listenerCalled);
		assertEquals(2, listenerConstructed);
	}


	public static class AsyncMethodBean {

		public void doNothing(int i) {
			assertTrue(Thread.currentThread().getName().equals(originalThreadName));
		}

		@Async
		public void doSomething(int i) {
			System.out.println(Thread.currentThread().getName() + ": " + i);
			assertTrue(!Thread.currentThread().getName().equals(originalThreadName));
		}

		@Async
		public Future<String> returnSomething(int i) {
			assertTrue(!Thread.currentThread().getName().equals(originalThreadName));
			return new AsyncResult<String>(Integer.toString(i));
		}
	}


	@Async
	public static class AsyncClassBean {

		public void doSomething(int i) {
			System.out.println(Thread.currentThread().getName() + ": " + i);
			assertTrue(!Thread.currentThread().getName().equals(originalThreadName));
		}

		public Future<String> returnSomething(int i) {
			assertTrue(!Thread.currentThread().getName().equals(originalThreadName));
			return new AsyncResult<String>(Integer.toString(i));
		}
	}


	@Async
	public interface AsyncInterface {

		void doSomething(int i);

		Future<String> returnSomething(int i);
	}


	public static class AsyncInterfaceBean implements AsyncInterface {

		public void doSomething(int i) {
			System.out.println(Thread.currentThread().getName() + ": " + i);
			assertTrue(!Thread.currentThread().getName().equals(originalThreadName));
		}

		public Future<String> returnSomething(int i) {
			assertTrue(!Thread.currentThread().getName().equals(originalThreadName));
			return new AsyncResult<String>(Integer.toString(i));
		}
	}


	public interface AsyncMethodsInterface {

		void doNothing(int i);

		@Async
		void doSomething(int i);

		@Async
		Future<String> returnSomething(int i);
	}


	public static class AsyncMethodsInterfaceBean implements AsyncMethodsInterface {

		public void doNothing(int i) {
			assertTrue(Thread.currentThread().getName().equals(originalThreadName));
		}

		public void doSomething(int i) {
			System.out.println(Thread.currentThread().getName() + ": " + i);
			assertTrue(!Thread.currentThread().getName().equals(originalThreadName));
		}

		public Future<String> returnSomething(int i) {
			assertTrue(!Thread.currentThread().getName().equals(originalThreadName));
			return new AsyncResult<String>(Integer.toString(i));
		}
	}


	public static class AsyncMethodListener implements ApplicationListener {

		@Async
		public void onApplicationEvent(ApplicationEvent event) {
			listenerCalled++;
			assertTrue(!Thread.currentThread().getName().equals(originalThreadName));
		}
	}


	@Async
	public static class AsyncClassListener implements ApplicationListener {

		public AsyncClassListener() {
			listenerConstructed++;
		}

		public void onApplicationEvent(ApplicationEvent event) {
			listenerCalled++;
			assertTrue(!Thread.currentThread().getName().equals(originalThreadName));
		}
	}

}
