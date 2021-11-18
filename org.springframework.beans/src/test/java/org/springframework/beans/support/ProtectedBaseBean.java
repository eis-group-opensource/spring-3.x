/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.support;

/**
 * @author Juergen Hoeller
 * @since 29.07.2004
 */
class ProtectedBaseBean {

	private String someProperty;

	public void setSomeProperty(String someProperty) {
		this.someProperty = someProperty;
	}

	public String getSomeProperty() {
		return someProperty;
	}

}