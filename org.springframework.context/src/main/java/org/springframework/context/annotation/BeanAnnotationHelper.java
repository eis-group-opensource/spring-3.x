/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation;

import java.lang.reflect.Method;

import org.springframework.core.annotation.AnnotationUtils;

/**
 * Utilities for processing {@link Bean}-annotated methods.
 *
 * @author Chris Beams
 * @since 3.1
 */
class BeanAnnotationHelper {

	/**
	 * Return whether the given method is annotated directly or indirectly with @Bean.
	 */
	public static boolean isBeanAnnotated(Method method) {
		return AnnotationUtils.findAnnotation(method, Bean.class) != null;
	}

	public static String determineBeanNameFor(Method beanMethod) {
		// by default the bean name is the name of the @Bean-annotated method
		String beanName = beanMethod.getName();

		// check to see if the user has explicitly set the bean name
		Bean bean = AnnotationUtils.findAnnotation(beanMethod, Bean.class);
		if (bean != null && bean.name().length > 0) {
			beanName = bean.name()[0];
		}

		return beanName;
	}

}
