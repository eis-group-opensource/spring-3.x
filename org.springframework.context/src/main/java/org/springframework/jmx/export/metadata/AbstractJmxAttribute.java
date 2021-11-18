/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export.metadata;

/**
 * Base class for all JMX metadata classes.
 *
 * @author Rob Harrop
 * @since 1.2
 */
public class AbstractJmxAttribute {

	private String description = "";

	private int currencyTimeLimit = -1;


	/**
	 * Set a description for this attribute.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Return a description for this attribute.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Set a currency time limit for this attribute.
	 */
	public void setCurrencyTimeLimit(int currencyTimeLimit) {
		this.currencyTimeLimit = currencyTimeLimit;
	}

	/**
	 * Return a currency time limit for this attribute.
	 */
	public int getCurrencyTimeLimit() {
		return this.currencyTimeLimit;
	}

}
