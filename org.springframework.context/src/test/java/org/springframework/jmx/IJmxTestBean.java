/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx;

/**
 * @author Rob Harrop
 * @author Juergen Hoeller
 */
public interface IJmxTestBean {

	public int add(int x, int y);

	public long myOperation();

	public int getAge();

	public void setAge(int age);

	public void setName(String name) throws Exception;

	public String getName();

	// used to test invalid methods that exist in the proxy interface
	public void dontExposeMe();
	
}
