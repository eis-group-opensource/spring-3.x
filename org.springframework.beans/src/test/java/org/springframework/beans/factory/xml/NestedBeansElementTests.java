/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.beans.factory.xml;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Tests for new nested beans element support in Spring XML
 *
 * @author Chris Beams
 */
public class NestedBeansElementTests {
	private final Resource XML =
		new ClassPathResource("NestedBeansElementTests-context.xml", this.getClass());

	@Test
	public void getBean_withoutActiveProfile() {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(bf).loadBeanDefinitions(XML);

		Object foo = bf.getBean("foo");
		assertThat(foo, instanceOf(String.class));
	}

	@Test
	public void getBean_withActiveProfile() {
		ConfigurableEnvironment env = new StandardEnvironment();
		env.setActiveProfiles("dev");

		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(bf);
		reader.setEnvironment(env);
		reader.loadBeanDefinitions(XML);

		bf.getBean("devOnlyBean"); // should not throw NSBDE

		Object foo = bf.getBean("foo");
		assertThat(foo, instanceOf(Integer.class));

		bf.getBean("devOnlyBean");
	}

}
