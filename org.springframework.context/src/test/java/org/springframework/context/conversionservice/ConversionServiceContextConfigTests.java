/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.conversionservice;

import static org.junit.Assert.*;
import org.junit.Test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Keith Donald
 */
public class ConversionServiceContextConfigTests {
	
	@Test
	public void testConfigOk() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("org/springframework/context/conversionservice/conversionService.xml");
		TestClient client = context.getBean("testClient", TestClient.class);
		assertEquals(2, client.getBars().size());
		assertEquals("value1", client.getBars().get(0).getValue());
		assertEquals("value2", client.getBars().get(1).getValue());
		assertTrue(client.isBool());
	}

}
