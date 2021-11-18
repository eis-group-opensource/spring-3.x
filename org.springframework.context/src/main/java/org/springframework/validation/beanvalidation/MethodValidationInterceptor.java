/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.validation.beanvalidation;

import java.util.Set;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.method.MethodConstraintViolation;
import org.hibernate.validator.method.MethodConstraintViolationException;
import org.hibernate.validator.method.MethodValidator;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.validation.annotation.Validated;

/**
 * An AOP Alliance {@link MethodInterceptor} implementation that delegates to a
 * JSR-303 provider for performing method-level validation on annotated methods.
 *
 * <p>Applicable methods have JSR-303 constraint annotations on their parameters
 * and/or on their return value (in the latter case specified at the method level,
 * typically as inline annotation).
 *
 * <p>E.g.: <code>public @NotNull Object myValidMethod(@NotNull String arg1, @Max(10) int arg2)</code>
 *
 * <p>Validation groups can be specified through Spring's {@link Validated} annotation
 * at the type level of the containing target class, applying to all public service methods
 * of that class. By default, JSR-303 will validate against its default group only.
 *
 * <p>As of Spring 3.1, this functionality requires Hibernate Validator 4.2 or higher.
 * In Spring 3.2, this class will autodetect a Bean Validation 1.1 compliant provider
 * and automatically use the standard method validation support there (once available).
 *
 * @author Juergen Hoeller
 * @since 3.1
 * @see MethodValidationPostProcessor
 * @see org.hibernate.validator.method.MethodValidator
 */
public class MethodValidationInterceptor implements MethodInterceptor {

	private final MethodValidator validator;


	/**
	 * Create a new MethodValidationInterceptor using a default JSR-303 validator underneath.
	 */
	public MethodValidationInterceptor() {
		this(Validation.byProvider(HibernateValidator.class).configure().buildValidatorFactory());
	}

	/**
	 * Create a new MethodValidationInterceptor using the given JSR-303 ValidatorFactory.
	 * @param validatorFactory the JSR-303 ValidatorFactory to use
	 */
	public MethodValidationInterceptor(ValidatorFactory validatorFactory) {
		this(validatorFactory.getValidator());
	}

	/**
	 * Create a new MethodValidationInterceptor using the given JSR-303 Validator.
	 * @param validatorFactory the JSR-303 Validator to use
	 */
	public MethodValidationInterceptor(Validator validator) {
		this.validator = validator.unwrap(MethodValidator.class);
	}


	public Object invoke(MethodInvocation invocation) throws Throwable {
		Class[] groups = determineValidationGroups(invocation);
		Set<MethodConstraintViolation<Object>> result = this.validator.validateAllParameters(
				invocation.getThis(), invocation.getMethod(), invocation.getArguments(), groups);
		if (!result.isEmpty()) {
			throw new MethodConstraintViolationException(result);
		}
		Object returnValue = invocation.proceed();
		result = this.validator.validateReturnValue(
				invocation.getThis(), invocation.getMethod(), returnValue, groups);
		if (!result.isEmpty()) {
			throw new MethodConstraintViolationException(result);
		}
		return returnValue;
	}

	/**
	 * Determine the validation groups to validate against for the given method invocation.
	 * <p>Default are the validation groups as specified in the {@link Validated} annotation
	 * on the containing target class of the method.
	 * @param invocation the current MethodInvocation
	 * @return the applicable validation groups as a Class array
	 */
	protected Class[] determineValidationGroups(MethodInvocation invocation) {
		Validated valid = AnnotationUtils.findAnnotation(invocation.getThis().getClass(), Validated.class);
		return (valid != null ? valid.value() : new Class[0]);
	}

}
