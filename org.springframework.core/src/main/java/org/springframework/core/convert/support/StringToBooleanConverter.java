/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.convert.support;

import java.util.HashSet;
import java.util.Set;

import org.springframework.core.convert.converter.Converter;

/**
 * Converts String to a Boolean.
 *
 * @author Keith Donald
 * @author Juergen Hoeller
 * @since 3.0
 */
final class StringToBooleanConverter implements Converter<String, Boolean> {

	private static final Set<String> trueValues = new HashSet<String>(4);

	private static final Set<String> falseValues = new HashSet<String>(4);

	static {
		trueValues.add("true");
		trueValues.add("on");
		trueValues.add("yes");
		trueValues.add("1");

		falseValues.add("false");
		falseValues.add("off");
		falseValues.add("no");
		falseValues.add("0");
	}
	
	public Boolean convert(String source) {
		String value = source.trim();
		if ("".equals(value)) {
			return null;
		}
		value = value.toLowerCase();
		if (trueValues.contains(value)) {
			return Boolean.TRUE;
		}
		else if (falseValues.contains(value)) {
			return Boolean.FALSE;
		}
		else {
			throw new IllegalArgumentException("Invalid boolean value '" + source + "'");
		}
	}

}
