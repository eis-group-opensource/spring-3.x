/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.convert.support;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Set;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

/**
 * Converts an Object to a single-element Array containing the Object.
 * Will convert the Object to the target Array's component type if necessary.
 *
 * @author Keith Donald
 * @since 3.0
 */
final class ObjectToArrayConverter implements ConditionalGenericConverter {

	private final ConversionService conversionService;

	public ObjectToArrayConverter(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(Object.class, Object[].class));
	}

	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return ConversionUtils.canConvertElements(sourceType, targetType.getElementTypeDescriptor(), this.conversionService);
	}

	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		if (source == null) {
			return null;
		}
		Object target = Array.newInstance(targetType.getElementTypeDescriptor().getType(), 1);
		Object targetElement = this.conversionService.convert(source, sourceType, targetType.getElementTypeDescriptor());
		Array.set(target, 0, targetElement);
		return target;
	}

}
