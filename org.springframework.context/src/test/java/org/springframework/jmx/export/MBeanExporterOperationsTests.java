/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export;

import javax.management.InstanceAlreadyExistsException;
import javax.management.ObjectName;
import javax.management.MBeanInfo;
import javax.management.modelmbean.RequiredModelMBean;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.ModelMBeanInfoSupport;

import org.springframework.jmx.AbstractMBeanServerTests;
import org.springframework.jmx.JmxTestBean;
import org.springframework.jmx.export.naming.ObjectNamingStrategy;
import org.springframework.jmx.support.ObjectNameManager;

/**
 * @author Rob Harrop
 * @author Juergen Hoeller
 */
public class MBeanExporterOperationsTests extends AbstractMBeanServerTests {

	public void testRegisterManagedResourceWithUserSuppliedObjectName() throws Exception {
		ObjectName objectName = ObjectNameManager.getInstance("spring:name=Foo");

		JmxTestBean bean = new JmxTestBean();
		bean.setName("Rob Harrop");

		MBeanExporter exporter = new MBeanExporter();
		exporter.setServer(getServer());
		exporter.registerManagedResource(bean, objectName);

		String name = (String) getServer().getAttribute(objectName, "Name");
		assertEquals("Incorrect name on MBean", name, bean.getName());
	}

	public void testRegisterExistingMBeanWithUserSuppliedObjectName() throws Exception {
		ObjectName objectName = ObjectNameManager.getInstance("spring:name=Foo");
		ModelMBeanInfo info = new ModelMBeanInfoSupport("myClass", "myDescription", null, null, null, null);
		RequiredModelMBean bean = new RequiredModelMBean(info);

		MBeanExporter exporter = new MBeanExporter();
		exporter.setServer(getServer());
		exporter.registerManagedResource(bean, objectName);

		MBeanInfo infoFromServer = getServer().getMBeanInfo(objectName);
		assertEquals(info, infoFromServer);
	}

	public void testRegisterManagedResourceWithGeneratedObjectName() throws Exception {
		final ObjectName objectNameTemplate = ObjectNameManager.getInstance("spring:type=Test");

		MBeanExporter exporter = new MBeanExporter();
		exporter.setServer(getServer());
		exporter.setNamingStrategy(new ObjectNamingStrategy() {
			public ObjectName getObjectName(Object managedBean, String beanKey) {
				return objectNameTemplate;
			}
		});

		JmxTestBean bean1 = new JmxTestBean();
		JmxTestBean bean2 = new JmxTestBean();

		ObjectName reg1 = exporter.registerManagedResource(bean1);
		ObjectName reg2 = exporter.registerManagedResource(bean2);

		assertIsRegistered("Bean 1 not registered with MBeanServer", reg1);
		assertIsRegistered("Bean 2 not registered with MBeanServer", reg2);

		assertObjectNameMatchesTemplate(objectNameTemplate, reg1);
		assertObjectNameMatchesTemplate(objectNameTemplate, reg2);
	}

	public void testRegisterManagedResourceWithGeneratedObjectNameWithoutUniqueness() throws Exception {
		final ObjectName objectNameTemplate = ObjectNameManager.getInstance("spring:type=Test");

		MBeanExporter exporter = new MBeanExporter();
		exporter.setServer(getServer());
		exporter.setEnsureUniqueRuntimeObjectNames(false);
		exporter.setNamingStrategy(new ObjectNamingStrategy() {
			public ObjectName getObjectName(Object managedBean, String beanKey) {
				return objectNameTemplate;
			}
		});

		JmxTestBean bean1 = new JmxTestBean();
		JmxTestBean bean2 = new JmxTestBean();

		ObjectName reg1 = exporter.registerManagedResource(bean1);
		assertIsRegistered("Bean 1 not registered with MBeanServer", reg1);

		try {
			exporter.registerManagedResource(bean2);
			fail("Shouldn't be able to register a runtime MBean with a reused ObjectName.");
		}
		catch (MBeanExportException e) {
			assertEquals("Incorrect root cause", InstanceAlreadyExistsException.class, e.getCause().getClass());
		}
	}

	private void assertObjectNameMatchesTemplate(ObjectName objectNameTemplate, ObjectName registeredName) {
		assertEquals("Domain is incorrect", objectNameTemplate.getDomain(), registeredName.getDomain());
	}

}
