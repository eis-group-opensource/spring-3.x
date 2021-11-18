/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scheduling.annotation;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.Executor;

import org.junit.Test;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Tests use of @EnableAsync on @Configuration classes.
 *
 * @author Chris Beams
 * @since 3.1
 */
public class EnableAsyncTests {

	@Test
	public void proxyingOccurs() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(AsyncConfig.class);
		ctx.refresh();

		AsyncBean asyncBean = ctx.getBean(AsyncBean.class);
		assertThat(AopUtils.isAopProxy(asyncBean), is(true));
		asyncBean.work();
	}


	@Configuration
	@EnableAsync
	static class AsyncConfig {
		@Bean
		public AsyncBean asyncBean() {
			return new AsyncBean();
		}
	}


	static class AsyncBean {
		private Thread threadOfExecution;

		@Async
		public void work() {
			this.threadOfExecution = Thread.currentThread();
		}

		public Thread getThreadOfExecution() {
			return threadOfExecution;
		}
	}


	@Test
	public void asyncProcessorIsOrderedLowestPrecedenceByDefault() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(AsyncConfig.class);
		ctx.refresh();

		AsyncAnnotationBeanPostProcessor bpp = ctx.getBean(AsyncAnnotationBeanPostProcessor.class);
		assertThat(bpp.getOrder(), is(Ordered.LOWEST_PRECEDENCE));
	}


	@Test
	public void orderAttributeIsPropagated() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(OrderedAsyncConfig.class);
		ctx.refresh();

		AsyncAnnotationBeanPostProcessor bpp = ctx.getBean(AsyncAnnotationBeanPostProcessor.class);
		assertThat(bpp.getOrder(), is(Ordered.HIGHEST_PRECEDENCE));
	}


	@Configuration
	@EnableAsync(order=Ordered.HIGHEST_PRECEDENCE)
	static class OrderedAsyncConfig {
		@Bean
		public AsyncBean asyncBean() {
			return new AsyncBean();
		}
	}


	@Test
	public void customAsyncAnnotationIsPropagated() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(CustomAsyncAnnotationConfig.class);
		ctx.refresh();

		Object bean = ctx.getBean(CustomAsyncBean.class);
		assertTrue(AopUtils.isAopProxy(bean));
		boolean isAsyncAdvised = false;
		for (Advisor advisor : ((Advised)bean).getAdvisors()) {
			if (advisor instanceof AsyncAnnotationAdvisor) {
				isAsyncAdvised = true;
				break;
			}
		}
		assertTrue("bean was not async advised as expected", isAsyncAdvised);
	}


	@Configuration
	@EnableAsync(annotation=CustomAsync.class)
	static class CustomAsyncAnnotationConfig {
		@Bean
		public CustomAsyncBean asyncBean() {
			return new CustomAsyncBean();
		}
	}


	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@interface CustomAsync {
	}


	static class CustomAsyncBean {
		@CustomAsync
		public void work() {
		}
	}


	/**
	 * Fails with classpath errors on trying to classload AnnotationAsyncExecutionAspect
	 */
	@Test(expected=BeanDefinitionStoreException.class)
	public void aspectModeAspectJAttemptsToRegisterAsyncAspect() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(AspectJAsyncAnnotationConfig.class);
		ctx.refresh();
	}


	@Configuration
	@EnableAsync(mode=AdviceMode.ASPECTJ)
	static class AspectJAsyncAnnotationConfig {
		@Bean
		public AsyncBean asyncBean() {
			return new AsyncBean();
		}
	}


	@Test
	public void customExecutorIsPropagated() throws InterruptedException {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(CustomExecutorAsyncConfig.class);
		ctx.refresh();

		AsyncBean asyncBean = ctx.getBean(AsyncBean.class);
		asyncBean.work();
		Thread.sleep(500);
		ctx.close();
		assertThat(asyncBean.getThreadOfExecution().getName(), startsWith("Custom-"));
	}


	@Configuration
	@EnableAsync
	static class CustomExecutorAsyncConfig implements AsyncConfigurer {
		@Bean
		public AsyncBean asyncBean() {
			return new AsyncBean();
		}

		public Executor getAsyncExecutor() {
			ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
			executor.setThreadNamePrefix("Custom-");
			executor.initialize();
			return executor;
		}

	}
}
