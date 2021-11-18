/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.conversionservice;

import org.springframework.core.convert.converter.Converter;

/**
 * @author Keith Donald
 */
public class StringToBarConverter implements Converter<String, Bar> {

	public Bar convert(String source) {
		return new Bar(source);
	}

}
