/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.convert.converter;

/**
 * For registering converters with a type conversion system.
 *
 * @author Keith Donald
 * @author Juergen Hoeller
 * @since 3.0
 */
public interface ConverterRegistry {
	
	/**
	 * Add a plain converter to this registry.
	 * The convertible sourceType/targetType pair is derived from the Converter's parameterized types.
	 * @throws IllegalArgumentException if the parameterized types could not be resolved
	 */
	void addConverter(Converter<?, ?> converter);

	/**
	 * Add a plain converter to this registry.
	 * The convertible sourceType/targetType pair is specified explicitly.
	 * Allows for a Converter to be reused for multiple distinct pairs without having to create a Converter class for each pair.
	 * @since 3.1
	 */
	void addConverter(Class<?> sourceType, Class<?> targetType, Converter<?, ?> converter);

	/**
	 * Add a generic converter to this registry.
	 */
	void addConverter(GenericConverter converter);
	
	/**
	 * Add a ranged converter factory to this registry.
	 * The convertible sourceType/rangeType pair is derived from the ConverterFactory's parameterized types.
	 * @throws IllegalArgumentException if the parameterized types could not be resolved. 
	 */
	void addConverterFactory(ConverterFactory<?, ?> converterFactory);

	/**
	 * Remove any converters from sourceType to targetType.
	 * @param sourceType the source type
	 * @param targetType the target type
	 */
	void removeConvertible(Class<?> sourceType, Class<?> targetType);

}
