/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.framework;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.aop.Advisor;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.util.ClassUtils;

/**
 * Base class for {@link BeanPostProcessor} implementations that apply a
 * Spring AOP {@link Advisor} to specific beans.
 *
 * @author Juergen Hoeller
 * @since 3.2
 */
public abstract class AbstractAdvisingBeanPostProcessor extends ProxyConfig
		implements BeanPostProcessor, BeanClassLoaderAware, Ordered {

	protected Advisor advisor;

	private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

	/**
	 * This should run after all other post-processors, so that it can just add
	 * an advisor to existing proxies rather than double-proxy.
	 */
	private int order = Ordered.LOWEST_PRECEDENCE;

	private final Map<String, Boolean> eligibleBeans = new ConcurrentHashMap<String, Boolean>();


	public void setBeanClassLoader(ClassLoader beanClassLoader) {
		this.beanClassLoader = beanClassLoader;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getOrder() {
		return this.order;
	}


	public Object postProcessBeforeInitialization(Object bean, String beanName) {
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) {
		if (bean instanceof AopInfrastructureBean) {
			// Ignore AOP infrastructure such as scoped proxies.
			return bean;
		}
		if (isEligible(bean, beanName)) {
			if (bean instanceof Advised) {
				((Advised) bean).addAdvisor(0, this.advisor);
				return bean;
			}
			else {
				ProxyFactory proxyFactory = new ProxyFactory(bean);
				// Copy our properties (proxyTargetClass etc) inherited from ProxyConfig.
				proxyFactory.copyFrom(this);
				proxyFactory.addAdvisor(this.advisor);
				return proxyFactory.getProxy(this.beanClassLoader);
			}
		}
		else {
			// No async proxy needed.
			return bean;
		}
	}

	/**
	 * Check whether the given bean is eligible for advising with this
	 * post-processor's {@link Advisor}.
	 * <p>Implements caching of <code>canApply</code> results per bean name.
	 * @param bean the bean instance
	 * @param beanName the name of the bean
	 * @see AopUtils#canApply(Advisor, Class)
	 */
	protected boolean isEligible(Object bean, String beanName) {
		Boolean eligible = this.eligibleBeans.get(beanName);
		if (eligible != null) {
			return eligible;
		}
		Class<?> targetClass = AopUtils.getTargetClass(bean);
		eligible = AopUtils.canApply(this.advisor, targetClass);
		this.eligibleBeans.put(beanName, eligible);
		return eligible;
	}

}
