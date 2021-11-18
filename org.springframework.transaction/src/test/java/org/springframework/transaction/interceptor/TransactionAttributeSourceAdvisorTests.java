/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.transaction.interceptor;

import java.util.Properties;

import org.springframework.util.SerializationTestUtils;

import junit.framework.TestCase;

/**
 * 
 * @author Rod Johnson
 */
public class TransactionAttributeSourceAdvisorTests extends TestCase {
	
	public TransactionAttributeSourceAdvisorTests(String s) {
		super(s);
	}
	
	public void testSerializability() throws Exception {
		TransactionInterceptor ti = new TransactionInterceptor();
		ti.setTransactionAttributes(new Properties());
		TransactionAttributeSourceAdvisor tas = new TransactionAttributeSourceAdvisor(ti);
		SerializationTestUtils.serializeAndDeserialize(tas);
	}

}
