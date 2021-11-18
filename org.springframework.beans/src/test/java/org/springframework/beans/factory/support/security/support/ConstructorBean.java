/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.beans.factory.support.security.support;

/**
 * @author Costin Leau
 */
public class ConstructorBean {

	public ConstructorBean() {
		System.getProperties();
	}

	public ConstructorBean(Object obj) {
		System.out.println("Received object " + obj);
	}
}
