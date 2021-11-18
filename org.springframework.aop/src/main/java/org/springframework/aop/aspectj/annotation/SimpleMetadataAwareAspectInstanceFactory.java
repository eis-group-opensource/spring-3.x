/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.aspectj.annotation;

import org.springframework.aop.aspectj.SimpleAspectInstanceFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * Implementation of {@link MetadataAwareAspectInstanceFactory} that
 * creates a new instance of the specified aspect class for every
 * {@link #getAspectInstance()} call.
 *
 * @author Juergen Hoeller
 * @since 2.0.4
 */
public class SimpleMetadataAwareAspectInstanceFactory extends SimpleAspectInstanceFactory
		implements MetadataAwareAspectInstanceFactory {

	private final AspectMetadata metadata;


	/**
	 * Create a new SimpleMetadataAwareAspectInstanceFactory for the given aspect class.
	 * @param aspectClass the aspect class
	 * @param aspectName the aspect name
	 */
	public SimpleMetadataAwareAspectInstanceFactory(Class aspectClass, String aspectName) {
		super(aspectClass);
		this.metadata = new AspectMetadata(aspectClass, aspectName);
	}


	public final AspectMetadata getAspectMetadata() {
		return this.metadata;
	}

	/**
	 * Determine a fallback order for the case that the aspect instance
	 * does not express an instance-specific order through implementing
	 * the {@link org.springframework.core.Ordered} interface.
	 * <p>The default implementation simply returns <code>Ordered.LOWEST_PRECEDENCE</code>.
	 * @param aspectClass the aspect class
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
