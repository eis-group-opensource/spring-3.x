/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.cache.annotation;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * Abstract base {@code @Configuration} class providing common structure for enabling
 * Spring's annotation-driven cache management capability.
 *
 * @author Chris Beams
 * @since 3.1
 * @see EnableCaching
 */
@Configuration
public abstract class AbstractCachingConfiguration implements ImportAware {

	protected AnnotationAttributes enableCaching;
	protected CacheManager cacheManager;
	protected KeyGenerator keyGenerator;

	@Autowired(required=false)
	private Collection<CacheManager> cacheManagerBeans;
	@Autowired(required=false)
	private Collection<CachingConfigurer> cachingConfigurers;

	public void setImportMetadata(AnnotationMetadata importMetadata) {
		this.enableCaching = AnnotationAttributes.fromMap(
				importMetadata.getAnnotationAttributes(EnableCaching.class.getName(), false));
		Assert.notNull(this.enableCaching,
				"@EnableCaching is not present on importing class " +
				importMetadata.getClassName());
	}

	/**
	 * Determine which {@code CacheManager} bean to use. Prefer the result of
	 * {@link CachingConfigurer#cacheManager()} over any by-type matching. If none, fall
	 * back to by-type matching on {@code CacheManager}.
	 * @throws IllegalArgumentException if no CacheManager can be found; if more than one
	 * CachingConfigurer implementation exists; if multiple CacheManager beans and no
	 * CachingConfigurer exists to disambiguate.
	 */
	@PostConstruct
	protected void reconcileCacheManager() {
		if (!CollectionUtils.isEmpty(cachingConfigurers)) {
			int nConfigurers = cachingConfigurers.size();
			if (nConfigurers > 1) {
				throw new IllegalStateException(nConfigurers + " implementations of " +
						"CachingConfigurer were found when only 1 was expected. " +
						"Refactor the configuration such that CachingConfigurer is " +
						"implemented only once or not at all.");
			}
			CachingConfigurer cachingConfigurer = cachingConfigurers.iterator().next();
			this.cacheManager = cachingConfigurer.cacheManager();
			this.keyGenerator = cachingConfigurer.keyGenerator();
		}
		else if (!CollectionUtils.isEmpty(cacheManagerBeans)) {
			int nManagers = cacheManagerBeans.size();
			if (nManagers > 1) {
				throw new IllegalStateException(nManagers + " beans of type CacheManager " +
						"were found when only 1 was expected. Remove all but one of the " +
						"CacheManager bean definitions, or implement CachingConfigurer " +
						"to make explicit which CacheManager should be used for " +
						"annotation-driven cache management.");
			}
			CacheManager cacheManager = cacheManagerBeans.iterator().next();
			this.cacheManager = cacheManager;
			// keyGenerator remains null; will fall back to default within CacheInterceptor
		}
		else {
			throw new IllegalStateException("No bean of type CacheManager could be found. " +
					"Register a CacheManager bean or remove the @EnableCaching annotation " +
					"from your configuration.");
		}
	}
}
