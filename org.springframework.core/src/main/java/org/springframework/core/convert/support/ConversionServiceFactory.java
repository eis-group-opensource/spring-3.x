/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.convert.support;

import java.util.Set;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.core.convert.converter.GenericConverter;

/**
 * A factory for common {@link org.springframework.core.convert.ConversionService}
 * configurations.
 *
 * @author Keith Donald
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 3.0
 */
public abstract class ConversionServiceFactory {

	/**
	 * Register the given Converter objects with the given target ConverterRegistry.
	 * @param converters the converter objects: implementing {@link Converter},
	 * {@link ConverterFactory}, or {@link GenericConverter}
	 * @param registry the target registry
	 */
	public static void registerConverters(Set<?> converters, ConverterRegistry registry) {
		if (converters != null) {
			for (Object converter : converters) {
				if (converter instanceof GenericConverter) {
					registry.addConverter((GenericConverter) converter);
				}
				else if (converter instanceof Converter<?, ?>) {
					registry.addConverter((Converter<?, ?>) converter);
				}
				else if (converter instanceof ConverterFactory<?, ?>) {
					registry.addConverterFactory((ConverterFactory<?, ?>) converter);
				}
				else {
					throw new IllegalArgumentException("Each converter object must implement one of the " +
							"Converter, ConverterFactory, or GenericConverter interfaces");
				}
			}
		}
	}

	/**
	 * Create a new default GenericConversionService instance that can be safely modified.
	 * @deprecated in Spring 3.1 in favor of {@link DefaultConversionService#DefaultConversionService()}
	 */
	public static GenericConversionService createDefaultConversionService() {
		return new DefaultConversionService();
	}

	/**
	 * Populate the given GenericConversionService instance with the set of default converters.
	 * @deprecated in Spring 3.1 in favor of {@link DefaultConversionService#addDefaultConverters(ConverterRegistry)}
	 */
	public static void addDefaultConverters(GenericConversionService conversionService) {
		DefaultConversionService.addDefaultConverters(conversionService);
	}

}
