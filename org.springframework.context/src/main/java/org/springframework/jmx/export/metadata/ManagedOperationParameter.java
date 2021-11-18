/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export.metadata;

/**
 * Metadata about JMX operation parameters.
 * Used in conjunction with a {@link ManagedOperation} attribute.
 *
 * @author Rob Harrop
 * @since 1.2
 */
public class ManagedOperationParameter {

	private int index = 0;

	private String name = "";

	private String description = "";


	/**
	 * Set the index of this parameter in the operation signature.
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * Return the index of this parameter in the operation signature.
	 */
	public int getIndex() {
		return this.index;
	}

	/**
	 * Set the name of this parameter in the operation signature.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Return the name of this parameter in the operation signature.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Set a description for this parameter.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Return a description for this parameter.
	 */
	public String getDescription() {
		return this.description;
	}

}
