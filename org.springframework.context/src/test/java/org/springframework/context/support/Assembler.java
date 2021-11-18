/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.support;

/**
 * @author Alef Arendsen
 */
public class Assembler implements TestIF {

	private Service service;
	private Logic l;	
	private String name;

	public void setService(Service service) {
		this.service = service;
	}
	
	public void setLogic(Logic l) {
		this.l = l;
	}
	
	public void setBeanName(String name) {
		this.name = name;
	}

	public void test() {
	}
	
	public void output() {
		System.out.println("Bean " + name);
		l.output();
	}

}
