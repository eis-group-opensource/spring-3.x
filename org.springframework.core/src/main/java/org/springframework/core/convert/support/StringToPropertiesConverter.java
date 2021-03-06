/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.convert.support;

import java.io.ByteArrayInputStream;
import java.util.Properties;

import org.springframework.core.convert.converter.Converter;

/**
 * Converts a String to a Properties by calling Properties#load(java.io.InputStream).
 * Uses ISO-8559-1 encoding required by Properties.
 * 
 * @author Keith Donald
 * @since 3.0
 */
final class StringToPropertiesConverter implements Converter<String, Properties> {

	public Properties convert(String source) {
		try {
			Properties props = new Properties();
			// Must use the ISO-8859-1 encoding because Properties.load(stream) expects it.
			props.load(new ByteArrayInputStream(source.getBytes("ISO-8859-1")));
			return props;
		}
		catch (Exception ex) {
			// Should never happen.
			throw new IllegalArgumentException("Failed to parse [" + source + "] into Properties", ex);
		}
	}

}
