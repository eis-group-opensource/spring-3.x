/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.context.annotation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.annotation.Inherited;

import org.junit.Test;
import org.springframework.beans.factory.parsing.BeanDefinitionParsingException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Tests regarding overloading and overriding of bean methods.
 * Related to SPR-6618.
 *
 * Bean-annotated methods should be able to be overridden, just as any regular
 * method. This is straightforward.
 *
 * Bean-annotated methods should be able to be overloaded, though supporting this
 * is more subtle. Essentially, it must be unambiguous to the container which bean
 * method to call.  A simple way to think about this is that no one Configuration
 * class may declare two bean methods with the same name.  In the case of inheritance,
 * the most specific subclass bean method will always be the one that is invoked.
 *
 * @author Chris Beams
 */
public class BeanMethodPolymorphismTests {

	@Test
	public void beanMethodOverloadingWithoutInheritance() {
		@SuppressWarnings("unused")
		@Configuration class Config {
			@Bean String aString() { return "na"; }
			@Bean String aString(Integer dependency) { return "na"; }
		}
		try {
			new AnnotationConfigApplicationContext(Config.class);
			fail("expected bean method overloading exception");
		} catch (BeanDefinitionParsingException ex) {
			assertTrue(ex.getMessage(), ex.getMessage().contains("2 overloaded @Bean methods named 'aString'"));
		}
	}

	@Test
	public void beanMethodOverloadingWithInheritance() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SubConfig.class);
		assertThat(ctx.getBean(String.class), equalTo("overloaded5"));
	}
	static @Configuration class SuperConfig {
		@Bean String aString() { return "super"; }
	}
	static @Configuration class SubConfig {
		@Bean Integer anInt() { return 5; }
		@Bean String aString(Integer dependency) { return "overloaded"+dependency; }
	}

	/**
	 * When inheritance is not involved, it is still possible to override a bean method from
	 * the container's point of view. This is not strictly 'overloading' of a method per se,
	 * so it's referred to here as 'shadowing' to distinguish the difference.
	 */
	@Test
	public void beanMethodShadowing() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(ShadowConfig.class);
		assertThat(ctx.getBean(String.class), equalTo("shadow"));
	}
	@Import(SubConfig.class)
	static @Configuration class ShadowConfig {
		@Bean String aString() { return "shadow"; }
	}

	/**
	 * Tests that polymorphic Configuration classes need not explicitly redeclare the
	 * {@link Configuration} annotation. This respects the {@link Inherited} nature
	 * of the Configuration annotation, even though it's being detected via ASM.
	 */
	@Test
	public void beanMethodsDetectedOnSuperClass() {
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		beanFactory.registerBeanDefinition("config", new RootBeanDefinition(Config.class));
		ConfigurationClassPostProcessor pp = new ConfigurationClassPostProcessor();
		pp.postProcessBeanFactory(beanFactory);
		beanFactory.getBean("testBean", TestBean.class);
	}


	@Configuration
	static class BaseConfig {

		@Bean
		public TestBean testBean() {
			return new TestBean();
		}
	}


	@Configuration
	static class Config extends BaseConfig {
	}

}
