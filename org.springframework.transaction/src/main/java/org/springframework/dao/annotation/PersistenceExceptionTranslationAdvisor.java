/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.dao.annotation;

import java.lang.annotation.Annotation;

import org.aopalliance.aop.Advice;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.dao.support.PersistenceExceptionTranslationInterceptor;
import org.springframework.dao.support.PersistenceExceptionTranslator;

/**
 * Spring AOP exception translation aspect for use at Repository or DAO layer level.
 * Translates native persistence exceptions into Spring's DataAccessException hierarchy,
 * based on a given PersistenceExceptionTranslator.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 2.0
 * @see org.springframework.dao.DataAccessException
 * @see org.springframework.dao.support.PersistenceExceptionTranslator
 */
public class PersistenceExceptionTranslationAdvisor extends AbstractPointcutAdvisor {

	private final PersistenceExceptionTranslationInterceptor advice;

	private final AnnotationMatchingPointcut pointcut;


	/**
	 * Create a new PersistenceExceptionTranslationAdvisor.
	 * @param persistenceExceptionTranslator the PersistenceExceptionTranslator to use
	 * @param repositoryAnnotationType the annotation type to check for
	 */
	public PersistenceExceptionTranslationAdvisor(
			PersistenceExceptionTranslator persistenceExceptionTranslator,
			Class<? extends Annotation> repositoryAnnotationType) {

		this.advice = new PersistenceExceptionTranslationInterceptor(persistenceExceptionTranslator);
		this.pointcut = new AnnotationMatchingPointcut(repositoryAnnotationType, true);
	}

	/**
	 * Create a new PersistenceExceptionTranslationAdvisor.
	 * @param beanFactory the ListableBeanFactory to obtaining all
	 * PersistenceExceptionTranslators from
	 * @param repositoryAnnotationType the annotation type to check for
	 */
	PersistenceExceptionTranslationAdvisor(
			ListableBeanFactory beanFactory, Class<? extends Annotation> repositoryAnnotationType) {

		this.advice = new PersistenceExceptionTranslationInterceptor(beanFactory);
		this.pointcut = new AnnotationMatchingPointcut(repositoryAnnotationType, true);
	}


	public Advice getAdvice() {
		return this.advice;
	}

	public Pointcut getPointcut() {
		return this.pointcut;
	}

}
