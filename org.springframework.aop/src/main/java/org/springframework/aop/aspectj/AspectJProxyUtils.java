/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.aspectj;

import java.util.List;

import org.springframework.aop.Advisor;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.interceptor.ExposeInvocationInterceptor;

/**
 * Utility methods for working with AspectJ proxies.
 *
 * @author Rod Johnson
 * @author Ramnivas Laddad
 * @since 2.0
 */
public abstract class AspectJProxyUtils {
	
	/**
	 * Add special advisors if necessary to work with a proxy chain that contains AspectJ advisors.
	 * This will expose the current Spring AOP invocation (necessary for some AspectJ pointcut matching)
	 * and make available the current AspectJ JoinPoint. The call will have no effect if there are no
	 * AspectJ advisors in the advisor chain.
	 * @param advisors Advisors available
	 * @return <code>true</code> if any special {@link Advisor Advisors} were added, otherwise <code>false</code>.
	 */
	public static boolean makeAdvisorChainAspectJCapableIfNecessary(List<Advisor> advisors) {
		// Don't add advisors to an empty list; may indicate that proxying is just not required
		if (!advisors.isEmpty()) {
			boolean foundAspectJAdvice = false;
			for (Advisor advisor : advisors) {
				// Be careful not to get the Advice without a guard, as
				// this might eagerly instantiate a non-singleton AspectJ aspect
				if (isAspectJAdvice(advisor)) {
					foundAspectJAdvice = true;
				}
			}
			if (foundAspectJAdvice && !advisors.contains(ExposeInvocationInterceptor.ADVISOR)) {
				advisors.add(0, ExposeInvocationInterceptor.ADVISOR);
				return true;
			}
		}
		return false;
	}

	/**
	 * Determine whether the given Advisor contains an AspectJ advice.
	 * @param advisor the Advisor to check
	 */
	private static boolean isAspectJAdvice(Advisor advisor) {
		return (advisor instanceof InstantiationModelAwarePointcutAdvisor ||
			   advisor.getAdvice() instanceof AbstractAspectJAdvice ||
			   (advisor instanceof PointcutAdvisor &&
						 ((PointcutAdvisor) advisor).getPointcut() instanceof AspectJExpressionPointcut));
	}

}
