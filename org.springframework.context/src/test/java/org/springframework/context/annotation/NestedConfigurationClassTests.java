/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import test.beans.TestBean;

/**
 * Tests ensuring that nested static @Configuration classes are automatically detected
 * and registered without the need for explicit registration or @Import. See SPR-8186.
 *
 * @author Chris Beams
 * @since 3.1
 */
public class NestedConfigurationClassTests {

	@Test
	public void oneLevelDeep() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(L0Config.L1Config.class);
		ctx.refresh();

		assertFalse(ctx.containsBean("l0Bean"));

		ctx.getBean(L0Config.L1Config.class);
		ctx.getBean("l1Bean");

		ctx.getBean(L0Config.L1Config.L2Config.class);
		ctx.getBean("l2Bean");

		// ensure that override order is correct
		assertThat(ctx.getBean("overrideBean", TestBean.class).getName(), is("override-l1"));
	}

	@Test
	public void twoLevelsDeep() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(L0Config.class);
		ctx.refresh();

		ctx.getBean(L0Config.class);
		ctx.getBean("l0Bean");

		ctx.getBean(L0Config.L1Config.class);
		ctx.getBean("l1Bean");

		ctx.getBean(L0Config.L1Config.L2Config.class);
		ctx.getBean("l2Bean");

		// ensure that override order is correct
		assertThat(ctx.getBean("overrideBean", TestBean.class).getName(), is("override-l0"));
	}

	@Test
	public void twoLevelsDeepWithInheritance() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(S1Config.class);
		ctx.refresh();

		ctx.getBean(S1Config.class);
		ctx.getBean("l0Bean");

		ctx.getBean(L0Config.L1Config.class);
		ctx.getBean("l1Bean");

		ctx.getBean(L0Config.L1Config.L2Config.class);
		ctx.getBean("l2Bean");

		// ensure that override order is correct
		assertThat(ctx.getBean("overrideBean", TestBean.class).getName(), is("override-s1"));
	}


	@Configuration
	static class L0Config {
		@Bean
		public TestBean l0Bean() {
			return new TestBean("l0");
		}

		@Bean
		public TestBean overrideBean() {
			return new TestBean("override-l0");
		}

		@Configuration
		static class L1Config {
			@Bean
			public TestBean l1Bean() {
				return new TestBean("l1");
			}

			@Bean
			public TestBean overrideBean() {
				return new TestBean("override-l1");
			}

			@Configuration
			protected static class L2Config {
				@Bean
				public TestBean l2Bean() {
					return new TestBean("l2");
				}

				@Bean
				public TestBean overrideBean() {
					return new TestBean("override-l2");
				}
			}
		}
	}


	@Configuration
	static class S1Config extends L0Config {
		@Bean
		public TestBean overrideBean() {
			return new TestBean("override-s1");
		}
	}

}