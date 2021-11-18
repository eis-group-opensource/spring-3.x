/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation;

import example.scannable.FooService;

import org.springframework.core.convert.converter.Converter;

/**
 * @author Juergen Hoeller
 */
public class FooServiceDependentConverter implements Converter<String, org.springframework.beans.TestBean> {

	private FooService fooService;

	public void setFooService(FooService fooService) {
		this.fooService = fooService;
	}

	public org.springframework.beans.TestBean convert(String source) {
		return new org.springframework.beans.TestBean(source);
	}

}
