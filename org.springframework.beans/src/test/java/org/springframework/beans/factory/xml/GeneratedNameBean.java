/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.xml;

import org.springframework.beans.factory.BeanNameAware;

/**
 * @author Rob Harrop
 */
public class GeneratedNameBean implements BeanNameAware {

	private String beanName;

	private GeneratedNameBean child;

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setChild(GeneratedNameBean child) {
		this.child = child;
	}

	public GeneratedNameBean getChild() {
		return child;
	}

}
