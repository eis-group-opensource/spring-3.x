/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Canonical MethodMatcher instance that matches all methods.
 *
 * @author Rod Johnson
 */
class TrueMethodMatcher implements MethodMatcher, Serializable {
	
	public static final TrueMethodMatcher INSTANCE = new TrueMethodMatcher();
	
	/**
	 * Enforce Singleton pattern.
	 */
	private TrueMethodMatcher() {
	}

	public boolean isRuntime() {
		return false;
	}

	public boolean matches(Method method, Class targetClass) {
		return true;
	}

	public boolean matches(Method method, Class targetClass, Object[] args) {
		// Should never be invoked as isRuntime returns false.
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Required to support serialization. Replaces with canonical
	 * instance on deserialization, protecting Singleton pattern.
	 * Alternative to overriding <code>equals()</code>.
	 */
	private Object readResolve() {
		return INSTANCE;
	}
	
	@Override
	public String toString() {
		return "MethodMatcher.TRUE";
	}

}
