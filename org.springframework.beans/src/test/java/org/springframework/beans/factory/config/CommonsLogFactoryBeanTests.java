/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.config;

import static org.junit.Assert.*;

import org.apache.commons.logging.Log;
import org.junit.Test;

/**
 * Unit tests for {@link CommonsLogFactoryBean}.
 *
 * @author Rick Evans
 * @author Chris Beams
 */
public final class CommonsLogFactoryBeanTests {

	@Test
	public void testIsSingleton() throws Exception {
		CommonsLogFactoryBean factory = new CommonsLogFactoryBean();
		assertTrue(factory.isSingleton());
	}

	@Test
	public void testGetObjectTypeDefaultsToPlainResourceInterfaceifLookupResourceIsNotSupplied() throws Exception {
		CommonsLogFactoryBean factory = new CommonsLogFactoryBean();
		assertEquals(Log.class, factory.getObjectType());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testWhenLogNameIsMissing() throws Exception {
		CommonsLogFactoryBean factory = new CommonsLogFactoryBean();
		factory.afterPropertiesSet();
	}

	@Test
	public void testSunnyDayPath() throws Exception {
		CommonsLogFactoryBean factory = new CommonsLogFactoryBean();
		factory.setLogName("The Tin Drum");
		factory.afterPropertiesSet();
		Object object = factory.getObject();

		assertNotNull("As per FactoryBean contract, the return value of getObject() cannot be null.", object);
		assertTrue("Obviously not getting a Log back", Log.class.isAssignableFrom(object.getClass()));
	}

}
