/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.convert;

import java.lang.annotation.Annotation;

import org.springframework.core.GenericCollectionTypeResolver;
import org.springframework.core.MethodParameter;

/**
 * @author Keith Donald
 * @since 3.1
 */
class ParameterDescriptor extends AbstractDescriptor {

	private final MethodParameter methodParameter;


	public ParameterDescriptor(MethodParameter methodParameter) {
		super(methodParameter.getParameterType());
		if (methodParameter.getNestingLevel() != 1) {
			throw new IllegalArgumentException("MethodParameter argument must have its nestingLevel set to 1");
		}			
		this.methodParameter = methodParameter;
	}

	private ParameterDescriptor(Class<?> type, MethodParameter methodParameter) {
		super(type);
		this.methodParameter = methodParameter;
	}


	@Override
	public Annotation[] getAnnotations() {
		if (this.methodParameter.getParameterIndex() == -1) {
			return TypeDescriptor.nullSafeAnnotations(this.methodParameter.getMethodAnnotations());
		}
		else {
			return TypeDescriptor.nullSafeAnnotations(this.methodParameter.getParameterAnnotations());
		}
	}
	
	@Override
	protected Class<?> resolveCollectionElementType() {
		return GenericCollectionTypeResolver.getCollectionParameterType(this.methodParameter);
	}

	@Override
	protected Class<?> resolveMapKeyType() {
		return GenericCollectionTypeResolver.getMapKeyParameterType(this.methodParameter);
	}

	@Override
	protected Class<?> resolveMapValueType() {
		return GenericCollectionTypeResolver.getMapValueParameterType(this.methodParameter);
	}

	@Override
	protected AbstractDescriptor nested(Class<?> type, int typeIndex) {
		MethodParameter methodParameter = new MethodParameter(this.methodParameter);
		methodParameter.increaseNestingLevel();
		methodParameter.setTypeIndexForCurrentLevel(typeIndex);
		return new ParameterDescriptor(type, methodParameter);
	}

}
