/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation.configuration;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import test.beans.TestBean;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Ensures that @Configuration is supported properly as a meta-annotation.
 *
 * @author Chris Beams
 */
public class ConfigurationMetaAnnotationTests {

	@Test
	public void customConfigurationStereotype() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(Config.class);
		ctx.refresh();
		assertThat(ctx.containsBean("customName"), is(true));
		TestBean a = ctx.getBean("a", TestBean.class);
		TestBean b = ctx.getBean("b", TestBean.class);
		assertThat(b, sameInstance(a.getSpouse()));
	}


	@TestConfiguration("customName")
	static class Config {
		@Bean
		public TestBean a() {
			TestBean a = new TestBean();
			a.setSpouse(b());
			return a;
		}

		@Bean
		public TestBean b() {
			return new TestBean();
		}
	}


	@Configuration
	@Retention(RetentionPolicy.RUNTIME)
	public @interface TestConfiguration {
		String value() default "";
	}
}
