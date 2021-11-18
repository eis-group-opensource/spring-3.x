/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.config;

import org.springframework.beans.factory.parsing.ParseState;

/**
 * {@link ParseState} entry representing an advisor.
 * 
 * @author Mark Fisher
 * @since 2.0
 */
public class AdvisorEntry implements ParseState.Entry {

	private final String name;


	/**
	 * Creates a new instance of the {@link AdvisorEntry} class.
	 * @param name the bean name of the advisor
	 */
	public AdvisorEntry(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Advisor '" + this.name + "'";
	}

}
