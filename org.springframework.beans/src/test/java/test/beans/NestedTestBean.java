/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package test.beans;

/**
 * Simple nested test bean used for testing bean factories, AOP framework etc.
 *
 * @author Trevor D. Cook
 * @since 30.09.2003
 */
public class NestedTestBean implements INestedTestBean {

	private String company = "";

	public NestedTestBean() {
	}

	public NestedTestBean(String company) {
		setCompany(company);
	}

	public void setCompany(String company) {
		this.company = (company != null ? company : "");
	}

	public String getCompany() {
		return company;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof NestedTestBean)) {
			return false;
		}
		NestedTestBean ntb = (NestedTestBean) obj;
		return this.company.equals(ntb.company);
	}

	public int hashCode() {
		return this.company.hashCode();
	}

	public String toString() {
		return "NestedTestBean: " + this.company;
	}

}