/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scheduling.timer;

import java.util.TimerTask;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.scheduling.support.MethodInvokingRunnable;

/**
 * {@link FactoryBean} that exposes a {@link TimerTask} object which
 * delegates job execution to a specified (static or non-static) method.
 * Avoids the need to implement a one-line TimerTask that just invokes
 * an existing business method.
 *
 * <p>Derives from {@link MethodInvokingRunnable} to share common properties
 * and behavior, effectively providing a TimerTask adapter for it.
 *
 * @author Juergen Hoeller
 * @since 19.02.2004
 * @see DelegatingTimerTask
 * @see ScheduledTimerTask#setTimerTask
 * @see ScheduledTimerTask#setRunnable
 * @see org.springframework.scheduling.support.MethodInvokingRunnable
 * @see org.springframework.beans.factory.config.MethodInvokingFactoryBean
 * @deprecated as of Spring 3.0, in favor of the <code>scheduling.concurrent</code>
 * package which is based on Java 5's <code>java.util.concurrent.ExecutorService</code>
 */
@Deprecated
public class MethodInvokingTimerTaskFactoryBean extends MethodInvokingRunnable implements FactoryBean<TimerTask> {

	private TimerTask timerTask;


	@Override
	public void afterPropertiesSet() throws ClassNotFoundException, NoSuchMethodException {
		super.afterPropertiesSet();
		this.timerTask = new DelegatingTimerTask(this);
	}


	public TimerTask getObject() {
		return this.timerTask;
	}

	public Class<TimerTask> getObjectType() {
		return TimerTask.class;
	}

	public boolean isSingleton() {
		return true;
	}

}
