/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context;

/**
 * Interface defining methods for start/stop lifecycle control.
 * The typical use case for this is to control asynchronous processing.
 *
 * <p>Can be implemented by both components (typically a Spring bean defined in
 * a Spring {@link org.springframework.beans.factory.BeanFactory}) and containers
 * (typically a Spring {@link ApplicationContext}). Containers will propagate
 * start/stop signals to all components that apply.
 *
 * <p>Can be used for direct invocations or for management operations via JMX.
 * In the latter case, the {@link org.springframework.jmx.export.MBeanExporter}
 * will typically be defined with an
 * {@link org.springframework.jmx.export.assembler.InterfaceBasedMBeanInfoAssembler},
 * restricting the visibility of activity-controlled components to the Lifecycle
 * interface.
 *
 * <p>Note that the Lifecycle interface is only supported on <b>top-level singleton beans</b>.
 * On any other component, the Lifecycle interface will remain undetected and hence ignored.
 * Also, note that the extended {@link SmartLifecycle} interface provides more sophisticated
 * integration with the container's startup and shutdown phases.
 *
 * @author Juergen Hoeller
 * @since 2.0
 * @see SmartLifecycle
 * @see ConfigurableApplicationContext
 * @see org.springframework.jms.listener.AbstractMessageListenerContainer
 * @see org.springframework.scheduling.quartz.SchedulerFactoryBean
 */
public interface Lifecycle {

	/**
	 * Start this component.
	 * Should not throw an exception if the component is already running.
	 * <p>In the case of a container, this will propagate the start signal
	 * to all components that apply.
	 */
	void start();

	/**
	 * Stop this component, typically in a synchronous fashion, such that
	 * the component is fully stopped upon return of this method. Consider
	 * implementing {@link SmartLifecycle} and its {@code stop(Runnable)}
	 * variant in cases where asynchronous stop behavior is necessary.
	 * <p>Should not throw an exception if the component isn't started yet.
	 * <p>In the case of a container, this will propagate the stop signal
	 * to all components that apply.
	 * @see SmartLifecycle#stop(Runnable)
	 */
	void stop();

	/**
	 * Check whether this component is currently running.
	 * <p>In the case of a container, this will return <code>true</code>
	 * only if <i>all</i> components that apply are currently running.
	 * @return whether the component is currently running
	 */
	boolean isRunning();

}
