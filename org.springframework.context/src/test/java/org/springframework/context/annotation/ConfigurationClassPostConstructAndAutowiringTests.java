/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.springframework.beans.TestBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests cornering the issue reported in SPR-8080. If the product of a @Bean method
 * was @Autowired into a configuration class while at the same time the declaring
 * configuration class for the @Bean method in question has a @PostConstruct
 * (or other initializer) method, the container would become confused about the
 * 'currently in creation' status of the autowired bean and result in creating multiple
 * instances of the given @Bean, violating container scoping / singleton semantics.
 *
 * This is resolved through no longer relying on 'currently in creation' status, but
 * rather on a thread local that informs the enhanced bean method implementation whether
 * the factory is the caller or not.
 *
 * @author Chris Beams
 * @since 3.1
 */
public class ConfigurationClassPostConstructAndAutowiringTests {

	/**
	 * Prior to the fix for SPR-8080, this method would succeed due to ordering of
	 * configuration class registration.
	 */
	@Test
	public void control() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(Config1.class, Config2.class);
		ctx.refresh();

		assertions(ctx);

		Config2 config2 = ctx.getBean(Config2.class);
		assertThat(config2.testBean, is(ctx.getBean(TestBean.class)));
	}

	/**
	 * Prior to the fix for SPR-8080, this method would fail due to ordering of
	 * configuration class registration.
	 */
	@Test
	public void originalReproCase() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(Config2.class, Config1.class);
		ctx.refresh();

		assertions(ctx);
	}

	private void assertions(AnnotationConfigApplicationContext ctx) {
		Config1 config1 = ctx.getBean(Config1.class);
		TestBean testBean = ctx.getBean(TestBean.class);
		assertThat(config1.beanMethodCallCount, is(1));
		assertThat(testBean.getAge(), is(2));
	}


	@Configuration
	static class Config1 {

		int beanMethodCallCount = 0;

		@PostConstruct
		public void init() {
			beanMethod().setAge(beanMethod().getAge() + 1); // age == 2
		}

		@Bean
		public TestBean beanMethod() {
			beanMethodCallCount++;
			TestBean testBean = new TestBean();
			testBean.setAge(1);
			return testBean;
		}
	}


	@Configuration
	static class Config2 {
		TestBean testBean;

		@Autowired
		void setTestBean(TestBean testBean) {
			this.testBean = testBean;
		}
	}

}
