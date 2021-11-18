/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export;

/**
 * @author Rob Harrop
 */
public class ExceptionOnInitBean {

	private boolean exceptOnInit = false;
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setExceptOnInit(boolean exceptOnInit) {
		this.exceptOnInit = exceptOnInit;
	}

	public ExceptionOnInitBean() {
		if (exceptOnInit) {
			throw new RuntimeException("I am being init'd!");
		}
	}

}
