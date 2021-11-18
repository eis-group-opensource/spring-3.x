/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.framework.autoproxy;

import org.springframework.core.NamedThreadLocal;

/**
 * Holder for the current proxy creation context, as exposed by auto-proxy creators
 * such as {@link AbstractAdvisorAutoProxyCreator}.
 *
 * @author Juergen Hoeller
 * @author Ramnivas Laddad
 * @since 2.5
 */
public class ProxyCreationContext {

	/** ThreadLocal holding the current proxied bean name during Advisor matching */
	private static final ThreadLocal<String> currentProxiedBeanName =
			new NamedThreadLocal<String>("Name of currently proxied bean");


	/**
	 * Return the name of the currently proxied bean instance.
	 * @return the name of the bean, or <code>null</code> if none available
	 */
	public static String getCurrentProxiedBeanName() {
		return currentProxiedBeanName.get();
	}

	/**
	 * Set the name of the currently proxied bean instance.
	 * @param beanName the name of the bean, or <code>null</code> to reset it
	 */
	static void setCurrentProxiedBeanName(String beanName) {
		if (beanName != null) {
			currentProxiedBeanName.set(beanName);
		}
		else {
			currentProxiedBeanName.remove();
		}
	}

}
