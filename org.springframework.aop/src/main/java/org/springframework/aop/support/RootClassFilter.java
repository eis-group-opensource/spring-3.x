/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.support;

import java.io.Serializable;

import org.springframework.aop.ClassFilter;

/**
 * Simple ClassFilter implementation that passes classes (and optionally subclasses)
 * @author Rod Johnson
 */
public class RootClassFilter implements ClassFilter, Serializable {
	
	private Class clazz;
	
	// TODO inheritance
	
	public RootClassFilter(Class clazz) {
		this.clazz = clazz;
	}

	public boolean matches(Class candidate) {
		return clazz.isAssignableFrom(candidate);
	}

}
