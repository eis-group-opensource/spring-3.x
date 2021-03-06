/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.validation;

import java.io.Serializable;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.util.Assert;

/**
 * Default implementation of the {@link Errors} and {@link BindingResult}
 * interfaces, for the registration and evaluation of binding errors on
 * JavaBean objects.
 * 
 * <p>Performs standard JavaBean property access, also supporting nested
 * properties. Normally, application code will work with the
 * <code>Errors</code> interface or the <code>BindingResult</code> interface.
 * A {@link DataBinder} returns its <code>BindingResult</code> via
 * {@link org.springframework.validation.DataBinder#getBindingResult()}.
 *
 * @author Juergen Hoeller
 * @since 2.0
 * @see DataBinder#getBindingResult()
 * @see DataBinder#initBeanPropertyAccess()
 * @see DirectFieldBindingResult
 */
@SuppressWarnings("serial")
public class BeanPropertyBindingResult extends AbstractPropertyBindingResult implements Serializable {

	private final Object target;

	private final boolean autoGrowNestedPaths;

	private final int autoGrowCollectionLimit;

	private transient BeanWrapper beanWrapper;


	/**
	 * Creates a new instance of the {@link BeanPropertyBindingResult} class.
	 * @param target the target bean to bind onto
	 * @param objectName the name of the target object
	 */
	public BeanPropertyBindingResult(Object target, String objectName) {
		this(target, objectName, true, Integer.MAX_VALUE);
	}

	/**
	 * Creates a new instance of the {@link BeanPropertyBindingResult} class.
	 * @param target the target bean to bind onto
	 * @param objectName the name of the target object
	 * @param autoGrowNestedPaths whether to "auto-grow" a nested path that contains a null value
	 * @param autoGrowCollectionLimit the limit for array and collection auto-growing
	 */
	public BeanPropertyBindingResult(Object target, String objectName, boolean autoGrowNestedPaths, int autoGrowCollectionLimit) {
		super(objectName);
		this.target = target;
		this.autoGrowNestedPaths = autoGrowNestedPaths;
		this.autoGrowCollectionLimit = autoGrowCollectionLimit;
	}


	@Override
	public final Object getTarget() {
		return this.target;
	}

	/**
	 * Returns the {@link BeanWrapper} that this instance uses.
	 * Creates a new one if none existed before.
	 * @see #createBeanWrapper()
	 */
	@Override
	public final ConfigurablePropertyAccessor getPropertyAccessor() {
		if (this.beanWrapper == null) {
			this.beanWrapper = createBeanWrapper();
			this.beanWrapper.setExtractOldValueForEditor(true);
			this.beanWrapper.setAutoGrowNestedPaths(this.autoGrowNestedPaths);
			this.beanWrapper.setAutoGrowCollectionLimit(this.autoGrowCollectionLimit);
		}
		return this.beanWrapper;
	}

	/**
	 * Create a new {@link BeanWrapper} for the underlying target object.
	 * @see #getTarget()
	 */
	protected BeanWrapper createBeanWrapper() {
		Assert.state(this.target != null, "Cannot access properties on null bean instance '" + getObjectName() + "'!");
		return PropertyAccessorFactory.forBeanPropertyAccess(this.target);
	}

}
