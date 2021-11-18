/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.support;

import org.springframework.aop.framework.AbstractSingletonProxyFactoryBean;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

@SuppressWarnings({ "serial", "deprecation" })
public class TestProxyFactoryBean extends AbstractSingletonProxyFactoryBean implements BeanFactoryAware {

	@Override
	protected Object createMainInterceptor() {
		return new NoOpAdvice();
	}

	public void setBeanFactory(BeanFactory beanFactory) {
	}

}
