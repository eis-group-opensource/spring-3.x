/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export.naming;

import java.util.Properties;

/**
 * @author Rob Harrop
 * @author Juergen Hoeller
 */
public class PropertiesNamingStrategyTests extends AbstractNamingStrategyTests {

	private static final String OBJECT_NAME = "bean:name=namingTest";

	protected ObjectNamingStrategy getStrategy() throws Exception {
		KeyNamingStrategy strat = new KeyNamingStrategy();
		Properties mappings = new Properties();
		mappings.setProperty("namingTest", "bean:name=namingTest");
		strat.setMappings(mappings);
		strat.afterPropertiesSet();
		return strat;
	}

	protected Object getManagedResource() {
		return new Object();
	}

	protected String getKey() {
		return "namingTest";
	}

	protected String getCorrectObjectName() {
		return OBJECT_NAME;
	}

}
