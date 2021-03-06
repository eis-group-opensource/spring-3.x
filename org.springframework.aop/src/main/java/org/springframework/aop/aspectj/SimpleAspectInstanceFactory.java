/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.aspectj;

import org.springframework.aop.framework.AopConfigException;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;

/**
 * Implementation of {@link AspectInstanceFactory} that creates a new instance
 * of the specified aspect class for every {@link #getAspectInstance()} call.
 *
 * @author Juergen Hoeller
 * @since 2.0.4
 */
public class SimpleAspectInstanceFactory implements AspectInstanceFactory {

	private final Class aspectClass;


	/**
	 * Create a new SimpleAspectInstanceFactory for the given aspect class.
	 * @param aspectClass the aspect class
	 */
	public SimpleAspectInstanceFactory(Class aspectClass) {
		Assert.notNull(aspectClass, "Aspect class must not be null");
		this.aspectClass = aspectClass;
	}

	/**
	 * Return the specified aspect class (never <code>null</code>).
	 */
	public final Class getAspectClass() {
		return this.aspectClass;
	}


	public final Object getAspectInstance() {
		try {
			return this.aspectClass.newInstance();
		}
		catch (InstantiationException ex) {
			throw new AopConfigException("Unable to instantiate aspect class [" + this.aspectClass.getName() + "]", ex);
		}
		catch (IllegalAccessException ex) {
			throw new AopConfigException("Cannot access element class [" + this.aspectClass.getName() + "]", ex);
		}
	}

	public ClassLoader getAspectClassLoader() {
		return this.aspectClass.getClassLoader();
	}

	/**
	 * Determine the order for this factory's aspect instance,
	 * either an instance-specific order expressed through implementing
	 * the {@link org.springframework.core.Ordered} interface,
	 * or a fallback order.
	 * @see org.springframework.core.Ordered
	 * @see #getOrderForAspectClass
	 */
	public int getOrder() {
		return getOrderForAspectClass(this.aspectClass);
	}

	/**
	 * Determine a fallback order for the case that the aspect instance
	 * does not express an instance-specific order through implementing
	 * the {@link org.springframework.core.Ordered} interface.
	 * <p>The default implementation simply returns <code>Ordered.LOWEST_PRECEDENCE</code>.
	 * @param aspectClass the aspect class
	 */
	protected int getOrderForAspectClass(Class<?> aspectClass) {
		return Ordered.LOWEST_PRECEDENCE;
	}

}
