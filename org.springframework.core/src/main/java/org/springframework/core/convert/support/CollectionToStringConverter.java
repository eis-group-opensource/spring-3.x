/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.convert.support;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

/**
 * Converts a Collection to a comma-delimited String.
 *
 * @author Keith Donald
 * @since 3.0
 */
final class CollectionToStringConverter implements ConditionalGenericConverter {

	private static final String DELIMITER = ",";

	private final ConversionService conversionService;

	public CollectionToStringConverter(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(Collection.class, String.class));
	}

	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return ConversionUtils.canConvertElements(sourceType.getElementTypeDescriptor(), targetType, this.conversionService);
	}

	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		if (source == null) {
			return null;
		}
		Collection<?> sourceCollection = (Collection<?>) source;
		if (sourceCollection.size() == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (Object sourceElement : sourceCollection) {
			if (i > 0) {
				sb.append(DELIMITER);
			}
			Object targetElement = this.conversionService.convert(sourceElement, sourceType.elementTypeDescriptor(sourceElement), targetType);
			sb.append(targetElement);
			i++;
		}
		return sb.toString();
	}

}
