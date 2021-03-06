/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scheduling.annotation;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.MethodCallback;
import org.springframework.util.StringValueResolver;

/**
 * Bean post-processor that registers methods annotated with @{@link Scheduled}
 * to be invoked by a {@link org.springframework.scheduling.TaskScheduler} according
 * to the "fixedRate", "fixedDelay", or "cron" expression provided via the annotation.
 *
 * <p>This post-processor is automatically registered by Spring's
 * {@code <task:annotation-driven>} XML element, and also by the @{@link EnableScheduling}
 * annotation.
 *
 * <p>Auto-detects any {@link SchedulingConfigurer} instances in the container,
 * allowing for customization of the scheduler to be used or for fine-grained control
 * over task registration (e.g. registration of {@link Trigger} tasks.
 * See @{@link EnableScheduling} Javadoc for complete usage details.
 *
 * @author Mark Fisher
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 3.0
 * @see Scheduled
 * @see EnableScheduling
 * @see SchedulingConfigurer
 * @see org.springframework.scheduling.TaskScheduler
 * @see org.springframework.scheduling.config.ScheduledTaskRegistrar
 */
public class ScheduledAnnotationBeanPostProcessor
		implements BeanPostProcessor, Ordered, EmbeddedValueResolverAware, ApplicationContextAware,
		ApplicationListener<ContextRefreshedEvent>, DisposableBean {

	private Object scheduler;

	private StringValueResolver embeddedValueResolver;

	private ApplicationContext applicationContext;

	private ScheduledTaskRegistrar registrar;

	private final Map<Runnable, String> cronTasks = new HashMap<Runnable, String>();

	private final Map<Runnable, Long> fixedDelayTasks = new HashMap<Runnable, Long>();

	private final Map<Runnable, Long> fixedRateTasks = new HashMap<Runnable, Long>();


	/**
	 * Set the {@link org.springframework.scheduling.TaskScheduler} that will invoke
	 * the scheduled methods, or a {@link java.util.concurrent.ScheduledExecutorService}
	 * to be wrapped as a TaskScheduler.
	 */
	public void setScheduler(Object scheduler) {
		this.scheduler = scheduler;
	}

	public void setEmbeddedValueResolver(StringValueResolver resolver) {
		this.embeddedValueResolver = resolver;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public int getOrder() {
		return LOWEST_PRECEDENCE;
	}


	public Object postProcessBeforeInitialization(Object bean, String beanName) {
		return bean;
	}

	public Object postProcessAfterInitialization(final Object bean, String beanName) {
		final Class<?> targetClass = AopUtils.getTargetClass(bean);
		ReflectionUtils.doWithMethods(targetClass, new MethodCallback() {
			public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
				Scheduled annotation = AnnotationUtils.getAnnotation(method, Scheduled.class);
				if (annotation != null) {
					Assert.isTrue(void.class.equals(method.getReturnType()),
							"Only void-returning methods may be annotated with @Scheduled.");
					Assert.isTrue(method.getParameterTypes().length == 0,
							"Only no-arg methods may be annotated with @Scheduled.");
					if (AopUtils.isJdkDynamicProxy(bean)) {
						try {
							// found a @Scheduled method on the target class for this JDK proxy -> is it
							// also present on the proxy itself?
							method = bean.getClass().getMethod(method.getName(), method.getParameterTypes());
						} catch (SecurityException ex) {
							ReflectionUtils.handleReflectionException(ex);
						} catch (NoSuchMethodException ex) {
							throw new IllegalStateException(String.format(
									"@Scheduled method '%s' found on bean target class '%s', " +
									"but not found in any interface(s) for bean JDK proxy. Either " +
									"pull the method up to an interface or switch to subclass (CGLIB) " +
									"proxies by setting proxy-target-class/proxyTargetClass " +
									"attribute to 'true'", method.getName(), targetClass.getSimpleName()));
						}
					}
					Runnable runnable = new ScheduledMethodRunnable(bean, method);
					boolean processedSchedule = false;
					String errorMessage = "Exactly one of 'cron', 'fixedDelay', or 'fixedRate' is required.";
					String cron = annotation.cron();
					if (!"".equals(cron)) {
						processedSchedule = true;
						if (embeddedValueResolver != null) {
							cron = embeddedValueResolver.resolveStringValue(cron);
						}
						cronTasks.put(runnable, cron);
					}
					long fixedDelay = annotation.fixedDelay();
					if (fixedDelay >= 0) {
						Assert.isTrue(!processedSchedule, errorMessage);
						processedSchedule = true;
						fixedDelayTasks.put(runnable, fixedDelay);
					}
					long fixedRate = annotation.fixedRate();
					if (fixedRate >= 0) {
						Assert.isTrue(!processedSchedule, errorMessage);
						processedSchedule = true;
						fixedRateTasks.put(runnable, fixedRate);
					}
					Assert.isTrue(processedSchedule, errorMessage);
				}
			}
		});
		return bean;
	}

	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext() != this.applicationContext) {
			return;
		}

		Map<String, SchedulingConfigurer> configurers = applicationContext.getBeansOfType(SchedulingConfigurer.class);

		if (this.cronTasks.isEmpty() && this.fixedDelayTasks.isEmpty() &&
				this.fixedRateTasks.isEmpty() && configurers.isEmpty()) {
			return;
		}

		this.registrar = new ScheduledTaskRegistrar();
		this.registrar.setCronTasks(this.cronTasks);
		this.registrar.setFixedDelayTasks(this.fixedDelayTasks);
		this.registrar.setFixedRateTasks(this.fixedRateTasks);

		if (this.scheduler != null) {
			this.registrar.setScheduler(this.scheduler);
		}

		for (SchedulingConfigurer configurer : configurers.values()) {
			configurer.configureTasks(this.registrar);
		}

		if (registrar.getScheduler() == null) {
			Map<String, ? super Object> schedulers = new HashMap<String, Object>();
			schedulers.putAll(applicationContext.getBeansOfType(TaskScheduler.class));
			schedulers.putAll(applicationContext.getBeansOfType(ScheduledExecutorService.class));
			if (schedulers.size() == 0) {
				// do nothing -> fall back to default scheduler
			} else if (schedulers.size() == 1) {
				this.registrar.setScheduler(schedulers.values().iterator().next());
			} else if (schedulers.size() >= 2){
				throw new IllegalStateException("More than one TaskScheduler and/or ScheduledExecutorService  " +
						"exist within the context. Remove all but one of the beans; or implement the " +
						"SchedulingConfigurer interface and call ScheduledTaskRegistrar#setScheduler " +
						"explicitly within the configureTasks() callback. Found the following beans: " + schedulers.keySet());
			}
		}

		this.registrar.afterPropertiesSet();
	}

	public void destroy() throws Exception {
		if (this.registrar != null) {
			this.registrar.destroy();
		}
	}

}
