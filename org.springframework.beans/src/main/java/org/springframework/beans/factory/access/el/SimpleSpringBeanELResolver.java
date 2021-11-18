/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.access.el;

import javax.el.ELContext;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.util.Assert;

/**
 * Simple concrete variant of {@link SpringBeanELResolver}, delegating
 * to a given {@link BeanFactory} that the resolver was constructed with.
 *
 * @author Juergen Hoeller
 * @since 2.5.2
 */
public class SimpleSpringBeanELResolver extends SpringBeanELResolver {

	private final BeanFactory beanFactory;


	/**
	 * Create a new SimpleSpringBeanELResolver for the given BeanFactory.
	 * @param beanFactory the Spring BeanFactory to delegate to
	 */
	public SimpleSpringBeanELResolver(BeanFactory beanFactory) {
		Assert.notNull(beanFactory, "BeanFactory must not be null");
		this.beanFactory = beanFactory;
	}

	@Override
	protected BeanFactory getBeanFactory(ELContext elContext) {
		return this.beanFactory;
	}

}
