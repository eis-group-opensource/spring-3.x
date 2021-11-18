/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package test.beans;

/**
 * 
 * @author Rod Johnson
 */
public interface Person {
	
	String getName();
	void setName(String name);
	int getAge();
	void setAge(int i);
	
	/** 
	 * Test for non-property method matching.
	 * If the parameter is a Throwable, it will be thrown rather than 
	 * returned.
	 */
	Object echo(Object o) throws Throwable;
}