/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export.naming;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import junit.framework.TestCase;

import org.springframework.jmx.JmxTestBean;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

/**
 * @author Rob Harrop
 */
public class IdentityNamingStrategyTests extends TestCase {

	public void testNaming() throws MalformedObjectNameException {
		JmxTestBean bean = new JmxTestBean();
		IdentityNamingStrategy strategy = new IdentityNamingStrategy();
		ObjectName objectName = strategy.getObjectName(bean, "null");
		assertEquals("Domain is incorrect", bean.getClass().getPackage().getName(),
				objectName.getDomain());
		assertEquals("Type property is incorrect", ClassUtils.getShortName(bean.getClass()),
				objectName.getKeyProperty(IdentityNamingStrategy.TYPE_KEY));
		assertEquals("HashCode property is incorrect", ObjectUtils.getIdentityHexString(bean),
				objectName.getKeyProperty(IdentityNamingStrategy.HASH_CODE_KEY));
	}

}
