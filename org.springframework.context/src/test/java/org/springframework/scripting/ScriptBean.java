/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scripting;

/**
 * Simple interface used in testing the scripted beans support.
 * 
 * @author Rick Evans
 */
public interface ScriptBean {

	String getName();

	void setName(String name);

	int getAge();

	void setAge(int age);

}
