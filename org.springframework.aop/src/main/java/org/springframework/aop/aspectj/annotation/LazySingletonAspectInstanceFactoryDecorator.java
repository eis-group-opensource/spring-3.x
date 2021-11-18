/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.aspectj.annotation;

import org.springframework.util.Assert;

/**
 * Decorator to cause a {@link MetadataAwareAspectInstanceFactory} to instantiate only once.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 2.0
 */
public class LazySingletonAspectInstanceFactoryDecorator implements MetadataAwareAspectInstanceFactory {

	private final MetadataAwareAspectInstanceFactory maaif;

	private volatile Object materialized;


	/**
	 * Create a new lazily initializing decorator for the given AspectInstanceFactory.
	 * @param maaif the MetadataAwareAspectInstanceFactory to decorate
	 */
	public LazySingletonAspectInstanceFactoryDecorator(MetadataAwareAspectInstanceFactory maaif) {
		Assert.notNull(maaif, "AspectInstanceFactory must not be null");
		this.maaif = maaif;
	}


	public synchronized Object getAspectInstance() {
		if (this.materialized == null) {
			synchronized (this) {
				if (this.materialized == null) {
					this.materialized = this.maaif.getAspectInstance();
				}
			}
		}
		return this.materialized;
	}

	public boolean isMaterialized() {
		return (this.materialized != null);
	}

	public ClassLoader getAspectClassLoader() {
		return this.maaif.getAspectClassLoader();
	}

	public AspectMetadata getAspectMetadata() {
		return this.maaif.getAspectMetadata();
	}

	public int getOrder() {
		return this.maaif.getOrder();
	}


	@Override
	public String toString() {
		return "LazySingletonAspectInstanceFactoryDecorator: decorating " + this.maaif;
	}

}
