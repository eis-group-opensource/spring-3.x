/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.annotation;

import org.springframework.beans.factory.wiring.BeanWiringInfo;
import org.springframework.beans.factory.wiring.BeanWiringInfoResolver;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * {@link org.springframework.beans.factory.wiring.BeanWiringInfoResolver} that
 * uses the Configurable annotation to identify which classes need autowiring.
 * The bean name to look up will be taken from the {@link Configurable} annotation
 * if specified; otherwise the default will be the fully-qualified name of the
 * class being configured.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 2.0
 * @see Configurable
 * @see org.springframework.beans.factory.wiring.ClassNameBeanWiringInfoResolver
 */
public class AnnotationBeanWiringInfoResolver implements BeanWiringInfoResolver {

	public BeanWiringInfo resolveWiringInfo(Object beanInstance) {
		Assert.notNull(beanInstance, "Bean instance must not be null");
		Configurable annotation = beanInstance.getClass().getAnnotation(Configurable.class);
		return (annotation != null ? buildWiringInfo(beanInstance, annotation) : null);
	}

	/**
	 * Build the BeanWiringInfo for the given Configurable annotation.
	 * @param beanInstance the bean instance
	 * @param annotation the Configurable annotation found on the bean class
	 * @return the resolved BeanWiringInfo
	 */
	protected BeanWiringInfo buildWiringInfo(Object beanInstance, Configurable annotation) {
		if (!Autowire.NO.equals(annotation.autowire())) {
			return new BeanWiringInfo(annotation.autowire().value(), annotation.dependencyCheck());
		}
		else {
			if (!"".equals(annotation.value())) {
				// explicitly specified bean name
				return new BeanWiringInfo(annotation.value(), false);
			}
			else {
				// default bean name
				return new BeanWiringInfo(getDefaultBeanName(beanInstance), true);
			}
		}
	}

	/**
	 * Determine the default bean name for the specified bean instance.
	 * <p>The default implementation returns the superclass name for a CGLIB
	 * proxy and the name of the plain bean class else.
	 * @param beanInstance the bean instance to build a default name for
	 * @return the default bean name to use
	 * @see org.springframework.util.ClassUtils#getUserClass(Class)
	 */
	protected String getDefaultBeanName(Object beanInstance) {
		return ClassUtils.getUserClass(beanInstance).getName();
	}

}
