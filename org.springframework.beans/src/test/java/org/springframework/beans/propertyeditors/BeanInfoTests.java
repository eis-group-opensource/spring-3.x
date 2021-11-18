/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.propertyeditors;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import junit.framework.TestCase;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.FatalBeanException;
import org.springframework.core.JdkVersion;
import org.springframework.util.Assert;

/**
 * @author Juergen Hoeller
 * @since 06.03.2006
 */
public class BeanInfoTests extends TestCase {

	public void testComplexObject() {
		ValueBean bean = new ValueBean();
		BeanWrapper bw = new BeanWrapperImpl(bean);
		Integer value = new Integer(1);

		bw.setPropertyValue("value", value);
		assertEquals("value not set correctly", bean.getValue(), value);

		value = new Integer(2);
		bw.setPropertyValue("value", value.toString());
		assertEquals("value not converted", bean.getValue(), value);

		bw.setPropertyValue("value", null);
		assertNull("value not null", bean.getValue());

		bw.setPropertyValue("value", "");
		assertNull("value not converted to null", bean.getValue());
	}


	public static class ValueBean {

		private Integer value;

		public Integer getValue() {
			return value;
		}

		public void setValue(Integer value) {
			this.value = value;
		}
	}


	public static class ValueBeanBeanInfo extends SimpleBeanInfo {

		public PropertyDescriptor[] getPropertyDescriptors() {
			try {
				PropertyDescriptor pd = new PropertyDescriptor("value", ValueBean.class);
				pd.setPropertyEditorClass(MyNumberEditor.class);
				return new PropertyDescriptor[] {pd};
			}
			catch (IntrospectionException ex) {
				throw new FatalBeanException("Couldn't create PropertyDescriptor", ex);
			}
		}
	}


	public static class MyNumberEditor extends CustomNumberEditor {

		private Object target;

		public MyNumberEditor() throws IllegalArgumentException {
			super(Integer.class, true);
		}

		public MyNumberEditor(Object target) throws IllegalArgumentException {
			super(Integer.class, true);
			this.target = target;
		}

		public void setAsText(String text) throws IllegalArgumentException {
			if (JdkVersion.getMajorJavaVersion() >= JdkVersion.JAVA_15) {
				Assert.isTrue(this.target instanceof ValueBean, "Target must be available on JDK 1.5+");
			}
			super.setAsText(text);
		}

	}

}
