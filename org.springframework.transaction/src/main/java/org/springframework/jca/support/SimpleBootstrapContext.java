/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jca.support;

import java.util.Timer;

import javax.resource.spi.BootstrapContext;
import javax.resource.spi.UnavailableException;
import javax.resource.spi.XATerminator;
import javax.resource.spi.work.WorkManager;

/**
 * Simple implementation of the JCA 1.5 {@link javax.resource.spi.BootstrapContext}
 * interface, used for bootstrapping a JCA ResourceAdapter in a local environment.
 *
 * <p>Delegates to the given WorkManager and XATerminator, if any. Creates simple
 * local instances of <code>java.util.Timer</code>.
 *
 * @author Juergen Hoeller
 * @since 2.0.3
 * @see javax.resource.spi.ResourceAdapter#start(javax.resource.spi.BootstrapContext)
 * @see ResourceAdapterFactoryBean
 */
public class SimpleBootstrapContext implements BootstrapContext {

	private WorkManager workManager;

	private XATerminator xaTerminator;


	/**
	 * Create a new SimpleBootstrapContext for the given WorkManager,
	 * with no XATerminator available.
	 * @param workManager the JCA WorkManager to use (may be <code>null</code>)
	 */
	public SimpleBootstrapContext(WorkManager workManager) {
		this.workManager = workManager;
	}

	/**
	 * Create a new SimpleBootstrapContext for the given WorkManager and XATerminator.
	 * @param workManager the JCA WorkManager to use (may be <code>null</code>)
	 * @param xaTerminator the JCA XATerminator to use (may be <code>null</code>)
	 */
	public SimpleBootstrapContext(WorkManager workManager, XATerminator xaTerminator) {
		this.workManager = workManager;
		this.xaTerminator = xaTerminator;
	}


	public WorkManager getWorkManager() {
		if (this.workManager == null) {
			throw new IllegalStateException("No WorkManager available");
		}
		return this.workManager;
	}

	public XATerminator getXATerminator() {
		return this.xaTerminator;
	}

	public Timer createTimer() throws UnavailableException {
		return new Timer();
	}

}
