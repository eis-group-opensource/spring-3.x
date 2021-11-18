/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.format;

import org.springframework.core.convert.converter.Converter;

/**
 * Registers {@link Converter Converters} and {@link Formatter Formatters} with
 * a FormattingConversionService through the {@link FormatterRegistry} SPI.
 * 
 * @author Keith Donald
 * @since 3.1
 */
public interface FormatterRegistrar {

	/**
	 * Register Formatters and Converters with a FormattingConversionService 
	 * through a FormatterRegistry SPI.
	 * @param registry the FormatterRegistry instance to use.
	 */
	void registerFormatters(FormatterRegistry registry);

}
