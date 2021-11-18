/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates one or more @{@link Configuration} classes to import.
 *
 * <p>Provides functionality equivalent to the {@code <import/>} element in Spring XML.
 * Only supported for classes annotated with {@code @Configuration} or declaring at least
 * one {@link @Bean} method, as well as {@link ImportSelector} and
 * {@link ImportBeanDefinitionRegistrar} implementations.
 *
 * <p>@{@code Bean} definitions declared in imported {@code @Configuration} classes
 * should be accessed by using @{@link Autowired} injection.  Either the bean itself can
 * be autowired, or the configuration class instance declaring the bean can be autowired.
 * The latter approach allows for explicit, IDE-friendly navigation between
 * {@code @Configuration} class methods.
 *
 * <p>May be declared at the class level or as a meta-annotation.
 *
 * <p>If XML or other non-{@code @Configuration} bean definition resources need to be
 * imported, use @{@link ImportResource}
 *
 * @author Chris Beams
 * @since 3.0
 * @see Configuration
 * @see ImportSelector
 * @see ImportResource
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Import {

	/**
	 * The @{@link Configuration} and/or {@link ImportSelector} classes to import.
	 */
	Class<?>[] value();
}
