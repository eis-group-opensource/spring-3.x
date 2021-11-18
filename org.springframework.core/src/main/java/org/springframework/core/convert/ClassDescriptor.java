/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.convert;

import java.lang.annotation.Annotation;

/**
 * @author Keith Donald
 * @since 3.1
 */
class ClassDescriptor extends AbstractDescriptor {

	ClassDescriptor(Class<?> type) {
		super(type);
	}

	@Override
	public Annotation[] getAnnotations() {
		return TypeDescriptor.EMPTY_ANNOTATION_ARRAY;
	}

	@Override
	protected Class<?> resolveCollectionElementType() {
		return null;
	}

	@Override
	protected Class<?> resolveMapKeyType() {
		return null;
	}

	@Override
	protected Class<?> resolveMapValueType() {
		return null;
	}

	@Override
	protected AbstractDescriptor nested(Class<?> type, int typeIndex) {
		return new ClassDescriptor(type);
	}
	
}
