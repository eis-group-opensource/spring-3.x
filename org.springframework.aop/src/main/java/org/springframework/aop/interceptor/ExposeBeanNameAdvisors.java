/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import org.springframework.aop.Advisor;
import org.springframework.aop.ProxyMethodInvocation;
import org.springframework.aop.support.DefaultIntroductionAdvisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.DelegatingIntroductionInterceptor;
import org.springframework.beans.factory.NamedBean;

/**
 * Convenient methods for creating advisors that may be used when autoproxying beans
 * created with the Spring IoC container, binding the bean name to the current
 * invocation. May support a <code>bean()</code> pointcut designator with AspectJ.
 *
 * <p>Typically used in Spring auto-proxying, where the bean name is known
 * at proxy creation time.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 2.0
 * @see org.springframework.beans.factory.NamedBean
 */
public abstract class ExposeBeanNameAdvisors {

	/**
	 * Binding for the bean name of the bean which is currently being invoked
	 * in the ReflectiveMethodInvocation userAttributes Map.
	 */
	private static final String BEAN_NAME_ATTRIBUTE = ExposeBeanNameAdvisors.class.getName() + ".BEAN_NAME";


	/**
	 * Find the bean name for the current invocation. Assumes that an ExposeBeanNameAdvisor
	 * has been included in the interceptor chain, and that the invocation is exposed
	 * with ExposeInvocationInterceptor.
	 * @return the bean name (never <code>null</code>)
	 * @throws IllegalStateException if the bean name has not been exposed
	 */
	public static String getBeanName() throws IllegalStateException {
		return getBeanName(ExposeInvocationInterceptor.currentInvocation());
	}

	/**
	 * Find the bean name for the given invocation. Assumes that an ExposeBeanNameAdvisor
	 * has been included in the interceptor chain.
	 * @param mi MethodInvocation that should contain the bean name as an attribute
	 * @return the bean name (never <code>null</code>)
	 * @throws IllegalStateException if the bean name has not been exposed
	 */
	public static String getBeanName(MethodInvocation mi) throws IllegalStateException {
		if (!(mi instanceof ProxyMethodInvocation)) {
			throw new IllegalArgumentException("MethodInvocation is not a Spring ProxyMethodInvocation: " + mi);
		}
		ProxyMethodInvocation pmi = (ProxyMethodInvocation) mi;
		String beanName = (String) pmi.getUserAttribute(BEAN_NAME_ATTRIBUTE);
		if (beanName == null) {
			throw new IllegalStateException("Cannot get bean name; not set on MethodInvocation: " + mi);
		}
		return beanName;
	}

	/**
	 * Create a new advisor that will expose the given bean name,
	 * with no introduction
	 * @param beanName bean name to expose
	 */
	public static Advisor createAdvisorWithoutIntroduction(String beanName) {
		return new DefaultPointcutAdvisor(new ExposeBeanNameInterceptor(beanName));
	}

	/**
	 * Create a new advisor that will expose the given bean name, introducing
	 * the NamedBean interface to make the bean name accessible without forcing
	 * the target object to be aware of this Spring IoC concept.
	 * @param beanName the bean name to expose
	 */
	public static Advisor createAdvisorIntroducingNamedBean(String beanName) {
		return new DefaultIntroductionAdvisor(new ExposeBeanNameIntroduction(beanName));
	}


	/**
	 * Interceptor that exposes the specified bean name as invocation attribute.
	 */
	private static class ExposeBeanNameInterceptor implements MethodInterceptor {

		private final String beanName;

		public ExposeBeanNameInterceptor(String beanName) {
			this.beanName = beanName;
		}

		public Object invoke(MethodInvocation mi) throws Throwable {
			if (!(mi instanceof ProxyMethodInvocation)) {
				throw new IllegalStateException("MethodInvocation is not a Spring ProxyMethodInvocation: " + mi);
			}
			ProxyMethodInvocation pmi = (ProxyMethodInvocation) mi;
			pmi.setUserAttribute(BEAN_NAME_ATTRIBUTE, this.beanName);
			return mi.proceed();
		}
	}


	/**
	 * Introduction that exposes the specified bean name as invocation attribute.
	 */
	private static class ExposeBeanNameIntroduction extends DelegatingIntroductionInterceptor implements NamedBean {

		private final String beanName; 

		public ExposeBeanNameIntroduction(String beanName) {
			this.beanName = beanName;
		}

		@Override
		public Object invoke(MethodInvocation mi) throws Throwable {
			if (!(mi instanceof ProxyMethodInvocation)) {
				throw new IllegalStateException("MethodInvocation is not a Spring ProxyMethodInvocation: " + mi);
			}
			ProxyMethodInvocation pmi = (ProxyMethodInvocation) mi;
			pmi.setUserAttribute(BEAN_NAME_ATTRIBUTE, this.beanName);
			return super.invoke(mi);
		}

		public String getBeanName() {
			return this.beanName;
		}
	}

}
