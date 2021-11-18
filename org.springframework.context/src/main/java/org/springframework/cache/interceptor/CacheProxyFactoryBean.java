/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.cache.interceptor;

import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.AbstractSingletonProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;

/**
 * Proxy factory bean for simplified declarative caching handling.
 * This is a convenient alternative to a standard AOP
 * {@link org.springframework.aop.framework.ProxyFactoryBean}
 * with a separate {@link CachingInterceptor} definition.
 *
 * <p>This class is designed to facilitate declarative cache demarcation: namely, wrapping
 * a singleton target object with a caching proxy, proxying all the interfaces that the
 * target implements. Exists primarily for third-party framework integration.
 * <strong>Users should favor the {@code cache:} XML namespace
 * {@link org.springframework.cache.annotation.Cacheable @Cacheable} annotation.</strong>
 * See the <a href="http://bit.ly/p9rIvx">declarative annotation-based caching</a> section
 * of the Spring reference documentation for more information.
 *
 * @author Costin Leau
 * @see org.springframework.aop.framework.ProxyFactoryBean
 * @see CachingInterceptor
 */
@SuppressWarnings("serial")
public class CacheProxyFactoryBean extends AbstractSingletonProxyFactoryBean {

	private final CacheInterceptor cachingInterceptor = new CacheInterceptor();
	private Pointcut pointcut;

	/**
	 * Set a pointcut, i.e a bean that can cause conditional invocation
	 * of the CacheInterceptor depending on method and attributes passed.
	 * Note: Additional interceptors are always invoked.
	 * @see #setPreInterceptors
	 * @see #setPostInterceptors
	 */
	public void setPointcut(Pointcut pointcut) {
		this.pointcut = pointcut;
	}

	@Override
	protected Object createMainInterceptor() {
		this.cachingInterceptor.afterPropertiesSet();
		if (this.pointcut != null) {
			return new DefaultPointcutAdvisor(this.pointcut, this.cachingInterceptor);
		} else {
			// Rely on default pointcut.
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * Set the sources used to find cache operations.
	 */
	public void setCacheOperationSources(CacheOperationSource... cacheOperationSources) {
		this.cachingInterceptor.setCacheOperationSources(cacheOperationSources);
	}

}
