/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scheduling.annotation;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Executor;

import org.aopalliance.aop.Advice;

import org.springframework.aop.Pointcut;
import org.springframework.aop.interceptor.AsyncExecutionInterceptor;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.util.Assert;

/**
 * Advisor that activates asynchronous method execution through the {@link Async}
 * annotation. This annotation can be used at the method and type level in
 * implementation classes as well as in service interfaces.
 *
 * <p>This advisor detects the EJB 3.1 <code>javax.ejb.Asynchronous</code>
 * annotation as well, treating it exactly like Spring's own <code>Async</code>.
 * Furthermore, a custom async annotation type may get specified through the
 * {@link #setAsyncAnnotationType "asyncAnnotationType"} property.
 *
 * @author Juergen Hoeller
 * @since 3.0
 * @see PersistenceExceptionTranslationAdvisor
 * @see org.springframework.stereotype.Repository
 * @see org.springframework.dao.DataAccessException
 * @see org.springframework.dao.support.PersistenceExceptionTranslator
 */
public class AsyncAnnotationAdvisor extends AbstractPointcutAdvisor {

	private Advice advice;

	private Pointcut pointcut;


	/**
	 * Create a new ConcurrencyAnnotationBeanPostProcessor for bean-style configuration.
	 */
	public AsyncAnnotationAdvisor() {
		this(new SimpleAsyncTaskExecutor());
	}

	/**
	 * Create a new ConcurrencyAnnotationBeanPostProcessor for the given task executor.
	 * @param executor the task executor to use for asynchronous methods
	 */
	@SuppressWarnings("unchecked")
	public AsyncAnnotationAdvisor(Executor executor) {
		Set<Class<? extends Annotation>> asyncAnnotationTypes = new LinkedHashSet<Class<? extends Annotation>>(2);
		asyncAnnotationTypes.add(Async.class);
		ClassLoader cl = AsyncAnnotationAdvisor.class.getClassLoader();
		try {
			asyncAnnotationTypes.add((Class) cl.loadClass("javax.ejb.Asynchronous"));
		}
		catch (ClassNotFoundException ex) {
			// If EJB 3.1 API not present, simply ignore.
		}
		this.advice = buildAdvice(executor);
		this.pointcut = buildPointcut(asyncAnnotationTypes);
	}

	/**
	 * Specify the task executor to use for asynchronous methods.
	 */
	public void setTaskExecutor(Executor executor) {
		this.advice = buildAdvice(executor);
	}

	/**
	 * Set the 'async' annotation type.
	 * <p>The default async annotation type is the {@link Async} annotation, as well
	 * as the EJB 3.1 <code>javax.ejb.Asynchronous</code> annotation (if present).
	 * <p>This setter property exists so that developers can provide their own
	 * (non-Spring-specific) annotation type to indicate that a method is to
	 * be executed asynchronously.
	 * @param asyncAnnotationType the desired annotation type
	 */
	public void setAsyncAnnotationType(Class<? extends Annotation> asyncAnnotationType) {
		Assert.notNull(asyncAnnotationType, "'asyncAnnotationType' must not be null");
		Set<Class<? extends Annotation>> asyncAnnotationTypes = new HashSet<Class<? extends Annotation>>();
		asyncAnnotationTypes.add(asyncAnnotationType);
		this.pointcut = buildPointcut(asyncAnnotationTypes);
	}


	public Advice getAdvice() {
		return this.advice;
	}

	public Pointcut getPointcut() {
		return this.pointcut;
	}


	protected Advice buildAdvice(Executor executor) {
		if (executor instanceof AsyncTaskExecutor) {
			return new AsyncExecutionInterceptor((AsyncTaskExecutor) executor);
		}
		else {
			return new AsyncExecutionInterceptor(executor);
		}
	}

	/**
	 * Calculate a pointcut for the given target class, if any.
	 * @param targetClass the class to introspect
	 * @return the applicable Pointcut object, or <code>null</code> if none
	 */
	protected Pointcut buildPointcut(Set<Class<? extends Annotation>> asyncAnnotationTypes) {
		ComposablePointcut result = null;
		for (Class<? extends Annotation> asyncAnnotationType : asyncAnnotationTypes) {
			Pointcut cpc = new AnnotationMatchingPointcut(asyncAnnotationType, true);
			Pointcut mpc = new AnnotationMatchingPointcut(null, asyncAnnotationType);
			if (result == null) {
				result = new ComposablePointcut(cpc).union(mpc);
			}
			else {
				result.union(cpc).union(mpc);
			}
		}
		return result;
	}

}
