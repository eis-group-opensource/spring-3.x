/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/


package test.beans;

import org.springframework.beans.TestBean;

public class Employee extends TestBean {
	
	private String co;

	/**
	 * Constructor for Employee.
	 */
	public Employee() {
		super();
	}
	
	public String getCompany() {
		return co;
	}
	
	public void setCompany(String co) {
		this.co = co;
	}

}
