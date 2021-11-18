/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans;

import java.io.IOException;

/**
 * Interface used for {@link org.springframework.beans.TestBean}.
 *
 * <p>Two methods are the same as on Person, but if this
 * extends person it breaks quite a few tests..
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 */
public interface ITestBean {

	int getAge();

	void setAge(int age);

	String getName();

	void setName(String name);

	ITestBean getSpouse();

	void setSpouse(ITestBean spouse);

	ITestBean[] getSpouses();

	String[] getStringArray();

	void setStringArray(String[] stringArray);

	/**
	 * Throws a given (non-null) exception.
	 */
	void exceptional(Throwable t) throws Throwable;

	Object returnsThis();

	INestedTestBean getDoctor();

	INestedTestBean getLawyer();

	IndexedTestBean getNestedIndexedBean();

	/**
	 * Increment the age by one.
	 * @return the previous age
	 */
	int haveBirthday();

	void unreliableFileOperation() throws IOException;

}