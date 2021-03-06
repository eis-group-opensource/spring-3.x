/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.dao.support;

import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * AOP Alliance MethodInterceptor that provides persistence exception translation
 * based on a given PersistenceExceptionTranslator.
 *
 * <p>Delegates to the given {@link PersistenceExceptionTranslator} to translate
 * a RuntimeException thrown into Spring's DataAccessException hierarchy
 * (if appropriate). If the RuntimeException in question is declared on the
 * target method, it is always propagated as-is (with no translation applied).
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 2.0
 * @see PersistenceExceptionTranslator
 */
public class PersistenceExceptionTranslationInterceptor
		implements MethodInterceptor, BeanFactoryAware, InitializingBean {

	private PersistenceExceptionTranslator persistenceExceptionTranslator;

	private boolean alwaysTranslate = false;


	/**
	 * Create a new PersistenceExceptionTranslationInterceptor.
	 * Needs to be configured with a PersistenceExceptionTranslator afterwards.
	 * @see #setPersistenceExceptionTranslator
	 */
	public PersistenceExceptionTranslationInterceptor() {
	}

	/**
	 * Create a new PersistenceExceptionTranslationInterceptor
	 * for the given PersistenceExceptionTranslator.
	 * @param persistenceExceptionTranslator the PersistenceExceptionTranslator to use
	 */
	public PersistenceExceptionTranslationInterceptor(PersistenceExceptionTranslator persistenceExceptionTranslator) {
		setPersistenceExceptionTranslator(persistenceExceptionTranslator);
	}

	/**
	 * Create a new PersistenceExceptionTranslationInterceptor, autodetecting
	 * PersistenceExceptionTranslators in the given BeanFactory.
	 * @param beanFactory the ListableBeanFactory to obtaining all
	 * PersistenceExceptionTranslators from
	 */
	public PersistenceExceptionTranslationInterceptor(ListableBeanFactory beanFactory) {
		this.persistenceExceptionTranslator = detectPersistenceExceptionTranslators(beanFactory);
	}


	/**
	 * Specify the PersistenceExceptionTranslator to use.
	 * <p>Default is to autodetect all PersistenceExceptionTranslators
	 * in the containing BeanFactory, using them in a chain.
	 * @see #detectPersistenceExceptionTranslators
	 */
	public void setPersistenceExceptionTranslator(PersistenceExceptionTranslator pet) {
		Assert.notNull(pet, "PersistenceExceptionTranslator must not be null");
		this.persistenceExceptionTranslator = pet;
	}

	/**
	 * Specify whether to always translate the exception ("true"), or whether throw the
	 * raw exception when declared, i.e. when the originating method signature's exception
	 * declarations allow for the raw exception to be thrown ("false").
	 * <p>Default is "false". Switch this flag to "true" in order to always translate
	 * applicable exceptions, independent from the originating method signature.
	 * <p>Note that the originating method does not have to declare the specific exception.
	 * Any base class will do as well, even <code>throws Exception</code>: As long as the
	 * originating method does explicitly declare compatible exceptions, the raw exception
	 * will be rethrown. If you would like to avoid throwing raw exceptions in any case,
	 * switch this flag to "true".
	 */
	public void setAlwaysTranslate(boolean alwaysTranslate) {
		this.alwaysTranslate = alwaysTranslate;
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		if (this.persistenceExceptionTranslator == null) {
			// No explicit exception translator specified - perform autodetection.
			if (!(beanFactory instanceof ListableBeanFactory)) {
				throw new IllegalArgumentException(
						"Cannot use PersistenceExceptionTranslator autodetection without ListableBeanFactory");
			}
			this.persistenceExceptionTranslator =
					detectPersistenceExceptionTranslators((ListableBeanFactory) beanFactory);
		}
	}

	public void afterPropertiesSet() {
		if (this.persistenceExceptionTranslator == null) {
			throw new IllegalArgumentException("Property 'persistenceExceptionTranslator' is required");
		}
	}


	/**
	 * Detect all PersistenceExceptionTranslators in the given BeanFactory.
	 * @param beanFactory the ListableBeanFactory to obtaining all
	 * PersistenceExceptionTranslators from
	 * @return a chained PersistenceExceptionTranslator, combining all
	 * PersistenceExceptionTranslators found in the factory
	 * @see ChainedPersistenceExceptionTranslator
	 */
	protected PersistenceExceptionTranslator detectPersistenceExceptionTranslators(ListableBeanFactory beanFactory) {
		// Find all translators, being careful not to activate FactoryBeans.
		Map<String, PersistenceExceptionTranslator> pets = BeanFactoryUtils.beansOfTypeIncludingAncestors(
				beanFactory, PersistenceExceptionTranslator.class, false, false);
		if (pets.isEmpty()) {
			throw new IllegalStateException(
					"No persistence exception translators found in bean factory. Cannot perform exception translation.");
		}
		ChainedPersistenceExceptionTranslator cpet = new ChainedPersistenceExceptionTranslator();
		for (PersistenceExceptionTranslator pet : pets.values()) {
			cpet.addDelegate(pet);
		}
		return cpet;
	}


	public Object invoke(MethodInvocation mi) throws Throwable {
		try {
			return mi.proceed();
		}
		catch (RuntimeException ex) {
			// Let it throw raw if the type of the exception is on the throws clause of the method.
			if (!this.alwaysTranslate && ReflectionUtils.declaresException(mi.getMethod(), ex.getClass())) {
				throw ex;
			}
			else {
				throw DataAccessUtils.translateIfNecessary(ex, this.persistenceExceptionTranslator);
			}
		}
	}

}
