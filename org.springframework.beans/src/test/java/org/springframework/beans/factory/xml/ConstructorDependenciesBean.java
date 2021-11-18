/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.xml;

import java.io.Serializable;

import test.beans.IndexedTestBean;
import test.beans.TestBean;

/**
 * Simple bean used to check constructor dependency checking.
 *
 * @author Juergen Hoeller
 * @since 09.11.2003
 */
public class ConstructorDependenciesBean implements Serializable {
	
	private int age;
	
	private String name;
	
	private TestBean spouse1;

	private TestBean spouse2;

	private IndexedTestBean other;

	public ConstructorDependenciesBean(int age) {
		this.age = age;
	}

	public ConstructorDependenciesBean(String name) {
		this.name = name;
	}

	public ConstructorDependenciesBean(TestBean spouse1) {
		this.spouse1 = spouse1;
	}

	public ConstructorDependenciesBean(TestBean spouse1, TestBean spouse2) {
		this.spouse1 = spouse1;
		this.spouse2 = spouse2;
	}

	public ConstructorDependenciesBean(TestBean spouse1, TestBean spouse2, int age) {
		this.spouse1 = spouse1;
		this.spouse2 = spouse2;
		this.age = age;
	}

	public ConstructorDependenciesBean(TestBean spouse1, TestBean spouse2, IndexedTestBean other) {
		this.spouse1 = spouse1;
		this.spouse2 = spouse2;
		this.other = other;
	}

	public int getAge() {
		return age;
	}

	public String getName() {
		return name;
	}

	public TestBean getSpouse1() {
		return spouse1;
	}

	public TestBean getSpouse2() {
		return spouse2;
	}

	public IndexedTestBean getOther() {
		return other;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setName(String name) {
		this.name = name;
	}

}
