/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.access;

import java.net.BindException;
import java.net.MalformedURLException;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import org.junit.Ignore;

/**
 * @author Rob Harrop
 */
// TODO [SPR-8089] Clean up ignored JMX tests.
//
// @Ignore should have no effect for JUnit 3.8 tests; however, it appears
// that tests on the CI server -- as well as those in Eclipse -- do in
// fact get ignored. So we leave @Ignore here so that developers can
// easily search for ignored tests.
@Ignore("Requires jmxremote_optional.jar; see comments in AbstractMBeanServerTests for details.")
public class RemoteMBeanClientInterceptorTestsIgnore extends MBeanClientInterceptorTests {

	private static final String SERVICE_URL = "service:jmx:jmxmp://localhost:9876";

	private JMXConnectorServer connectorServer;

	private JMXConnector connector;

	public void onSetUp() throws Exception {
		super.onSetUp();
		this.connectorServer = JMXConnectorServerFactory.newJMXConnectorServer(getServiceUrl(), null, getServer());
		try {
			this.connectorServer.start();
		} catch (BindException ex) {
			// skipping tests, server already running at this port
			runTests = false;
		}
	}

	private JMXServiceURL getServiceUrl() throws MalformedURLException {
		return new JMXServiceURL(SERVICE_URL);
	}

	protected MBeanServerConnection getServerConnection() throws Exception {
		this.connector = JMXConnectorFactory.connect(getServiceUrl());
		return this.connector.getMBeanServerConnection();
	}

	public void tearDown() throws Exception {
		if (this.connector != null) {
			this.connector.close();
		}
		this.connectorServer.stop();
		super.tearDown();
	}

}
