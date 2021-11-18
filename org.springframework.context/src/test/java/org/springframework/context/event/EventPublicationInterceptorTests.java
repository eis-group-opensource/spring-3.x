/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.event;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.ITestBean;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.TestBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.TestListener;
import org.springframework.context.support.StaticApplicationContext;

/**
 * @author Dmitriy Kopylenko
 * @author Juergen Hoeller
 * @author Rick Evans
 */
public class EventPublicationInterceptorTests {
	
	private ApplicationEventPublisher publisher;


	@Before
	public void setUp() {
		publisher = createMock(ApplicationEventPublisher.class);
		replay(publisher);
	}
	
	@After
	public void tearDown() {
		verify(publisher);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testWithNoApplicationEventClassSupplied() throws Exception {
		EventPublicationInterceptor interceptor = new EventPublicationInterceptor();
		interceptor.setApplicationEventPublisher(publisher);
		interceptor.afterPropertiesSet();
	}

	@Test(expected=IllegalArgumentException.class)
	public void testWithNonApplicationEventClassSupplied() throws Exception {
		EventPublicationInterceptor interceptor = new EventPublicationInterceptor();
		interceptor.setApplicationEventPublisher(publisher);
		interceptor.setApplicationEventClass(getClass());
		interceptor.afterPropertiesSet();
	}

	@Test(expected=IllegalArgumentException.class)
	public void testWithAbstractStraightApplicationEventClassSupplied() throws Exception {
		EventPublicationInterceptor interceptor = new EventPublicationInterceptor();
		interceptor.setApplicationEventPublisher(publisher);
		interceptor.setApplicationEventClass(ApplicationEvent.class);
		interceptor.afterPropertiesSet();
	}

	@Test(expected=IllegalArgumentException.class)
	public void testWithApplicationEventClassThatDoesntExposeAValidCtor() throws Exception {
		EventPublicationInterceptor interceptor = new EventPublicationInterceptor();
		interceptor.setApplicationEventPublisher(publisher);
		interceptor.setApplicationEventClass(TestEventWithNoValidOneArgObjectCtor.class);
		interceptor.afterPropertiesSet();
	}

	@Test
	public void testExpectedBehavior() throws Exception {
		TestBean target = new TestBean();
		final TestListener listener = new TestListener();

		class TestContext extends StaticApplicationContext {
			protected void onRefresh() throws BeansException {
				addListener(listener);
			}
		}

		StaticApplicationContext ctx = new TestContext();
		MutablePropertyValues pvs = new MutablePropertyValues();
		pvs.add("applicationEventClass", TestEvent.class.getName());
		// should automatically receive applicationEventPublisher reference
		ctx.registerSingleton("publisher", EventPublicationInterceptor.class, pvs);
		ctx.registerSingleton("otherListener", FactoryBeanTestListener.class);
		ctx.refresh();

		EventPublicationInterceptor interceptor =
				(EventPublicationInterceptor) ctx.getBean("publisher");
		ProxyFactory factory = new ProxyFactory(target);
		factory.addAdvice(0, interceptor);

		ITestBean testBean = (ITestBean) factory.getProxy();

		// invoke any method on the advised proxy to see if the interceptor has been invoked
		testBean.getAge();

		// two events: ContextRefreshedEvent and TestEvent
		assertTrue("Interceptor must have published 2 events", listener.getEventCount() == 2);
		TestListener otherListener = (TestListener) ctx.getBean("&otherListener");
		assertTrue("Interceptor must have published 2 events", otherListener.getEventCount() == 2);
	}


	public static class TestEvent extends ApplicationEvent {

		public TestEvent(Object source) {
			super(source);
		}
	}


	public static final class TestEventWithNoValidOneArgObjectCtor extends ApplicationEvent {

		public TestEventWithNoValidOneArgObjectCtor() {
			super("");
		}
	}


	public static class FactoryBeanTestListener extends TestListener implements FactoryBean {

		public Object getObject() throws Exception {
			return "test";
		}

		public Class getObjectType() {
			return String.class;
		}

		public boolean isSingleton() {
			return true;
		}
	}

}
