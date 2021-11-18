/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.support;

import java.util.List;
import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;

import junit.framework.TestCase;

/**
 * @author Rob Harrop
 * @author Juergen Hoeller
 */
public class MBeanServerFactoryBeanTests extends TestCase {

	public void testGetObject() throws Exception {
		MBeanServerFactoryBean bean = new MBeanServerFactoryBean();
		bean.afterPropertiesSet();
		try {
			MBeanServer server = bean.getObject();
			assertNotNull("The MBeanServer should not be null", server);
		}
		finally {
			bean.destroy();
		}
	}

	public void testDefaultDomain() throws Exception {
		MBeanServerFactoryBean bean = new MBeanServerFactoryBean();
		bean.setDefaultDomain("foo");
		bean.afterPropertiesSet();
		try {
			MBeanServer server = bean.getObject();
			assertEquals("The default domain should be foo", "foo", server.getDefaultDomain());
		}
		finally {
			bean.destroy();
		}
	}

	public void testWithLocateExistingAndExistingServer() {
		MBeanServer server = MBeanServerFactory.createMBeanServer();
		try {
			MBeanServerFactoryBean bean = new MBeanServerFactoryBean();
			bean.setLocateExistingServerIfPossible(true);
			bean.afterPropertiesSet();
			try {
				MBeanServer otherServer = bean.getObject();
				assertSame("Existing MBeanServer not located", server, otherServer);
			}
			finally {
				bean.destroy();
			}
		}
		finally {
			MBeanServerFactory.releaseMBeanServer(server);
		}
	}

	public void testWithLocateExistingAndFallbackToPlatformServer() {
		MBeanServerFactoryBean bean = new MBeanServerFactoryBean();
		bean.setLocateExistingServerIfPossible(true);
		bean.afterPropertiesSet();
		try {
			assertSame(ManagementFactory.getPlatformMBeanServer(), bean.getObject());
		}
		finally {
			bean.destroy();
		}
	}

	public void testWithEmptyAgentIdAndFallbackToPlatformServer() {
		MBeanServerFactoryBean bean = new MBeanServerFactoryBean();
		bean.setAgentId("");
		bean.afterPropertiesSet();
		try {
			assertSame(ManagementFactory.getPlatformMBeanServer(), bean.getObject());
		}
		finally {
			bean.destroy();
		}
	}

	public void testCreateMBeanServer() throws Exception {
		testCreation(true, "The server should be available in the list");
	}

	public void testNewMBeanServer() throws Exception {
		testCreation(false, "The server should not be available in the list");
	}

	private void testCreation(boolean referenceShouldExist, String failMsg) throws Exception {
		MBeanServerFactoryBean bean = new MBeanServerFactoryBean();
		bean.setRegisterWithFactory(referenceShouldExist);
		bean.afterPropertiesSet();

		try {
			MBeanServer server = bean.getObject();
			List<MBeanServer> servers = MBeanServerFactory.findMBeanServer(null);

			boolean found = false;
			for (MBeanServer candidate : servers) {
				if (candidate == server) {
					found = true;
					break;
				}
			}

			if (!(found == referenceShouldExist)) {
				fail(failMsg);
			}
		}
		finally {
			bean.destroy();
		}
	}

}
