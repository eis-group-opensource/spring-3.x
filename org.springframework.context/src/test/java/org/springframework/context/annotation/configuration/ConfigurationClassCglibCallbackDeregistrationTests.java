/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation.configuration;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * Tests ensuring that @Configuration-related CGLIB callbacks are de-registered
 * at container shutdown time, allowing for proper garbage collection. See SPR-7901.
 *
 * @author Chris Beams
 */
public class ConfigurationClassCglibCallbackDeregistrationTests {

	/**
	 * asserting that the actual callback is deregistered is difficult,
	 * but we can at least assert that the @Configuration class is enhanced
	 * to implement DisposableBean. The enhanced implementation of destroy()
	 * will do the de-registration work.
	 */
	@Test
	public void destroyContext() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class);
		Config config = ctx.getBean(Config.class);
		assertThat(config, instanceOf(DisposableBean.class));
		ctx.destroy();
	}

	/**
	 * The DisposableBeanMethodInterceptor in ConfigurationClassEnhancer
	 * should be careful to invoke any explicit super-implementation of
	 * DisposableBean#destroy().
	 */
	@Test
	public void destroyExplicitDisposableBeanConfig() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(DisposableConfig.class);
		DisposableConfig config = ctx.getBean(DisposableConfig.class);
		assertThat(config.destroyed, is(false));
		ctx.destroy();
		assertThat("DisposableConfig.destroy() was not invoked", config.destroyed, is(true));
	}


	@Configuration
	static class Config {
	}


	@Configuration
	static class DisposableConfig implements DisposableBean {
		boolean destroyed = false;
		public void destroy() throws Exception {
			this.destroyed = true;
		}
	}
}
