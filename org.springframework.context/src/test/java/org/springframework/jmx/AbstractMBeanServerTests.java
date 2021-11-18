/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

import junit.framework.TestCase;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

/**
 * <strong>Note:</strong> the JMX test suite requires the presence of the
 * <code>jmxremote_optional.jar</code> in your classpath. Thus, if you
 * run into the <em>"Unsupported protocol: jmxmp"</em> error, you will
 * need to download the
 * <a href="http://www.oracle.com/technetwork/java/javase/tech/download-jsp-141676.html">JMX Remote API 1.0.1_04 Reference Implementation</a> 
 * from Oracle and extract <code>jmxremote_optional.jar</code> into your
 * classpath, for example in the <code>lib/ext</code> folder of your JVM.
 * See also <a href="https://issuetracker.springsource.com/browse/EBR-349">EBR-349</a>.
 * 
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @author Sam Brannen
 */
public abstract class AbstractMBeanServerTests extends TestCase {

	protected MBeanServer server;

	public final void setUp() throws Exception {
		this.server = MBeanServerFactory.createMBeanServer();
		try {
			onSetUp();
		} catch (Exception ex) {
			releaseServer();
			throw ex;
		}
	}

	protected ConfigurableApplicationContext loadContext(String configLocation) {
		GenericApplicationContext ctx = new GenericApplicationContext();
		new XmlBeanDefinitionReader(ctx).loadBeanDefinitions(configLocation);
		ctx.getDefaultListableBeanFactory().registerSingleton("server", this.server);
		ctx.refresh();
		return ctx;
	}

	protected void tearDown() throws Exception {
		releaseServer();
		onTearDown();
	}

	private void releaseServer() {
		MBeanServerFactory.releaseMBeanServer(getServer());
	}

	protected void onTearDown() throws Exception {
	}

	protected void onSetUp() throws Exception {
	}

	public MBeanServer getServer() {
		return this.server;
	}

	protected void assertIsRegistered(String message, ObjectName objectName) {
		assertTrue(message, getServer().isRegistered(objectName));
	}

	protected void assertIsNotRegistered(String message, ObjectName objectName) {
		assertFalse(message, getServer().isRegistered(objectName));
	}

}
