/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jca.support;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ManagedConnectionFactory;

import org.junit.Test;

/**
 * Unit tests for the {@link LocalConnectionFactoryBean} class.
 *
 * @author Rick Evans
 * @author Chris Beams
 */
public final class LocalConnectionFactoryBeanTests {

	@Test(expected=IllegalArgumentException.class)
	public void testManagedConnectionFactoryIsRequired() throws Exception {
		new LocalConnectionFactoryBean().afterPropertiesSet();
	}

	@Test
	public void testIsSingleton() throws Exception {
		LocalConnectionFactoryBean factory = new LocalConnectionFactoryBean();
		assertTrue(factory.isSingleton());
	}

	@Test
	public void testGetObjectTypeIsNullIfConnectionFactoryHasNotBeenConfigured() throws Exception {
		LocalConnectionFactoryBean factory = new LocalConnectionFactoryBean();
		assertNull(factory.getObjectType());
	}

	@Test
	public void testCreatesVanillaConnectionFactoryIfNoConnectionManagerHasBeenConfigured() throws Exception {

		final Object CONNECTION_FACTORY = new Object();

		ManagedConnectionFactory managedConnectionFactory = createMock(ManagedConnectionFactory.class);

		expect(managedConnectionFactory.createConnectionFactory()).andReturn(CONNECTION_FACTORY);
		replay(managedConnectionFactory);

		LocalConnectionFactoryBean factory = new LocalConnectionFactoryBean();
		factory.setManagedConnectionFactory(managedConnectionFactory);
		factory.afterPropertiesSet();
		assertEquals(CONNECTION_FACTORY, factory.getObject());

		verify(managedConnectionFactory);
	}

	@Test
	public void testCreatesManagedConnectionFactoryIfAConnectionManagerHasBeenConfigured() throws Exception {
		ManagedConnectionFactory managedConnectionFactory = createMock(ManagedConnectionFactory.class);

		ConnectionManager connectionManager = createMock(ConnectionManager.class);

		expect(managedConnectionFactory.createConnectionFactory(connectionManager)).andReturn(null);

		replay(connectionManager, managedConnectionFactory);

		LocalConnectionFactoryBean factory = new LocalConnectionFactoryBean();
		factory.setManagedConnectionFactory(managedConnectionFactory);
		factory.setConnectionManager(connectionManager);
		factory.afterPropertiesSet();

		verify(connectionManager, managedConnectionFactory);
	}

}
