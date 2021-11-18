/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export.assembler;




/**
 * @author Rob Harrop
 */
public class ReflectiveAssemblerTests extends AbstractJmxAssemblerTests {

	protected static final String OBJECT_NAME = "bean:name=testBean1";

	protected String getObjectName() {
		return OBJECT_NAME;
	}

	protected int getExpectedOperationCount() {
		return 11;
	}

	protected int getExpectedAttributeCount() {
		return 4;
	}

	protected MBeanInfoAssembler getAssembler() {
		return new SimpleReflectiveMBeanInfoAssembler();
	}

	protected String getApplicationContextPath() {
		return "org/springframework/jmx/export/assembler/reflectiveAssembler.xml";
	}

}
