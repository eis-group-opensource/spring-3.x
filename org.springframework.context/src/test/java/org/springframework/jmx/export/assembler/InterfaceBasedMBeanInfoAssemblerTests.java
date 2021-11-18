/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export.assembler;

/**
 * @author Rob Harrop
 */
public class InterfaceBasedMBeanInfoAssemblerTests extends AbstractJmxAssemblerTests {

	protected String getObjectName() {
		return "bean:name=testBean4";
	}

	protected int getExpectedOperationCount() {
		return 7;
	}

	protected int getExpectedAttributeCount() {
		return 2;
	}

	protected MBeanInfoAssembler getAssembler() {
		return new InterfaceBasedMBeanInfoAssembler();
	}

	protected String getApplicationContextPath() {
		return "org/springframework/jmx/export/assembler/interfaceAssembler.xml";
	}

}
