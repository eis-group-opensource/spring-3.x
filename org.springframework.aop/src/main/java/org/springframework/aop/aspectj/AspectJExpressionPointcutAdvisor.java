/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.aspectj;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractGenericPointcutAdvisor;

/**
 * Spring AOP Advisor that can be used for any AspectJ pointcut expression.
 * 
 * @author Rob Harrop
 * @since 2.0
 */
public class AspectJExpressionPointcutAdvisor extends AbstractGenericPointcutAdvisor {

	private final AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();


	public Pointcut getPointcut() {
		return this.pointcut;
	}

	public void setExpression(String expression) {
		this.pointcut.setExpression(expression);
	}

	public void setLocation(String location) {
		this.pointcut.setLocation(location);
	}

	public void setParameterTypes(Class[] types) {
		this.pointcut.setParameterTypes(types);
	}

	public void setParameterNames(String[] names) {
		this.pointcut.setParameterNames(names);
	}

	public String getLocation() {
		return this.pointcut.getLocation();
	}

	public String getExpression() {
		return this.pointcut.getExpression();
	}

}
