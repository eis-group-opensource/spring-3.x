/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.wiring;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.util.Assert;

/**
 * Holder for bean wiring metadata information about a particular class. Used in
 * conjunction with the {@link org.springframework.beans.factory.annotation.Configurable}
 * annotation and the AspectJ <code>AnnotationBeanConfigurerAspect</code>.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 2.0
 * @see BeanWiringInfoResolver
 * @see org.springframework.beans.factory.config.AutowireCapableBeanFactory
 * @see org.springframework.beans.factory.annotation.Configurable
 */
public class BeanWiringInfo {

	/**
	 * Constant that indicates autowiring bean properties by name.
	 * @see #BeanWiringInfo(int, boolean)
	 * @see org.springframework.beans.factory.config.AutowireCapableBeanFactory#AUTOWIRE_BY_NAME
	 */
	public static final int AUTOWIRE_BY_NAME = AutowireCapableBeanFactory.AUTOWIRE_BY_NAME;

	/**
	 * Constant that indicates autowiring bean properties by type.
	 * @see #BeanWiringInfo(int, boolean)
	 * @see org.springframework.beans.factory.config.AutowireCapableBeanFactory#AUTOWIRE_BY_TYPE
	 */
	public static final int AUTOWIRE_BY_TYPE = AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE;


	private String beanName = null;

	private boolean isDefaultBeanName = false;

	private int autowireMode = AutowireCapableBeanFactory.AUTOWIRE_NO;

	private boolean dependencyCheck = false;


	/**
	 * Create a default BeanWiringInfo that suggests plain initialization of
	 * factory and post-processor callbacks that the bean class may expect.
	 */
	public BeanWiringInfo() {
	}

	/**
	 * Create a new BeanWiringInfo that points to the given bean name.
	 * @param beanName the name of the bean definition to take the property values from
	 * @throws IllegalArgumentException if the supplied beanName is <code>null</code>,
	 * is empty, or consists wholly of whitespace
	 */
	public BeanWiringInfo(String beanName) {
		this(beanName, false);
	}

	/**
	 * Create a new BeanWiringInfo that points to the given bean name.
	 * @param beanName the name of the bean definition to take the property values from
	 * @param isDefaultBeanName whether the given bean name is a suggested
	 * default bean name, not necessarily matching an actual bean definition
	 * @throws IllegalArgumentException if the supplied beanName is <code>null</code>,
	 * is empty, or consists wholly of whitespace
	 */
	public BeanWiringInfo(String beanName, boolean isDefaultBeanName) {
		Assert.hasText(beanName, "'beanName' must not be empty");
		this.beanName = beanName;
		this.isDefaultBeanName = isDefaultBeanName;
	}

	/**
	 * Create a new BeanWiringInfo that indicates autowiring.
	 * @param autowireMode one of the constants {@link #AUTOWIRE_BY_NAME} /
	 * {@link #AUTOWIRE_BY_TYPE}
	 * @param dependencyCheck whether to perform a dependency check for object
	 * references in the bean instance (after autowiring)
	 * @throws IllegalArgumentException if the supplied <code>autowireMode</code>
	 * is not one of the allowed values
	 * @see #AUTOWIRE_BY_NAME
	 * @see #AUTOWIRE_BY_TYPE
	 */
	public BeanWiringInfo(int autowireMode, boolean dependencyCheck) {
		if (autowireMode != AUTOWIRE_BY_NAME && autowireMode != AUTOWIRE_BY_TYPE) {
			throw new IllegalArgumentException("Only constants AUTOWIRE_BY_NAME and AUTOWIRE_BY_TYPE supported");
		}
		this.autowireMode = autowireMode;
		this.dependencyCheck = dependencyCheck;
	}


	/**
	 * Return whether this BeanWiringInfo indicates autowiring.
	 */
	public boolean indicatesAutowiring() {
		return (this.beanName == null);
	}

	/**
	 * Return the specific bean name that this BeanWiringInfo points to, if any.
	 */
	public String getBeanName() {
		return this.beanName;
	}

	/**
	 * Return whether the specific bean name is a suggested default bean name,
	 * not necessarily matching an actual bean definition in the factory.
	 */
	public boolean isDefaultBeanName() {
		return this.isDefaultBeanName;
	}

	/**
	 * Return one of the constants {@link #AUTOWIRE_BY_NAME} /
	 * {@link #AUTOWIRE_BY_TYPE}, if autowiring is indicated.
	 */
	public int getAutowireMode() {
		return this.autowireMode;
	}

	/**
	 * Return whether to perform a dependency check for object references
	 * in the bean instance (after autowiring).
	 */
	public boolean getDependencyCheck() {
		return this.dependencyCheck;
	}

}
