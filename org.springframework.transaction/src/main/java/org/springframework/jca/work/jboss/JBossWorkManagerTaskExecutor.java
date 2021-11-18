/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jca.work.jboss;

import javax.resource.spi.work.WorkManager;

import org.springframework.jca.work.WorkManagerTaskExecutor;

/**
 * Spring TaskExecutor adapter for the JBoss JCA WorkManager.
 * Can be defined in web applications to make a TaskExecutor reference
 * available, talking to the JBoss WorkManager (thread pool) underneath.
 *
 * <p>This is the JBoss equivalent of the CommonJ
 * {@link org.springframework.scheduling.commonj.WorkManagerTaskExecutor}
 * adapter for WebLogic and WebSphere.
 *
 * @author Juergen Hoeller
 * @since 2.5.2
 * @see org.jboss.resource.work.JBossWorkManagerMBean
 */
public class JBossWorkManagerTaskExecutor extends WorkManagerTaskExecutor {

	/**
	 * Identify a specific JBossWorkManagerMBean to talk to,
	 * through its JMX object name.
	 * <p>The default MBean name is "jboss.jca:service=WorkManager".
	 * @see JBossWorkManagerUtils#getWorkManager(String)
	 */
	public void setWorkManagerMBeanName(String mbeanName) {
		setWorkManager(JBossWorkManagerUtils.getWorkManager(mbeanName));
	}

	/**
	 * Obtains the default JBoss JCA WorkManager through a JMX lookup
	 * for the JBossWorkManagerMBean.
	 * @see JBossWorkManagerUtils#getWorkManager()
	 */
	@Override
	protected WorkManager getDefaultWorkManager() {
		return JBossWorkManagerUtils.getWorkManager();
	}

}
