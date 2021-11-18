/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export.annotation;

/**
 * @author Rob Harrop
 */
public class AnnotationTestSubBean extends AnnotationTestBean implements IAnnotationTestBean {

	private String colour;

	@Override
	public long myOperation() {
		return 123L;
	}

	@Override
	public void setAge(int age) {
		super.setAge(age);
	}

	@Override
	public int getAge() {
		return super.getAge();
	}

	public String getColour() {
		return this.colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public void fromInterface() {
	}

	public int getExpensiveToCalculate() {
		return Integer.MAX_VALUE;
	}
}
