/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export.assembler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import javax.management.Descriptor;
import javax.management.MBeanParameterInfo;
import javax.management.modelmbean.ModelMBeanNotificationInfo;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jmx.export.metadata.InvalidMetadataException;
import org.springframework.jmx.export.metadata.JmxAttributeSource;
import org.springframework.jmx.export.metadata.JmxMetadataUtils;
import org.springframework.jmx.export.metadata.ManagedAttribute;
import org.springframework.jmx.export.metadata.ManagedMetric;
import org.springframework.jmx.export.metadata.ManagedNotification;
import org.springframework.jmx.export.metadata.ManagedOperation;
import org.springframework.jmx.export.metadata.ManagedOperationParameter;
import org.springframework.jmx.export.metadata.ManagedResource;
import org.springframework.jmx.support.MetricType;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Implementation of the {@link org.springframework.jmx.export.assembler.MBeanInfoAssembler}
 * interface that reads the management interface information from source level metadata.
 *
 * <p>Uses the {@link JmxAttributeSource} strategy interface, so that
 * metadata can be read using any supported implementation. Out of the box,
 * Spring provides an implementation based on JDK 1.5+ annotations,
 * <code>AnnotationJmxAttributeSource</code>.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @author Jennifer Hickey
 * @since 1.2
 * @see #setAttributeSource
 * @see org.springframework.jmx.export.annotation.AnnotationJmxAttributeSource
 */
public class MetadataMBeanInfoAssembler extends AbstractReflectiveMBeanInfoAssembler
		implements AutodetectCapableMBeanInfoAssembler, InitializingBean {

	private JmxAttributeSource attributeSource;


	/**
	 * Create a new <code>MetadataMBeanInfoAssembler<code> which needs to be
	 * configured through the {@link #setAttributeSource} method.
	 */
	public MetadataMBeanInfoAssembler() {
	}

	/**
	 * Create a new <code>MetadataMBeanInfoAssembler<code> for the given
	 * <code>JmxAttributeSource</code>.
	 * @param attributeSource the JmxAttributeSource to use
	 */
	public MetadataMBeanInfoAssembler(JmxAttributeSource attributeSource) {
		Assert.notNull(attributeSource, "JmxAttributeSource must not be null");
		this.attributeSource = attributeSource;
	}


	/**
	 * Set the <code>JmxAttributeSource</code> implementation to use for
	 * reading the metadata from the bean class.
	 * @see org.springframework.jmx.export.annotation.AnnotationJmxAttributeSource
	 */
	public void setAttributeSource(JmxAttributeSource attributeSource) {
		Assert.notNull(attributeSource, "JmxAttributeSource must not be null");
		this.attributeSource = attributeSource;
	}

	public void afterPropertiesSet() {
		if (this.attributeSource == null) {
			throw new IllegalArgumentException("Property 'attributeSource' is required");
		}
	}


	/**
	 * Throws an IllegalArgumentException if it encounters a JDK dynamic proxy.
	 * Metadata can only be read from target classes and CGLIB proxies!
	 */
	@Override
	protected void checkManagedBean(Object managedBean) throws IllegalArgumentException {
		if (AopUtils.isJdkDynamicProxy(managedBean)) {
			throw new IllegalArgumentException(
					"MetadataMBeanInfoAssembler does not support JDK dynamic proxies - " +
					"export the target beans directly or use CGLIB proxies instead");
		}
	}

	/**
	 * Used for autodetection of beans. Checks to see if the bean's class has a
	 * <code>ManagedResource</code> attribute. If so it will add it list of included beans.
	 * @param beanClass the class of the bean
	 * @param beanName the name of the bean in the bean factory
	 */
	public boolean includeBean(Class<?> beanClass, String beanName) {
		return (this.attributeSource.getManagedResource(getClassToExpose(beanClass)) != null);
	}

	/**
	 * Vote on the inclusion of an attribute accessor.
	 * @param method the accessor method
	 * @param beanKey the key associated with the MBean in the beans map
	 * @return whether the method has the appropriate metadata
	 */
	@Override
	protected boolean includeReadAttribute(Method method, String beanKey) {
		return hasManagedAttribute(method) || hasManagedMetric(method);
	}

	/**
	 * Votes on the inclusion of an attribute mutator.
	 * @param method the mutator method
	 * @param beanKey the key associated with the MBean in the beans map
	 * @return whether the method has the appropriate metadata
	 */
	@Override
	protected boolean includeWriteAttribute(Method method, String beanKey) {
		return hasManagedAttribute(method);
	}

	/**
	 * Votes on the inclusion of an operation.
	 * @param method the operation method
	 * @param beanKey the key associated with the MBean in the beans map
	 * @return whether the method has the appropriate metadata
	 */
	@Override
	protected boolean includeOperation(Method method, String beanKey) {
		PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
		if (pd != null) {
			if(hasManagedAttribute(method)) {
				return true;
			}
		}
		return hasManagedOperation(method);
	}

	/**
	 * Checks to see if the given Method has the <code>ManagedAttribute</code> attribute.
	 */
	private boolean hasManagedAttribute(Method method) {
		return (this.attributeSource.getManagedAttribute(method) != null);
	}

	/**
	 * Checks to see if the given Method has the <code>ManagedMetric</code> attribute.
	 */
	private boolean hasManagedMetric(Method method) {
		return (this.attributeSource.getManagedMetric(method) != null);
	}

	/**
	 * Checks to see if the given Method has the <code>ManagedOperation</code> attribute.
	 * @param method the method to check
	 */
	private boolean hasManagedOperation(Method method) {
		return (this.attributeSource.getManagedOperation(method) != null);
	}


	/**
	 * Reads managed resource description from the source level metadata.
	 * Returns an empty <code>String</code> if no description can be found.
	 */
	@Override
	protected String getDescription(Object managedBean, String beanKey) {
		ManagedResource mr = this.attributeSource.getManagedResource(getClassToExpose(managedBean));
		return (mr != null ? mr.getDescription() : "");
	}

	/**
	 * Creates a description for the attribute corresponding to this property
	 * descriptor. Attempts to create the description using metadata from either
	 * the getter or setter attributes, otherwise uses the property name.
	 */
	@Override
	protected String getAttributeDescription(PropertyDescriptor propertyDescriptor, String beanKey) {
		Method readMethod = propertyDescriptor.getReadMethod();
		Method writeMethod = propertyDescriptor.getWriteMethod();

		ManagedAttribute getter =
				(readMethod != null ? this.attributeSource.getManagedAttribute(readMethod) : null);
		ManagedAttribute setter =
				(writeMethod != null ? this.attributeSource.getManagedAttribute(writeMethod) : null);

		if (getter != null && StringUtils.hasText(getter.getDescription())) {
			return getter.getDescription();
		}
		else if (setter != null && StringUtils.hasText(setter.getDescription())) {
			return setter.getDescription();
		}

		ManagedMetric metric = (readMethod != null ? this.attributeSource.getManagedMetric(readMethod) : null);
		if (metric != null && StringUtils.hasText(metric.getDescription())) {
			return metric.getDescription();
		}

		return propertyDescriptor.getDisplayName();
	}

	/**
	 * Retrieves the description for the supplied <code>Method</code> from the
	 * metadata. Uses the method name is no description is present in the metadata.
	 */
	@Override
	protected String getOperationDescription(Method method, String beanKey) {
		PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
		if (pd != null) {
			ManagedAttribute ma = this.attributeSource.getManagedAttribute(method);
			if (ma != null && StringUtils.hasText(ma.getDescription())) {
				return ma.getDescription();
			}
			ManagedMetric metric = this.attributeSource.getManagedMetric(method);
			if (metric != null && StringUtils.hasText(metric.getDescription())) {
				return metric.getDescription();
			}
			return method.getName();
		}
		else {
			ManagedOperation mo = this.attributeSource.getManagedOperation(method);
			if (mo != null && StringUtils.hasText(mo.getDescription())) {
				return mo.getDescription();
			}
			return method.getName();
		}
	}

	/**
	 * Reads <code>MBeanParameterInfo</code> from the <code>ManagedOperationParameter</code>
	 * attributes attached to a method. Returns an empty array of <code>MBeanParameterInfo</code>
	 * if no attributes are found.
	 */
	@Override
	protected MBeanParameterInfo[] getOperationParameters(Method method, String beanKey) {
		ManagedOperationParameter[] params = this.attributeSource.getManagedOperationParameters(method);
		if (params == null || params.length == 0) {
			return new MBeanParameterInfo[0];
		}

		MBeanParameterInfo[] parameterInfo = new MBeanParameterInfo[params.length];
		Class[] methodParameters = method.getParameterTypes();

		for (int i = 0; i < params.length; i++) {
			ManagedOperationParameter param = params[i];
			parameterInfo[i] =
					new MBeanParameterInfo(param.getName(), methodParameters[i].getName(), param.getDescription());
		}

		return parameterInfo;
	}

	/**
	 * Reads the {@link ManagedNotification} metadata from the <code>Class</code> of the managed resource
	 * and generates and returns the corresponding {@link ModelMBeanNotificationInfo} metadata.
	 */
	@Override
	protected ModelMBeanNotificationInfo[] getNotificationInfo(Object managedBean, String beanKey) {
		ManagedNotification[] notificationAttributes =
				this.attributeSource.getManagedNotifications(getClassToExpose(managedBean));
		ModelMBeanNotificationInfo[] notificationInfos =
				new ModelMBeanNotificationInfo[notificationAttributes.length];

		for (int i = 0; i < notificationAttributes.length; i++) {
			ManagedNotification attribute = notificationAttributes[i];
			notificationInfos[i] = JmxMetadataUtils.convertToModelMBeanNotificationInfo(attribute);
		}

		return notificationInfos;
	}

	/**
	 * Adds descriptor fields from the <code>ManagedResource</code> attribute
	 * to the MBean descriptor. Specifically, adds the <code>currencyTimeLimit</code>,
	 * <code>persistPolicy</code>, <code>persistPeriod</code>, <code>persistLocation</code>
	 * and <code>persistName</code> descriptor fields if they are present in the metadata.
	 */
	@Override
	protected void populateMBeanDescriptor(Descriptor desc, Object managedBean, String beanKey) {
		ManagedResource mr = this.attributeSource.getManagedResource(getClassToExpose(managedBean));
		if (mr == null) {
			throw new InvalidMetadataException(
					"No ManagedResource attribute found for class: " + getClassToExpose(managedBean));
		}

		applyCurrencyTimeLimit(desc, mr.getCurrencyTimeLimit());

		if (mr.isLog()) {
			desc.setField(FIELD_LOG, "true");
		}
		if (StringUtils.hasLength(mr.getLogFile())) {
			desc.setField(FIELD_LOG_FILE, mr.getLogFile());
		}

		if (StringUtils.hasLength(mr.getPersistPolicy())) {
			desc.setField(FIELD_PERSIST_POLICY, mr.getPersistPolicy());
		}
		if (mr.getPersistPeriod() >= 0) {
			desc.setField(FIELD_PERSIST_PERIOD, Integer.toString(mr.getPersistPeriod()));
		}
		if (StringUtils.hasLength(mr.getPersistName())) {
			desc.setField(FIELD_PERSIST_NAME, mr.getPersistName());
		}
		if (StringUtils.hasLength(mr.getPersistLocation())) {
			desc.setField(FIELD_PERSIST_LOCATION, mr.getPersistLocation());
		}
	}

	/**
	 * Adds descriptor fields from the <code>ManagedAttribute</code> attribute or the <code>ManagedMetric</code> attribute
	 * to the attribute descriptor.
	 */
	@Override
	protected void populateAttributeDescriptor(Descriptor desc, Method getter, Method setter, String beanKey) {
		if(getter != null && hasManagedMetric(getter)) {
			populateMetricDescriptor(desc, this.attributeSource.getManagedMetric(getter));
		}
		else {
			ManagedAttribute gma =
				(getter == null) ? ManagedAttribute.EMPTY : this.attributeSource.getManagedAttribute(getter);
			ManagedAttribute sma =
				(setter == null) ? ManagedAttribute.EMPTY : this.attributeSource.getManagedAttribute(setter);
			populateAttributeDescriptor(desc,gma,sma);
		}
	}

	private void populateAttributeDescriptor(Descriptor desc, ManagedAttribute gma, ManagedAttribute sma) {
		applyCurrencyTimeLimit(desc, resolveIntDescriptor(gma.getCurrencyTimeLimit(), sma.getCurrencyTimeLimit()));

		Object defaultValue = resolveObjectDescriptor(gma.getDefaultValue(), sma.getDefaultValue());
		desc.setField(FIELD_DEFAULT, defaultValue);

		String persistPolicy = resolveStringDescriptor(gma.getPersistPolicy(), sma.getPersistPolicy());
		if (StringUtils.hasLength(persistPolicy)) {
			desc.setField(FIELD_PERSIST_POLICY, persistPolicy);
		}
		int persistPeriod = resolveIntDescriptor(gma.getPersistPeriod(), sma.getPersistPeriod());
		if (persistPeriod >= 0) {
			desc.setField(FIELD_PERSIST_PERIOD, Integer.toString(persistPeriod));
		}
	}

	private void populateMetricDescriptor(Descriptor desc, ManagedMetric metric) {
		applyCurrencyTimeLimit(desc, metric.getCurrencyTimeLimit());

		if (StringUtils.hasLength(metric.getPersistPolicy())) {
			desc.setField(FIELD_PERSIST_POLICY, metric.getPersistPolicy());
		}
		if (metric.getPersistPeriod() >= 0) {
			desc.setField(FIELD_PERSIST_PERIOD, Integer.toString(metric.getPersistPeriod()));
		}

		if (StringUtils.hasLength(metric.getDisplayName())) {
			desc.setField(FIELD_DISPLAY_NAME, metric.getDisplayName());
		}

		if(StringUtils.hasLength(metric.getUnit())) {
			desc.setField(FIELD_UNITS, metric.getUnit());
		}

		if(StringUtils.hasLength(metric.getCategory())) {
			desc.setField(FIELD_METRIC_CATEGORY, metric.getCategory());
		}

		String metricType = (metric.getMetricType() == null) ? MetricType.GAUGE.toString() : metric.getMetricType().toString();
		desc.setField(FIELD_METRIC_TYPE, metricType);
	}

	/**
	 * Adds descriptor fields from the <code>ManagedAttribute</code> attribute
	 * to the attribute descriptor. Specifically, adds the <code>currencyTimeLimit</code>
	 * descriptor field if it is present in the metadata.
	 */
	@Override
	protected void populateOperationDescriptor(Descriptor desc, Method method, String beanKey) {
		ManagedOperation mo = this.attributeSource.getManagedOperation(method);
		if (mo != null) {
			applyCurrencyTimeLimit(desc, mo.getCurrencyTimeLimit());
		}
	}

	/**
	 * Determines which of two <code>int</code> values should be used as the value
	 * for an attribute descriptor. In general, only the getter or the setter will
	 * be have a non-negative value so we use that value. In the event that both values
	 * are non-negative, we use the greater of the two. This method can be used to
	 * resolve any <code>int</code> valued descriptor where there are two possible values.
	 * @param getter the int value associated with the getter for this attribute
	 * @param setter the int associated with the setter for this attribute
	 */
	private int resolveIntDescriptor(int getter, int setter) {
		return (getter >= setter ? getter : setter);
	}

	/**
	 * Locates the value of a descriptor based on values attached
	 * to both the getter and setter methods. If both have values
	 * supplied then the value attached to the getter is preferred.
	 * @param getter the Object value associated with the get method
	 * @param setter the Object value associated with the set method
	 * @return the appropriate Object to use as the value for the descriptor
	 */
	private Object resolveObjectDescriptor(Object getter, Object setter) {
		return (getter != null ? getter : setter);
	}

	/**
	 * Locates the value of a descriptor based on values attached
	 * to both the getter and setter methods. If both have values
	 * supplied then the value attached to the getter is preferred.
	 * The supplied default value is used to check to see if the value
	 * associated with the getter has changed from the default.
	 * @param getter the String value associated with the get method
	 * @param setter the String value associated with the set method
	 * @return the appropriate String to use as the value for the descriptor
	 */
	private String resolveStringDescriptor(String getter, String setter) {
		return (StringUtils.hasLength(getter) ? getter : setter);
	}

}
