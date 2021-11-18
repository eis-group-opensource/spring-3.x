/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export.assembler;

import java.util.Properties;

import javax.management.MBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanInfo;

import org.junit.Ignore;

/**
 * @author Rob Harrop
 */
public class InterfaceBasedMBeanInfoAssemblerMappedTests extends AbstractJmxAssemblerTests {

	protected static final String OBJECT_NAME = "bean:name=testBean4";

	public void testGetAgeIsReadOnly() throws Exception {
		ModelMBeanInfo info = getMBeanInfoFromAssembler();
		ModelMBeanAttributeInfo attr = info.getAttribute(AGE_ATTRIBUTE);

		assertTrue("Age is not readable", attr.isReadable());
		assertFalse("Age is not writable", attr.isWritable());
	}

	public void testWithUnknownClass() throws Exception {
		try {
			InterfaceBasedMBeanInfoAssembler assembler = getWithMapping("com.foo.bar.Unknown");
			fail("Should have thrown IllegalArgumentException");
		}
		catch (IllegalArgumentException ex) {
			// expected
		}
	}

	public void testWithNonInterface() throws Exception {
		try {
			InterfaceBasedMBeanInfoAssembler assembler = getWithMapping("JmxTestBean");
			fail("Should have thrown IllegalArgumentException");
		}
		catch (IllegalArgumentException ex) {
			// expected
		}
	}

	public void ignoreTestWithFallThrough() throws Exception {
		InterfaceBasedMBeanInfoAssembler assembler =
				getWithMapping("foobar", "org.springframework.jmx.export.assembler.ICustomJmxBean");
		assembler.setManagedInterfaces(new Class[] {IAdditionalTestMethods.class});

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
		return getWithMapping(
				"org.springframework.jmx.export.assembler.IAdditionalTestMethods, " +
				"org.springframework.jmx.export.assembler.ICustomJmxBean");
	}

	protected String getApplicationContextPath() {
		return "org/springframework/jmx/export/assembler/interfaceAssemblerMapped.xml";
	}

	private InterfaceBasedMBeanInfoAssembler getWithMapping(String mapping) {
		return getWithMapping(OBJECT_NAME, mapping);
	}

	private InterfaceBasedMBeanInfoAssembler getWithMapping(String name, String mapping) {
		InterfaceBasedMBeanInfoAssembler assembler = new InterfaceBasedMBeanInfoAssembler();
		Properties props = new Properties();
		props.setProperty(name, mapping);
		assembler.setInterfaceMappings(props);
		assembler.afterPropertiesSet();
		return assembler;
	}

	private void assertNickName(MBeanAttributeInfo attr) {
		assertNotNull("Nick Name should not be null", attr);
		assertTrue("Nick Name should be writable", attr.isWritable());
		assertTrue("Nick Name should be readab;e", attr.isReadable());
	}

}
