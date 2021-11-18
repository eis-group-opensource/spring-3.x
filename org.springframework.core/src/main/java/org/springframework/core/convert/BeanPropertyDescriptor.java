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
class BeanPropertyDescriptor extends AbstractDescriptor {

	private final Property property;

	private final MethodParameter methodParameter;
	
	private final Annotation[] annotations;
	

	public BeanPropertyDescriptor(Property property) {
		super(property.getType());
		this.property = property;
		this.methodParameter = property.getMethodParameter();
		this.annotations = property.getAnnotations();
	}


	@Override
	public Annotation[] getAnnotations() {
		return this.annotations;
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
		return new BeanPropertyDescriptor(type, this.property, methodParameter, this.annotations);
	}
	

	// internal

	private BeanPropertyDescriptor(Class<?> type, Property propertyDescriptor, MethodParameter methodParameter, Annotation[] annotations) {
		super(type);
		this.property = propertyDescriptor;
		this.methodParameter = methodParameter;
		this.annotations = annotations;
	}
	
}
