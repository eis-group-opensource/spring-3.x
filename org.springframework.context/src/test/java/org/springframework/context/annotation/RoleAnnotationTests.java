/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.role.ComponentWithRole;
import org.springframework.context.annotation.role.ComponentWithoutRole;

/**
 * Tests the use of the @Role annotation on @Bean methods and
 * @Component classes.
 *
 * @author Chris Beams
 * @since 3.1
 */
public class RoleAnnotationTests {

	@Test
	public void onBeanMethod() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(Config.class);
		ctx.refresh();
		assertThat("Expected bean to have ROLE_APPLICATION",
				ctx.getBeanDefinition("foo").getRole(), is(BeanDefinition.ROLE_APPLICATION));
		assertThat("Expected bean to have ROLE_INFRASTRUCTURE",
				ctx.getBeanDefinition("bar").getRole(), is(BeanDefinition.ROLE_INFRASTRUCTURE));
	}


	@Configuration
	static class Config {
		@Bean
		public String foo() {
			return "foo";
		}

		@Bean
		@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
		public String bar() {
			return "bar";
		}
	}


	@Test
	public void onComponentClass() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(ComponentWithoutRole.class, ComponentWithRole.class);
		ctx.refresh();
		assertThat("Expected bean to have ROLE_APPLICATION",
				ctx.getBeanDefinition("componentWithoutRole").getRole(), is(BeanDefinition.ROLE_APPLICATION));
		assertThat("Expected bean to have ROLE_INFRASTRUCTURE",
				ctx.getBeanDefinition("componentWithRole").getRole(), is(BeanDefinition.ROLE_INFRASTRUCTURE));
	}


	@Test
	public void viaComponentScanning() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.scan("org.springframework.context.annotation.role");
		ctx.refresh();
		assertThat("Expected bean to have ROLE_APPLICATION",
				ctx.getBeanDefinition("componentWithoutRole").getRole(), is(BeanDefinition.ROLE_APPLICATION));
		assertThat("Expected bean to have ROLE_INFRASTRUCTURE",
				ctx.getBeanDefinition("componentWithRole").getRole(), is(BeanDefinition.ROLE_INFRASTRUCTURE));
	}
}
