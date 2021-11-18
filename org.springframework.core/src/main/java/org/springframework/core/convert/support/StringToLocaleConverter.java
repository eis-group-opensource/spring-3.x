/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.convert.support;

import java.util.Locale;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

/**
 * Converts a String to a Locale.
 *
 * @author Keith Donald
 * @since 3.0
 */
final class StringToLocaleConverter implements Converter<String, Locale> {

	public Locale convert(String source) {
		return StringUtils.parseLocaleString(source);
	}

}
