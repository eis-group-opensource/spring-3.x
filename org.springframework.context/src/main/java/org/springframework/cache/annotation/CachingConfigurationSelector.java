/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.cache.annotation;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.AutoProxyRegistrar;

/**
 * Selects which implementation of {@link AbstractCachingConfiguration} should be used
 * based on the value of {@link EnableCaching#mode} on the importing {@code @Configuration}
 * class.
 *
 * @author Chris Beams
 * @since 3.1
 * @see EnableCaching
 * @see ProxyCachingConfiguration
 * @see AnnotationConfigUtils.CACHE_ASPECT_CONFIGURATION_CLASS_NAME
 */
public class CachingConfigurationSelector extends AdviceModeImportSelector<EnableCaching> {

	/**
	 * {@inheritDoc}
	 * @return {@link ProxyCachingConfiguration} or {@code AspectJCacheConfiguration} for
	 * {@code PROXY} and {@code ASPECTJ} values of {@link EnableCaching#mode()}, respectively
	 */
	public String[] selectImports(AdviceMode adviceMode) {
		switch (adviceMode) {
			case PROXY:
				return new String[] { AutoProxyRegistrar.class.getName(), ProxyCachingConfiguration.class.getName() };
			case ASPECTJ:
				return new String[] { AnnotationConfigUtils.CACHE_ASPECT_CONFIGURATION_CLASS_NAME };
			default:
				return null;
		}
	}

}
