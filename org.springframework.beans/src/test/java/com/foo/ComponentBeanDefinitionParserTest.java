/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.foo;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * @author Costin Leau
 */
public class ComponentBeanDefinitionParserTest {

	private static XmlBeanFactory bf;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		bf = new XmlBeanFactory(new ClassPathResource(
				"com/foo/component-config.xml"));
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		bf.destroySingletons();
	}

	private Component getBionicFamily() {
		return bf.getBean("bionic-family", Component.class);
	}

	@Test
	public void testBionicBasic() throws Exception {
		Component cp = getBionicFamily();
		assertThat("Bionic-1", equalTo(cp.getName()));
	}

	@Test
	public void testBionicFirstLevelChildren() throws Exception {
		Component cp = getBionicFamily();
		List<Component> components = cp.getComponents();
		assertThat(2, equalTo(components.size()));
		assertThat("Mother-1", equalTo(components.get(0).getName()));
		assertThat("Rock-1", equalTo(components.get(1).getName()));
	}

	@Test
	public void testBionicSecondLevenChildren() throws Exception {
		Component cp = getBionicFamily();
		List<Component> components = cp.getComponents().get(0).getComponents();
		assertThat(2, equalTo(components.size()));
		assertThat("Karate-1", equalTo(components.get(0).getName()));
		assertThat("Sport-1", equalTo(components.get(1).getName()));
	}
}