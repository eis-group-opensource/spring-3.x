/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scheduling.annotation;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * Tests use of @EnableScheduling on @Configuration classes.
 *
 * @author Chris Beams
 * @since 3.1
 */
public class EnableSchedulingTests {

	@Test
	public void withFixedRateTask() throws InterruptedException {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(FixedRateTaskConfig.class);
		ctx.refresh();

		Thread.sleep(100);
		assertThat(ctx.getBean(AtomicInteger.class).get(), greaterThanOrEqualTo(10));
		ctx.close();
	}


	@EnableScheduling @Configuration
	static class FixedRateTaskConfig {

		@Bean
		public AtomicInteger counter() {
			return new AtomicInteger();
		}

		@Scheduled(fixedRate=10)
		public void task() {
			counter().incrementAndGet();
		}
	}


	@Test
	public void withSubclass() throws InterruptedException {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(FixedRateTaskConfigSubclass.class);
		ctx.refresh();

		Thread.sleep(100);
		assertThat(ctx.getBean(AtomicInteger.class).get(), greaterThanOrEqualTo(10));
		ctx.close();
	}


	@Configuration
	static class FixedRateTaskConfigSubclass extends FixedRateTaskConfig {
	}


	@Test
	public void withExplicitScheduler() throws InterruptedException {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(ExplicitSchedulerConfig.class);
		ctx.refresh();

		Thread.sleep(100);
		assertThat(ctx.getBean(AtomicInteger.class).get(), greaterThanOrEqualTo(10));
		assertThat(ctx.getBean(ExplicitSchedulerConfig.class).threadName, startsWith("explicitScheduler-"));
		ctx.close();
	}


	@EnableScheduling @Configuration
	static class ExplicitSchedulerConfig {

		String threadName;

		@Bean
		public TaskScheduler taskScheduler() {
			ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
			scheduler.setThreadNamePrefix("explicitScheduler-");
			return scheduler;
		}

		@Bean
		public AtomicInteger counter() {
			return new AtomicInteger();
		}

		@Scheduled(fixedRate=10)
		public void task() {
			threadName = Thread.currentThread().getName();
			counter().incrementAndGet();
		}
	}


	@Test(expected=IllegalStateException.class)
	public void withExplicitSchedulerAmbiguity_andSchedulingEnabled() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(AmbiguousExplicitSchedulerConfig.class);
		try {
			ctx.refresh();
		} catch (IllegalStateException ex) {
			assertThat(ex.getMessage(), startsWith("More than one TaskScheduler"));
			throw ex;
		}
	}

	@EnableScheduling @Configuration
	static class AmbiguousExplicitSchedulerConfig {

		String threadName;

		@Bean
		public TaskScheduler taskScheduler1() {
			ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
			scheduler.setThreadNamePrefix("explicitScheduler1");
			return scheduler;
		}

		@Bean
		public TaskScheduler taskScheduler2() {
			ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
			scheduler.setThreadNamePrefix("explicitScheduler2");
			return scheduler;
		}

		@Bean
		public AtomicInteger counter() {
			return new AtomicInteger();
		}

		@Scheduled(fixedRate=10)
		public void task() {
			threadName = Thread.currentThread().getName();
			counter().incrementAndGet();
		}
	}


	@Test
	public void withExplicitScheduledTaskRegistrar() throws InterruptedException {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(ExplicitScheduledTaskRegistrarConfig.class);
		ctx.refresh();

		Thread.sleep(100);
		assertThat(ctx.getBean(AtomicInteger.class).get(), greaterThanOrEqualTo(10));
		assertThat(ctx.getBean(ExplicitScheduledTaskRegistrarConfig.class).threadName, startsWith("explicitScheduler1"));
		ctx.close();
	}


	@EnableScheduling @Configuration
	static class ExplicitScheduledTaskRegistrarConfig implements SchedulingConfigurer {

		String threadName;

		@Bean
		public TaskScheduler taskScheduler1() {
			ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
			scheduler.setThreadNamePrefix("explicitScheduler1");
			return scheduler;
		}

		@Bean
		public TaskScheduler taskScheduler2() {
			ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
			scheduler.setThreadNamePrefix("explicitScheduler2");
			return scheduler;
		}

		@Bean
		public AtomicInteger counter() {
			return new AtomicInteger();
		}

		@Scheduled(fixedRate=10)
		public void task() {
			threadName = Thread.currentThread().getName();
			counter().incrementAndGet();
		}

		public Object getScheduler() {
			return null;
		}

		public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
			taskRegistrar.setScheduler(taskScheduler1());
		}

	}


	@Test
	public void withAmbiguousTaskSchedulers_butNoActualTasks() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(SchedulingEnabled_withAmbiguousTaskSchedulers_butNoActualTasks.class);
		ctx.refresh();
	}


	@Configuration
	@EnableScheduling
	static class SchedulingEnabled_withAmbiguousTaskSchedulers_butNoActualTasks {

		@Bean
		public TaskScheduler taskScheduler1() {
			ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
			scheduler.setThreadNamePrefix("explicitScheduler1");
			return scheduler;
		}

		@Bean
		public TaskScheduler taskScheduler2() {
			ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
			scheduler.setThreadNamePrefix("explicitScheduler2");
			return scheduler;
		}
	}


	@Test(expected=IllegalStateException.class)
	public void withAmbiguousTaskSchedulers_andSingleTask() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(SchedulingEnabled_withAmbiguousTaskSchedulers_andSingleTask.class);
		try {
			ctx.refresh();
		} catch (IllegalStateException ex) {
			assertThat(ex.getMessage(), startsWith("More than one TaskScheduler and/or"));
			throw ex;
		}
	}


	@Configuration
	@EnableScheduling
	static class SchedulingEnabled_withAmbiguousTaskSchedulers_andSingleTask {

		@Scheduled(fixedRate=10L)
		public void task() {
		}

		@Bean
		public TaskScheduler taskScheduler1() {
			ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
			scheduler.setThreadNamePrefix("explicitScheduler1");
			return scheduler;
		}

		@Bean
		public TaskScheduler taskScheduler2() {
			ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
			scheduler.setThreadNamePrefix("explicitScheduler2");
			return scheduler;
		}
	}

	@Test
	public void withAmbiguousTaskSchedulers_andSingleTask_disambiguatedByScheduledTaskRegistrarBean() throws InterruptedException {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(SchedulingEnabled_withAmbiguousTaskSchedulers_andSingleTask_disambiguatedByScheduledTaskRegistrar.class);
		ctx.refresh();
		Thread.sleep(20);
		ThreadAwareWorker worker = ctx.getBean(ThreadAwareWorker.class);
		ctx.close();
		assertThat(worker.executedByThread, startsWith("explicitScheduler2-"));
	}


	static class ThreadAwareWorker {
		String executedByThread;
	}


	@Configuration
	@EnableScheduling
	static class SchedulingEnabled_withAmbiguousTaskSchedulers_andSingleTask_disambiguatedByScheduledTaskRegistrar implements SchedulingConfigurer {

		@Scheduled(fixedRate=10)
		public void task() {
			worker().executedByThread = Thread.currentThread().getName();
		}

		@Bean
		public ThreadAwareWorker worker() {
			return new ThreadAwareWorker();
		}

		public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
			taskRegistrar.setScheduler(taskScheduler2());
		}

		@Bean
		public TaskScheduler taskScheduler1() {
			ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
			scheduler.setThreadNamePrefix("explicitScheduler1-");
			return scheduler;
		}

		@Bean
		public TaskScheduler taskScheduler2() {
			ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
			scheduler.setThreadNamePrefix("explicitScheduler2-");
			return scheduler;
		}
	}


	@Test
	public void withAmbiguousTaskSchedulers_andSingleTask_disambiguatedBySchedulerNameAttribute() throws InterruptedException {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(SchedulingEnabled_withAmbiguousTaskSchedulers_andSingleTask_disambiguatedBySchedulerNameAttribute.class);
		ctx.refresh();
		Thread.sleep(20);
		ThreadAwareWorker worker = ctx.getBean(ThreadAwareWorker.class);
		ctx.close();
		assertThat(worker.executedByThread, startsWith("explicitScheduler2-"));
	}


	@Configuration
	@EnableScheduling
	static class SchedulingEnabled_withAmbiguousTaskSchedulers_andSingleTask_disambiguatedBySchedulerNameAttribute implements SchedulingConfigurer {

		@Scheduled(fixedRate=10)
		public void task() {
			worker().executedByThread = Thread.currentThread().getName();
		}

		@Bean
		public ThreadAwareWorker worker() {
			return new ThreadAwareWorker();
		}

		@Bean
		public TaskScheduler taskScheduler1() {
			ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
			scheduler.setThreadNamePrefix("explicitScheduler1-");
			return scheduler;
		}

		@Bean
		public TaskScheduler taskScheduler2() {
			ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
			scheduler.setThreadNamePrefix("explicitScheduler2-");
			return scheduler;
		}

		public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
			taskRegistrar.setScheduler(taskScheduler2());
		}
	}


	@Test
	public void withTriggerTask() throws InterruptedException {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(TriggerTaskConfig.class);
		ctx.refresh();

		Thread.sleep(100);
		assertThat(ctx.getBean(AtomicInteger.class).get(), greaterThan(1));
		ctx.close();
	}


	@Configuration
	static class TriggerTaskConfig {

		@Bean
		public AtomicInteger counter() {
			return new AtomicInteger();
		}

		@Bean
		public TaskScheduler scheduler() {
			ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
			scheduler.initialize();
			scheduler.schedule(
				new Runnable() {
					public void run() {
						counter().incrementAndGet();
					}
				},
				new Trigger() {
					public Date nextExecutionTime(TriggerContext triggerContext) {
						return new Date(new Date().getTime()+10);
					}
				});
			return scheduler;
		}
	}
}
