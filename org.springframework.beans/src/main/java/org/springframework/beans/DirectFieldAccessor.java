/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans;

import java.beans.PropertyChangeEvent;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.ConverterNotFoundException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * {@link PropertyAccessor} implementation that directly accesses instance fields.
 * Allows for direct binding to fields instead of going through JavaBean setters.
 *
 * <p>This implementation just supports fields in the actual target object.
 * It is not able to traverse nested fields.
 *
 * <p>A DirectFieldAccessor's default for the "extractOldValueForEditor" setting
 * is "true", since a field can always be read without side effects.
 *
 * @author Juergen Hoeller
 * @since 2.0
 * @see #setExtractOldValueForEditor
 * @see BeanWrapper
 * @see org.springframework.validation.DirectFieldBindingResult
 * @see org.springframework.validation.DataBinder#initDirectFieldAccess()
 */
public class DirectFieldAccessor extends AbstractPropertyAccessor {

	private final Object target;

	private final Map<String, Field> fieldMap = new HashMap<String, Field>();

	private final TypeConverterDelegate typeConverterDelegate;


	/**
	 * Create a new DirectFieldAccessor for the given target object.
	 * @param target the target object to access
	 */
	public DirectFieldAccessor(final Object target) {
		Assert.notNull(target, "Target object must not be null");
		this.target = target;
		ReflectionUtils.doWithFields(this.target.getClass(), new ReflectionUtils.FieldCallback() {
			public void doWith(Field field) {
				if (fieldMap.containsKey(field.getName())) {
					// ignore superclass declarations of fields already found in a subclass
				} else {
					fieldMap.put(field.getName(), field);
				}
			}
		});
		this.typeConverterDelegate = new TypeConverterDelegate(this, target);
		registerDefaultEditors();
		setExtractOldValueForEditor(true);
	}


	public boolean isReadableProperty(String propertyName) throws BeansException {
		return this.fieldMap.containsKey(propertyName);
	}

	public boolean isWritableProperty(String propertyName) throws BeansException {
		return this.fieldMap.containsKey(propertyName);
	}

	@Override
	public Class<?> getPropertyType(String propertyName) throws BeansException {
		Field field = this.fieldMap.get(propertyName);
		if (field != null) {
			return field.getType();
		}
		return null;
	}

	public TypeDescriptor getPropertyTypeDescriptor(String propertyName) throws BeansException {
		Field field = this.fieldMap.get(propertyName);
		if (field != null) {
			return new TypeDescriptor(field);
		}
		return null;
	}

	@Override
	public Object getPropertyValue(String propertyName) throws BeansException {
		Field field = this.fieldMap.get(propertyName);
		if (field == null) {
			throw new NotReadablePropertyException(
					this.target.getClass(), propertyName, "Field '" + propertyName + "' does not exist");
		}
		try {
			ReflectionUtils.makeAccessible(field);
			return field.get(this.target);
		}
		catch (IllegalAccessException ex) {
			throw new InvalidPropertyException(this.target.getClass(), propertyName, "Field is not accessible", ex);
		}
	}

	@Override
	public void setPropertyValue(String propertyName, Object newValue) throws BeansException {
		Field field = this.fieldMap.get(propertyName);
		if (field == null) {
			throw new NotWritablePropertyException(
					this.target.getClass(), propertyName, "Field '" + propertyName + "' does not exist");
		}
		Object oldValue = null;
		try {
			ReflectionUtils.makeAccessible(field);
			oldValue = field.get(this.target);
			Object convertedValue = this.typeConverterDelegate.convertIfNecessary(
					field.getName(), oldValue, newValue, field.getType(), new TypeDescriptor(field));
			field.set(this.target, convertedValue);
		}
		catch (ConverterNotFoundException ex) {
			PropertyChangeEvent pce = new PropertyChangeEvent(this.target, propertyName, oldValue, newValue);
			throw new ConversionNotSupportedException(pce, field.getType(), ex);
		}
		catch (ConversionException ex) {
			PropertyChangeEvent pce = new PropertyChangeEvent(this.target, propertyName, oldValue, newValue);
			throw new TypeMismatchException(pce, field.getType(), ex);
		}
		catch (IllegalStateException ex) {
			PropertyChangeEvent pce = new PropertyChangeEvent(this.target, propertyName, oldValue, newValue);
			throw new ConversionNotSupportedException(pce, field.getType(), ex);
		}
		catch (IllegalArgumentException ex) {
			PropertyChangeEvent pce = new PropertyChangeEvent(this.target, propertyName, oldValue, newValue);
			throw new TypeMismatchException(pce, field.getType(), ex);
		}
		catch (IllegalAccessException ex) {
			throw new InvalidPropertyException(this.target.getClass(), propertyName, "Field is not accessible", ex);
		}
	}

	public <T> T convertIfNecessary(
			Object value, Class<T> requiredType, MethodParameter methodParam) throws TypeMismatchException {
		try {
			return this.typeConverterDelegate.convertIfNecessary(value, requiredType, methodParam);
		}
		catch (IllegalArgumentException ex) {
			throw new TypeMismatchException(value, requiredType, ex);
		}
		catch (IllegalStateException ex) {
			throw new ConversionNotSupportedException(value, requiredType, ex);
		}
	}

}
