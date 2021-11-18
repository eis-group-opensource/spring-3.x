/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jca.work.glassfish;

import java.lang.reflect.Method;
import javax.resource.spi.work.WorkManager;

import org.springframework.jca.work.WorkManagerTaskExecutor;
import org.springframework.util.ReflectionUtils;

/**
 * Spring TaskExecutor adapter for the GlassFish JCA WorkManager.
 * Can be defined in web applications to make a TaskExecutor reference
 * available, talking to the GlassFish WorkManager (thread pool) underneath.
 *
 * <p>This is the GlassFish equivalent of the CommonJ
 * {@link org.springframework.scheduling.commonj.WorkManagerTaskExecutor}
 * adapter for WebLogic and WebSphere.
 *
 * @author Juergen Hoeller
 * @since 2.5.2
 * @see com.sun.enterprise.connectors.work.WorkManagerFactory
 */
public class GlassFishWorkManagerTaskExecutor extends WorkManagerTaskExecutor {

	private static final String WORK_MANAGER_FACTORY_CLASS = "com.sun.enterprise.connectors.work.WorkManagerFactory";

	private final Method getWorkManagerMethod;


	public GlassFishWorkManagerTaskExecutor() {
		try {
			Class wmf = getClass().getClassLoader().loadClass(WORK_MANAGER_FACTORY_CLASS);
			this.getWorkManagerMethod = wmf.getMethod("getWorkManager", new Class[] {String.class});
		}
		catch (Exception ex) {
			throw new IllegalStateException(
					"Could not initialize GlassFishWorkManagerTaskExecutor because GlassFish API is not available: " + ex);
		}
	}

	/**
	 * Identify a specific GlassFish thread pool to talk to.
	 * <p>The thread pool name matches the resource adapter name
	 * in default RAR deployment scenarios.
	 */
	public void setThreadPoolName(String threadPoolName) {
		WorkManager wm = (WorkManager) ReflectionUtils.invokeMethod(this.getWorkManagerMethod, null, threadPoolName);
		if (wm == null) {
			throw new IllegalArgumentException("Specified thread pool name '" + threadPoolName +
					"' does not correspond to an actual pool definition in GlassFish. Check your configuration!");
		}
		setWorkManager(wm);
	}

	/**
	 * Obtains GlassFish's default thread pool.
	 */
	@Override
	protected WorkManager getDefaultWorkManager() {
		return (WorkManager) ReflectionUtils.invokeMethod(this.getWorkManagerMethod, null, new Object[] {null});
	}

}
