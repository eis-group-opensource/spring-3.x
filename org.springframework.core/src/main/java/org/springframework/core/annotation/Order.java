/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.Ordered;

/**
 * Annotation that defines ordering. The value is optional, and represents order value
 * as defined in the {@link Ordered} interface. Lower values have higher priority.
 * The default value is <code>Ordered.LOWEST_PRECEDENCE</code>, indicating
 * lowest priority (losing to any other specified order value).
 *
 * <p><b>NOTE:</b> Annotation-based ordering is supported for specific kinds of
 * components only, e.g. for annotation-based AspectJ aspects. Spring container
 * strategies, on the other hand, are typically based on the {@link Ordered}
 * interface in order to allow for configurable ordering of each <i>instance</i>.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 2.0
 * @see org.springframework.core.Ordered
 * @see AnnotationAwareOrderComparator
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface Order {

	/**
	 * The order value. Default is {@link Ordered#LOWEST_PRECEDENCE}.
	 * @see Ordered#getOrder()
	 */
	int value() default Ordered.LOWEST_PRECEDENCE;

}
