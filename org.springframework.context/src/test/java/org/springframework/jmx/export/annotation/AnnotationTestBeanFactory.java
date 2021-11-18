/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export.annotation;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.jmx.IJmxTestBean;

/**
 * @author Juergen Hoeller
 */
public class AnnotationTestBeanFactory implements FactoryBean<IJmxTestBean> {

	private final FactoryCreatedAnnotationTestBean instance = new FactoryCreatedAnnotationTestBean();

	public AnnotationTestBeanFactory() {
		this.instance.setName("FACTORY");
	}

	public IJmxTestBean getObject() throws Exception {
		return this.instance;
	}

	public Class<? extends IJmxTestBean> getObjectType() {
		return FactoryCreatedAnnotationTestBean.class;
	}

	public boolean isSingleton() {
		return true;
	}

}
