/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation.configuration;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import test.beans.ITestBean;
import test.beans.TestBean;

/**
 * A configuration class that registers a placeholder configurer @Bean method
 * cannot also have @Value fields.  Logically, the config class must be instantiated
 * in order to invoke the placeholder configurer bean method, and it is a
 * chicken-and-egg problem to process the @Value field.
 *
 * Therefore, placeholder configurers should be put in separate configuration classes
 * as has been done in the test below. Simply said, placeholder configurer @Bean methods
 * and @Value fields in the same configuration class are mutually exclusive.
 *
 * @author Chris Beams
 */
public class ConfigurationClassWithPlaceholderConfigurerBeanTests {

	/**
	 * Intentionally ignored test proving that a property placeholder bean
	 * cannot be declared in the same configuration class that has a @Value
	 * field in need of placeholder replacement.  It's an obvious chicken-and-egg issue.
	 * The solution is to do as {@link #valueFieldsAreProcessedWhenPlaceholderConfigurerIsSegregated()}
	 * does and segragate the two bean definitions across configuration classes.
	 */
	@Ignore @Test
	public void valueFieldsAreNotProcessedWhenPlaceholderConfigurerIsIntegrated() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(ConfigWithValueFieldAndPlaceholderConfigurer.class);
		System.setProperty("test.name", "foo");
		ctx.refresh();
		System.clearProperty("test.name");

		TestBean testBean = ctx.getBean(TestBean.class);
		assertThat(testBean.getName(), nullValue());
	}

	@Test
	public void valueFieldsAreProcessedWhenPlaceholderConfigurerIsSegregated() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(ConfigWithValueField.class);
		ctx.register(ConfigWithPlaceholderConfigurer.class);
		System.setProperty("test.name", "foo");
		ctx.refresh();
		System.clearProperty("test.name");

		TestBean testBean = ctx.getBean(TestBean.class);
		assertThat(testBean.getName(), equalTo("foo"));
	}
}

@Configuration
class ConfigWithValueField {

	@Value("${test.name}")
	private String name;

	@Bean
	public ITestBean testBean() {
		return new TestBean(this.name);
	}
}

@Configuration
class ConfigWithPlaceholderConfigurer {

	@Bean
	public PropertySourcesPlaceholderConfigurer ppc() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}

@Configuration
class ConfigWithValueFieldAndPlaceholderConfigurer {

	@Value("${test.name}")
	private String name;

	@Bean
	public ITestBean testBean() {
		return new TestBean(this.name);
	}

	@Bean
	public PropertySourcesPlaceholderConfigurer ppc() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}