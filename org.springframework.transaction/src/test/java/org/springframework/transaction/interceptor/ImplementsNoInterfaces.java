/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.transaction.interceptor;

import org.springframework.beans.TestBean;

/**
 * Test for CGLIB proxying that implements no interfaces
 * and has one dependency.
 *
 * @author Rod Johnson
 */
public class ImplementsNoInterfaces {
	
	private TestBean testBean;
	
	public void setDependency(TestBean testBean) {
		this.testBean = testBean;
	}
	
	public String getName() {
		return testBean.getName();
	}
	
	public void setName(String name) {
		testBean.setName(name);
	}

}
