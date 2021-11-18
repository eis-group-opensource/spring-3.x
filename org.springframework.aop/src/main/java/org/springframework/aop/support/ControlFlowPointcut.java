/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.support;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.core.ControlFlow;
import org.springframework.core.ControlFlowFactory;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * Pointcut and method matcher for use in simple <b>cflow</b>-style pointcut.
 * Note that evaluating such pointcuts is 10-15 times slower than evaluating
 * normal pointcuts, but they are useful in some cases.
 *
 * @author Rod Johnson
 * @author Rob Harrop
 * @see org.springframework.core.ControlFlow
 */
public class ControlFlowPointcut implements Pointcut, ClassFilter, MethodMatcher, Serializable {

	private Class clazz;

	private String methodName;

	private int evaluations;


	/**
	 * Construct a new pointcut that matches all control flows below that class.
	 * @param clazz the clazz
	 */
	public ControlFlowPointcut(Class clazz) {
		this(clazz, null);
	}

	/**
	 * Construct a new pointcut that matches all calls below the
	 * given method in the given class. If the method name is null,
	 * matches all control flows below that class.
	 * @param clazz the clazz
	 * @param methodName the name of the method
	 */
	public ControlFlowPointcut(Class clazz, String methodName) {
		Assert.notNull(clazz, "Class must not be null");
		this.clazz = clazz;
		this.methodName = methodName;
	}


	/**
	 * Subclasses can override this for greater filtering (and performance).
	 */
	public boolean matches(Class clazz) {
		return true;
	}

	/**
	 * Subclasses can override this if it's possible to filter out
	 * some candidate classes.
	 */
	public boolean matches(Method method, Class targetClass) {
		return true;
	}

	public boolean isRuntime() {
		return true;
	}

	public boolean matches(Method method, Class targetClass, Object[] args) {
		++this.evaluations;
		ControlFlow cflow = ControlFlowFactory.createControlFlow();
		return (this.methodName != null) ? cflow.under(this.clazz, this.methodName) : cflow.under(this.clazz);
	}

	/**
	 * It's useful to know how many times we've fired, for optimization.
	 */
	public int getEvaluations() {
		return evaluations;
	}


	public ClassFilter getClassFilter() {
		return this;
	}

	public MethodMatcher getMethodMatcher() {
		return this;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ControlFlowPointcut)) {
			return false;
		}
		ControlFlowPointcut that = (ControlFlowPointcut) other;
		return (this.clazz.equals(that.clazz)) && ObjectUtils.nullSafeEquals(that.methodName, this.methodName);
	}

	@Override
	public int hashCode() {
		int code = 17;
		code = 37 * code + this.clazz.hashCode();
		if (this.methodName != null) {
			code = 37 * code + this.methodName.hashCode();
		}
		return code;
	}

}
