/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scripting.config;

/**
 * @author Mark Fisher
 */
public class OtherTestBean implements ITestBean {

	public ITestBean getOtherBean() {
		return null;
	}

	public boolean isInitialized() {
		return false;
	}

	public boolean isDestroyed() {
		return false;
	}

}
