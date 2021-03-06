/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.NotificationListener;
import javax.management.ReflectionException;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jmx.AbstractMBeanServerTests;
import org.springframework.jmx.export.notification.NotificationPublisher;
import org.springframework.jmx.export.notification.NotificationPublisherAware;
import org.springframework.jmx.support.ObjectNameManager;

/**
 * Integration tests for the Spring JMX {@link NotificationPublisher} functionality.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 */
public class NotificationPublisherTests extends AbstractMBeanServerTests {

	private CountingNotificationListener listener = new CountingNotificationListener();

	public void testSimpleBean() throws Exception {
		// start the MBeanExporter
		ConfigurableApplicationContext ctx = loadContext("org/springframework/jmx/export/notificationPublisherTests.xml");
		this.server.addNotificationListener(ObjectNameManager.getInstance("spring:type=Publisher"), listener, null,
				null);

		MyNotificationPublisher publisher = (MyNotificationPublisher) ctx.getBean("publisher");
		assertNotNull("NotificationPublisher should not be null", publisher.getNotificationPublisher());
		publisher.sendNotification();
		assertEquals("Notification not sent", 1, listener.count);
	}

	public void testSimpleBeanRegisteredManually() throws Exception {
		// start the MBeanExporter
		ConfigurableApplicationContext ctx = loadContext("org/springframework/jmx/export/notificationPublisherTests.xml");
		MBeanExporter exporter = (MBeanExporter) ctx.getBean("exporter");
		MyNotificationPublisher publisher = new MyNotificationPublisher();
		exporter.registerManagedResource(publisher, ObjectNameManager.getInstance("spring:type=Publisher2"));
		this.server.addNotificationListener(ObjectNameManager.getInstance("spring:type=Publisher2"), listener, null,
				null);

		assertNotNull("NotificationPublisher should not be null", publisher.getNotificationPublisher());
		publisher.sendNotification();
		assertEquals("Notification not sent", 1, listener.count);
	}

	public void testMBean() throws Exception {
		// start the MBeanExporter
		ConfigurableApplicationContext ctx = loadContext("org/springframework/jmx/export/notificationPublisherTests.xml");
		this.server.addNotificationListener(ObjectNameManager.getInstance("spring:type=PublisherMBean"), listener,
				null, null);

		MyNotificationPublisherMBean publisher = (MyNotificationPublisherMBean) ctx.getBean("publisherMBean");
		publisher.sendNotification();
		assertEquals("Notification not sent", 1, listener.count);
	}

	/*
	public void testStandardMBean() throws Exception {
		// start the MBeanExporter
		ApplicationContext ctx = new ClassPathXmlApplicationContext("org/springframework/jmx/export/notificationPublisherTests.xml");
		this.server.addNotificationListener(ObjectNameManager.getInstance("spring:type=PublisherStandardMBean"), listener, null, null);

		MyNotificationPublisherStandardMBean publisher = (MyNotificationPublisherStandardMBean) ctx.getBean("publisherStandardMBean");
		publisher.sendNotification();
		assertEquals("Notification not sent", 1, listener.count);
	}
	*/

	public void testLazyInit() throws Exception {
		// start the MBeanExporter
		ConfigurableApplicationContext ctx = loadContext("org/springframework/jmx/export/notificationPublisherLazyTests.xml");
		assertFalse("Should not have instantiated the bean yet", ctx.getBeanFactory().containsSingleton("publisher"));

		// need to touch the MBean proxy
		server.getAttribute(ObjectNameManager.getInstance("spring:type=Publisher"), "Name");
		this.server.addNotificationListener(ObjectNameManager.getInstance("spring:type=Publisher"), listener, null,
				null);

		MyNotificationPublisher publisher = (MyNotificationPublisher) ctx.getBean("publisher");
		assertNotNull("NotificationPublisher should not be null", publisher.getNotificationPublisher());
		publisher.sendNotification();
		assertEquals("Notification not sent", 1, listener.count);
	}

	private static class CountingNotificationListener implements NotificationListener {

		private int count;

		private Notification lastNotification;

		public void handleNotification(Notification notification, Object handback) {
			this.lastNotification = notification;
			this.count++;
		}

		@SuppressWarnings("unused")
		public int getCount() {
			return count;
		}

		@SuppressWarnings("unused")
		public Notification getLastNotification() {
			return lastNotification;
		}
	}

	public static class MyNotificationPublisher implements NotificationPublisherAware {

		private NotificationPublisher notificationPublisher;

		public void setNotificationPublisher(NotificationPublisher notificationPublisher) {
			this.notificationPublisher = notificationPublisher;
		}

		public NotificationPublisher getNotificationPublisher() {
			return notificationPublisher;
		}

		public void sendNotification() {
			this.notificationPublisher.sendNotification(new Notification("test", this, 1));
		}

		public String getName() {
			return "Rob Harrop";
		}
	}

	public static class MyNotificationPublisherMBean extends NotificationBroadcasterSupport implements DynamicMBean {

		public Object getAttribute(String attribute) throws AttributeNotFoundException, MBeanException,
				ReflectionException {
			return null;
		}

		public void setAttribute(Attribute attribute) throws AttributeNotFoundException,
				InvalidAttributeValueException, MBeanException, ReflectionException {
		}

		public AttributeList getAttributes(String[] attributes) {
			return null;
		}

		public AttributeList setAttributes(AttributeList attributes) {
			return null;
		}

		public Object invoke(String actionName, Object params[], String signature[]) throws MBeanException,
				ReflectionException {
			return null;
		}

		public MBeanInfo getMBeanInfo() {
			return new MBeanInfo(MyNotificationPublisherMBean.class.getName(), "", new MBeanAttributeInfo[0],
					new MBeanConstructorInfo[0], new MBeanOperationInfo[0], new MBeanNotificationInfo[0]);
		}

		public void sendNotification() {
			sendNotification(new Notification("test", this, 1));
		}
	}

	public static class MyNotificationPublisherStandardMBean extends NotificationBroadcasterSupport implements MyMBean {

		public void sendNotification() {
			sendNotification(new Notification("test", this, 1));
		}
	}

	public interface MyMBean {

		void sendNotification();
	}

}
