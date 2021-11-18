/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.beans.factory.support.security.support;

import java.util.Properties;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author Costin Leau
 */
public class CustomFactoryBean implements FactoryBean<Object> {

	public Object getObject() throws Exception {
		return System.getProperties();
	}

	public Class getObjectType() {
		System.setProperty("factory.object.type", "true");
		return Properties.class;
	}

	public boolean isSingleton() {
		return true;
	}
}
