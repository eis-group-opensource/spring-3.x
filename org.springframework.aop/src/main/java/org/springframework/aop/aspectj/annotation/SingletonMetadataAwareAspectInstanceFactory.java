/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.aspectj.annotation;

import org.springframework.aop.aspectj.SingletonAspectInstanceFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * Implementation of {@link MetadataAwareAspectInstanceFactory} that is backed
 * by a specified singleton object, returning the same instance for every
 * {@link #getAspectInstance()} call.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 2.0
 * @see SimpleMetadataAwareAspectInstanceFactory
 */
public class SingletonMetadataAwareAspectInstanceFactory extends SingletonAspectInstanceFactory
		implements MetadataAwareAspectInstanceFactory {

	private final AspectMetadata metadata;


	/**
	 * Create a new SingletonMetadataAwareAspectInstanceFactory for the given aspect.
	 * @param aspectInstance the singleton aspect instance
	 * @param aspectName the name of the aspect
	 */
	public SingletonMetadataAwareAspectInstanceFactory(Object aspectInstance, String aspectName) {
		super(aspectInstance);
		this.metadata = new AspectMetadata(aspectInstance.getClass(), aspectName);
	}


	public final AspectMetadata getAspectMetadata() {
		return this.metadata;
	}

	/**
	 * Check whether the aspect class carries an
	 * {@link org.springframework.core.annotation.Order} annotation,
	 * falling back to <code>Ordered.LOWEST_PRECEDENCE</code>.
	 * @see org.springframework.core.annotation.Order
	 */
	@Override
	protected int getOrderForAspectClass(Class<?> aspectClass) {
		Order order = aspectClass.getAnnotation(Order.class);
		if (order != null) {
			return order.value();
		}
		return Ordered.LOWEST_PRECEDENCE;
	}

}
