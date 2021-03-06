/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.support;

import org.springframework.util.StringUtils;

/**
 * A simple holder for <code>BeanDefinition</code> property defaults.
 * 
 * @author Mark Fisher
 * @since 2.5
 */
public class BeanDefinitionDefaults {

	private boolean lazyInit;

	private int dependencyCheck = AbstractBeanDefinition.DEPENDENCY_CHECK_NONE;

	private int autowireMode = AbstractBeanDefinition.AUTOWIRE_NO;

	private String initMethodName;

	private String destroyMethodName;


	public void setLazyInit(boolean lazyInit) {
		this.lazyInit = lazyInit;
	}

	public boolean isLazyInit() {
		return this.lazyInit;
	}

	public void setDependencyCheck(int dependencyCheck) {
		this.dependencyCheck = dependencyCheck;
	}

	public int getDependencyCheck() {
		return this.dependencyCheck;
	}

	public void setAutowireMode(int autowireMode) {
		this.autowireMode = autowireMode;
	}

	public int getAutowireMode() {
		return this.autowireMode;
	}

	public void setInitMethodName(String initMethodName) {
		this.initMethodName = (StringUtils.hasText(initMethodName)) ? initMethodName : null;
	}

	public String getInitMethodName() {
		return this.initMethodName;
	}

	public void setDestroyMethodName(String destroyMethodName) {
		this.destroyMethodName = (StringUtils.hasText(destroyMethodName)) ? destroyMethodName : null;
	}

	public String getDestroyMethodName() {
		return this.destroyMethodName;
	}

}
