/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export.naming;

/**
 * @author Rob Harrop
 */
public class KeyNamingStrategyTests extends AbstractNamingStrategyTests {

	private static final String OBJECT_NAME = "spring:name=test";

	protected ObjectNamingStrategy getStrategy() throws Exception {
		return new KeyNamingStrategy();
	}

	protected Object getManagedResource() {
		return new Object();
	}

	protected String getKey() {
		return OBJECT_NAME;
	}

	protected String getCorrectObjectName() {
		return OBJECT_NAME;
	}
	
}
