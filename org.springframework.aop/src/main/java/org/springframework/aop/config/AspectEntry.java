/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.config;

import org.springframework.beans.factory.parsing.ParseState;
import org.springframework.util.StringUtils;

/**
 * {@link ParseState} entry representing an aspect.
 *
 * @author Mark Fisher
 * @author Juergen Hoeller
 * @since 2.0
 */
public class AspectEntry implements ParseState.Entry {

	private final String id;

	private final String ref;


	/**
	 * Create a new AspectEntry.
	 * @param id the id of the aspect element
	 * @param ref the bean name referenced by this aspect element
	 */
	public AspectEntry(String id, String ref) {
		this.id = id;
		this.ref = ref;
	}

	@Override
	public String toString() {
		return "Aspect: " + (StringUtils.hasLength(this.id) ? "id='" + this.id + "'" : "ref='" + this.ref + "'");
	}

}
