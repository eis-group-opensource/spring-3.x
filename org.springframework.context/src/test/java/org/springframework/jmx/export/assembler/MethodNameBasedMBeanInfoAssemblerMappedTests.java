/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export.assembler;

import javax.management.MBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanInfo;
import java.util.Properties;

/**
 * @author Rob Harrop
 */
public class MethodNameBasedMBeanInfoAssemblerMappedTests extends AbstractJmxAssemblerTests {

	protected static final String OBJECT_NAME = "bean:name=testBean4";

	public void testGetAgeIsReadOnly() throws Exception {
		ModelMBeanInfo info = getMBeanInfoFromAssembler();
		ModelMBeanAttributeInfo attr = info.getAttribute(AGE_ATTRIBUTE);

		assertTrue("Age is not readable", attr.isReadable());
		assertFalse("Age is not writable", attr.isWritable());
	}

	public void testWithFallThrough() throws Exception {
		MethodNameBasedMBeanInfoAssembler assembler =
				getWithMapping("foobar", "add,myOperation,getName,setName,getAge");
		assembler.setManagedMethods(new String[]{"getNickName", "setNickName"});

		ModelMBeanInfo inf = assembler.getMBeanInfo(getBean(), getObjectName());
		MBeanAttributeInfo attr = inf.getAttribute("NickName");

		assertNickName(attr);
	}

	public void testNickNameIsExposed() throws Exception {
		ModelMBeanInfo inf = (ModelMBeanInfo) getMBeanInfo();
		MBeanAttributeInfo attr = inf.getAttribute("NickName");

		assertNickName(attr);
	}

	protected String getObjectName() {
		return OBJECT_NAME;
	}

	protected int getExpectedOperationCount() {
		return 7;
	}

	protected int getExpectedAttributeCount() {
		return 3;
	}

	protected MBeanInfoAssembler getAssembler() throws Exception {
		return getWithMapping("getNickName,setNickName,add,myOperation,getName,setName,getAge");
	}

	protected String getApplicationContextPath() {
		return "org/springframework/jmx/export/assembler/methodNameAssemblerMapped.xml";
	}

	private MethodNameBasedMBeanInfoAssembler getWithMapping(String mapping) {
		return getWithMapping(OBJECT_NAME, mapping);
	}

	private MethodNameBasedMBeanInfoAssembler getWithMapping(String name, String mapping) {
		MethodNameBasedMBeanInfoAssembler assembler = new MethodNameBasedMBeanInfoAssembler();
		Properties props = new Properties();
		props.setProperty(name, mapping);
		assembler.setMethodMappings(props);
		return assembler;
	}

	private void assertNickName(MBeanAttributeInfo attr) {
		assertNotNull("Nick Name should not be null", attr);
		assertTrue("Nick Name should be writable", attr.isWritable());
		assertTrue("Nick Name should be readable", attr.isReadable());
	}

}
