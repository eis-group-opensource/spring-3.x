/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;
import javax.management.modelmbean.InvalidTargetObjectTypeException;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.RequiredModelMBean;

/**
 * Extension of the {@link RequiredModelMBean} class that ensures the
 * {@link Thread#getContextClassLoader() thread context ClassLoader} is switched
 * for the managed resource's {@link ClassLoader} before any invocations occur.
 *
 * @author Rob Harrop
 * @since 2.0
 * @see RequiredModelMBean
 */
public class SpringModelMBean extends RequiredModelMBean {

	/**
	 * Stores the {@link ClassLoader} to use for invocations. Defaults
	 * to the current thread {@link ClassLoader}.
	 */
	private ClassLoader managedResourceClassLoader = Thread.currentThread().getContextClassLoader();


	/**
	 * Construct a new SpringModelMBean instance with an empty {@link ModelMBeanInfo}.
	 * @see javax.management.modelmbean.RequiredModelMBean#RequiredModelMBean()
	 */
	public SpringModelMBean() throws MBeanException, RuntimeOperationsException {
		super();
	}

	/**
	 * Construct a new SpringModelMBean instance with the given {@link ModelMBeanInfo}.
	 * @see javax.management.modelmbean.RequiredModelMBean#RequiredModelMBean(ModelMBeanInfo)
	 */
	public SpringModelMBean(ModelMBeanInfo mbi) throws MBeanException, RuntimeOperationsException  {
		super(mbi);
	}


	/**
	 * Sets managed resource to expose and stores its {@link ClassLoader}.
	 */
	@Override
	public void setManagedResource(Object managedResource, String managedResourceType)
			throws MBeanException, InstanceNotFoundException, InvalidTargetObjectTypeException {

		this.managedResourceClassLoader = managedResource.getClass().getClassLoader();
		super.setManagedResource(managedResource, managedResourceType);
	}


	/**
	 * Switches the {@link Thread#getContextClassLoader() context ClassLoader} for the
	 * managed resources {@link ClassLoader} before allowing the invocation to occur.
	 * @see javax.management.modelmbean.ModelMBean#invoke
	 */
	@Override
	public Object invoke(String opName, Object[] opArgs, String[] sig)
			throws MBeanException, ReflectionException {

		ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(this.managedResourceClassLoader);
			return super.invoke(opName, opArgs, sig);
		}
		finally {
			Thread.currentThread().setContextClassLoader(currentClassLoader);
		}
	}

	/**
	 * Switches the {@link Thread#getContextClassLoader() context ClassLoader} for the
	 * managed resources {@link ClassLoader} before allowing the invocation to occur.
	 * @see javax.management.modelmbean.ModelMBean#getAttribute
	 */
	@Override
	public Object getAttribute(String attrName)
			throws AttributeNotFoundException, MBeanException, ReflectionException {

		ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(this.managedResourceClassLoader);
			return super.getAttribute(attrName);
		}
		finally {
			Thread.currentThread().setContextClassLoader(currentClassLoader);
		}
	}

	/**
	 * Switches the {@link Thread#getContextClassLoader() context ClassLoader} for the
	 * managed resources {@link ClassLoader} before allowing the invocation to occur.
	 * @see javax.management.modelmbean.ModelMBean#getAttributes
	 */
	@Override
	public AttributeList getAttributes(String[] attrNames) {
		ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(this.managedResourceClassLoader);
			return super.getAttributes(attrNames);
		}
		finally {
			Thread.currentThread().setContextClassLoader(currentClassLoader);
		}
	}

	/**
	 * Switches the {@link Thread#getContextClassLoader() context ClassLoader} for the
	 * managed resources {@link ClassLoader} before allowing the invocation to occur.
	 * @see javax.management.modelmbean.ModelMBean#setAttribute
	 */
	@Override
	public void setAttribute(Attribute attribute)
			throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {

		ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(this.managedResourceClassLoader);
			super.setAttribute(attribute);
		}
		finally {
			Thread.currentThread().setContextClassLoader(currentClassLoader);
		}
	}

	/**
	 * Switches the {@link Thread#getContextClassLoader() context ClassLoader} for the
	 * managed resources {@link ClassLoader} before allowing the invocation to occur.
	 * @see javax.management.modelmbean.ModelMBean#setAttributes
	 */
	@Override
	public AttributeList setAttributes(AttributeList attributes) {
		ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(this.managedResourceClassLoader);
			return super.setAttributes(attributes);
		}
		finally {
			Thread.currentThread().setContextClassLoader(currentClassLoader);
		}
	}

}
