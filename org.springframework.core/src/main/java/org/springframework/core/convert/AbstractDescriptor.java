/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.convert;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;

import org.springframework.util.Assert;

/**
 * @author Keith Donald
 * @since 3.1
 */
abstract class AbstractDescriptor {

	private final Class<?> type;


	protected AbstractDescriptor(Class<?> type) {
		Assert.notNull(type, "Type must not be null");
		this.type = type;
	}
	

	public Class<?> getType() {
		return this.type;
	}

	public TypeDescriptor getElementTypeDescriptor() {
		if (isCollection()) {
			Class<?> elementType = resolveCollectionElementType();
			return (elementType != null ? new TypeDescriptor(nested(elementType, 0)) : null);
		}
		else if (isArray()) {
			Class<?> elementType = getType().getComponentType();
			return new TypeDescriptor(nested(elementType, 0));				
		}
		else {
			return null;
		}
	}
	
	public TypeDescriptor getMapKeyTypeDescriptor() {
		if (isMap()) {
			Class<?> keyType = resolveMapKeyType();
			return keyType != null ? new TypeDescriptor(nested(keyType, 0)) : null;
		}
		else {
			return null;
		}
	}
	
	public TypeDescriptor getMapValueTypeDescriptor() {
		if (isMap()) {
			Class<?> valueType = resolveMapValueType();
			return valueType != null ? new TypeDescriptor(nested(valueType, 1)) : null;
		}
		else {
			return null;
		}
	}

	public AbstractDescriptor nested() {
		if (isCollection()) {
			Class<?> elementType = resolveCollectionElementType();
			return (elementType != null ? nested(elementType, 0) : null);
		}
		else if (isArray()) {
			return nested(getType().getComponentType(), 0);
		}
		else if (isMap()) {
			Class<?> mapValueType = resolveMapValueType();
			return (mapValueType != null ? nested(mapValueType, 1) : null);
		}
		else if (Object.class.equals(getType())) {
			// could be a collection type but we don't know about its element type,
			// so let's just assume there is an element type of type Object
			return this;
		}
		else {
			throw new IllegalStateException("Not a collection, array, or map: cannot resolve nested value types");
		}
	}
	

	// subclassing hooks
	
	public abstract Annotation[] getAnnotations();

	protected abstract Class<?> resolveCollectionElementType();
	
	protected abstract Class<?> resolveMapKeyType();
	
	protected abstract Class<?> resolveMapValueType();
	
	protected abstract AbstractDescriptor nested(Class<?> type, int typeIndex);
	

	// internal helpers
	
	private boolean isCollection() {
		return Collection.class.isAssignableFrom(getType());
	}
	
	private boolean isArray() {
		return getType().isArray();
	}
	
	private boolean isMap() {
		return Map.class.isAssignableFrom(getType());
	}
	
}
