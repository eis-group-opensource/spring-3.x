/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.support;

import junit.framework.TestCase;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;

import test.beans.TestBean;

/**
 * @author Rob Harrop
 */
public class DefinitionMetadataEqualsHashCodeTests extends TestCase {

	public void testRootBeanDefinitionEqualsAndHashCode() throws Exception {
		RootBeanDefinition master = new RootBeanDefinition(TestBean.class);
		RootBeanDefinition equal = new RootBeanDefinition(TestBean.class);
		RootBeanDefinition notEqual = new RootBeanDefinition(String.class);
		RootBeanDefinition subclass = new RootBeanDefinition(TestBean.class) {};
		setBaseProperties(master);
		setBaseProperties(equal);
		setBaseProperties(notEqual);
		setBaseProperties(subclass);

		assertEqualsContract(master, equal, notEqual, subclass);
		assertEquals("Hash code for equal instances should match", master.hashCode(), equal.hashCode());
	}

	public void testChildBeanDefinitionEqualsAndHashCode() throws Exception {
		ChildBeanDefinition master = new ChildBeanDefinition("foo");
		ChildBeanDefinition equal = new ChildBeanDefinition("foo");
		ChildBeanDefinition notEqual = new ChildBeanDefinition("bar");
		ChildBeanDefinition subclass = new ChildBeanDefinition("foo"){};
		setBaseProperties(master);
		setBaseProperties(equal);
		setBaseProperties(notEqual);
		setBaseProperties(subclass);

		assertEqualsContract(master, equal, notEqual, subclass);
		assertEquals("Hash code for equal instances should match", master.hashCode(), equal.hashCode());
	}

	public void testRuntimeBeanReference() throws Exception {
		RuntimeBeanReference master = new RuntimeBeanReference("name");
		RuntimeBeanReference equal = new RuntimeBeanReference("name");
		RuntimeBeanReference notEqual = new RuntimeBeanReference("someOtherName");
		RuntimeBeanReference subclass = new RuntimeBeanReference("name"){};
		assertEqualsContract(master, equal, notEqual, subclass);
	}
	private void setBaseProperties(AbstractBeanDefinition definition) {
		definition.setAbstract(true);
		definition.setAttribute("foo", "bar");
		definition.setAutowireCandidate(false);
		definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
		//definition.getConstructorArgumentValues().addGenericArgumentValue("foo");
		definition.setDependencyCheck(AbstractBeanDefinition.DEPENDENCY_CHECK_OBJECTS);
		definition.setDependsOn(new String[]{"foo", "bar"});
		definition.setDestroyMethodName("destroy");
		definition.setEnforceDestroyMethod(false);
		definition.setEnforceInitMethod(true);
		definition.setFactoryBeanName("factoryBean");
		definition.setFactoryMethodName("factoryMethod");
		definition.setInitMethodName("init");
		definition.setLazyInit(true);
		definition.getMethodOverrides().addOverride(new LookupOverride("foo", "bar"));
		definition.getMethodOverrides().addOverride(new ReplaceOverride("foo", "bar"));
		definition.getPropertyValues().add("foo", "bar");
		definition.setResourceDescription("desc");
		definition.setRole(BeanDefinition.ROLE_APPLICATION);
		definition.setScope(BeanDefinition.SCOPE_PROTOTYPE);
		definition.setSource("foo");
	}

	private void assertEqualsContract(Object master, Object equal, Object notEqual, Object subclass) {
		assertEquals("Should be equal", master, equal);
		assertFalse("Should not be equal", master.equals(notEqual));
		assertEquals("Subclass should be equal", master, subclass);
	}

}
