/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scheduling.annotation;

import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * Interface to be implemented by @{@link org.springframework.context.annotation.Configuration}
 * classes annotated with @{@link EnableScheduling} that wish to register scheduled tasks
 * in a <em>programmatic</em> fashion as opposed to the <em>declarative</em> approach of
 * using the @{@link Scheduled} annotation. For example, this may be necessary when
 * implementing {@link org.springframework.scheduling.Trigger Trigger}-based tasks, which
 * are not supported by the {@code @Scheduled} annotation.
 *
 * <p>See @{@link EnableScheduling} for detailed usage examples.
 *
 * @author Chris Beams
 * @since 3.1
 * @see EnableScheduling
 * @see ScheduledTaskRegistrar
 */
public interface SchedulingConfigurer {

	void configureTasks(ScheduledTaskRegistrar taskRegistrar);

}
