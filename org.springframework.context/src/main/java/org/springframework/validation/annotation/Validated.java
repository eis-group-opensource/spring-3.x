/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Variant of JSR-303's {@link javax.validation.Valid}, supporting the
 * specification of validation groups. Designed for convenient use with
 * Spring's JSR-303 support but not JSR-303 specific.
 *
 * <p>Can be used e.g. with Spring MVC handler methods arguments.
 * Supported through {@link org.springframework.validation.SmartValidator}'s
 * validation hint concept, with validation group classes acting as hint objects.
 *
 * <p>Can also be used with method level validation, indicating that a specific
 * class is supposed to be validated at the method level (acting as a pointcut
 * for the corresponding validation interceptor), but also optionally specifying
 * the validation groups for method-level validation in the annotated class.
 * Can also be used as a meta-annotation on a custom stereotype annotation.
 *
 * @author Juergen Hoeller
 * @since 3.1
 * @see javax.validation.Validator#validate(Object, Class[])
 * @see org.springframework.validation.SmartValidator#validate(Object, org.springframework.validation.Errors, Object...)
 * @see org.springframework.validation.beanvalidation.SpringValidatorAdapter
 * @see org.springframework.validation.beanvalidation.MethodValidationPostProcessor
 */
@Target({ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Validated {

	/**
	 * Specify one or more validation groups to apply to the validation step
	 * kicked off by this annotation.
	 * <p>JSR-303 defines validation groups as custom annotations which an application declares
	 * for the sole purpose of using them as type-safe group arguments, as implemented in
	 * {@link org.springframework.validation.beanvalidation.SpringValidatorAdapter}.
	 * <p>Other {@link org.springframework.validation.SmartValidator} implementations may
	 * support class arguments in other ways as well.
	 */
	Class<?>[] value() default {};

}