/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.convert.support;

import org.springframework.core.convert.converter.Converter;

/**
 * Converts a String to a Character.
 *
 * @author Keith Donald
 * @since 3.0
 */
final class StringToCharacterConverter implements Converter<String, Character> {

	public Character convert(String source) {
		if (source.length() == 0) {
			return null;
		}
		if (source.length() > 1) {
			throw new IllegalArgumentException(
					"Can only convert a [String] with length of 1 to a [Character]; string value '" + source + "'  has length of " + source.length());
		}
		return source.charAt(0);
	}

}
