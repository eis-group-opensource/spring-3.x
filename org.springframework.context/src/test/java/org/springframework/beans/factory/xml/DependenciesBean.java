/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.xml;

import org.springframework.beans.TestBean;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * Simple bean used to test dependency checking.
 * 
 * Note: would be defined within {@link XmlBeanFactoryTestTypes}, but must be a public type
 * in order to satisfy test dependencies.
 *
 * @author Rod Johnson
 * @author Chris Beams
 * @since 04.09.2003
 */
public final class DependenciesBean implements BeanFactoryAware {
	
	private int age;
	
	private String name;
	
	private TestBean spouse;

	private BeanFactory beanFactory;


	public void setAge(int age) {
		this.age = age;
	}

	public int getAge() {
		return age;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setSpouse(TestBean spouse) {
		this.spouse = spouse;
	}

	public TestBean getSpouse() {
		return spouse;
	}

	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

}
