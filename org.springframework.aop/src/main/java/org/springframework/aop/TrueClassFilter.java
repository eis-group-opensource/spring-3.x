/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop;

import java.io.Serializable;

/**
 * Canonical ClassFilter instance that matches all classes.
 *
 * @author Rod Johnson
 */
class TrueClassFilter implements ClassFilter, Serializable {
	
	public static final TrueClassFilter INSTANCE = new TrueClassFilter();
	
	/**
	 * Enforce Singleton pattern.
	 */
	private TrueClassFilter() {
	}

	public boolean matches(Class clazz) {
		return true;
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
		return "ClassFilter.TRUE";
	}

}
