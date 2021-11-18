/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export.naming;

import javax.management.ObjectName;

import junit.framework.TestCase;

/**
 * @author Rob Harrop
 */
public abstract class AbstractNamingStrategyTests extends TestCase {

	public void testNaming() throws Exception {
		ObjectNamingStrategy strat = getStrategy();
		ObjectName objectName = strat.getObjectName(getManagedResource(), getKey());
		assertEquals(objectName.getCanonicalName(), getCorrectObjectName());
	}

	protected abstract ObjectNamingStrategy getStrategy() throws Exception;

	protected abstract Object getManagedResource() throws Exception;

	protected abstract String getKey();

	protected abstract String getCorrectObjectName();

}
