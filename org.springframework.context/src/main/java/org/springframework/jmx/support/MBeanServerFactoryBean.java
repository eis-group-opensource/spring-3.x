/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.support;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jmx.MBeanServerNotFoundException;

/**
 * {@link FactoryBean} that obtains an {@link javax.management.MBeanServer} reference
 * through the standard JMX 1.2 {@link javax.management.MBeanServerFactory}
 * API (which is available on JDK 1.5 or as part of a JMX 1.2 provider).
 * Exposes the <code>MBeanServer</code> for bean references.
 *
 * <p>By default, <code>MBeanServerFactoryBean</code> will always create
 * a new <code>MBeanServer</code> even if one is already running. To have
 * the <code>MBeanServerFactoryBean</code> attempt to locate a running
 * <code>MBeanServer</code> first, set the value of the
 * "locateExistingServerIfPossible" property to "true".
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 1.2
 * @see #setLocateExistingServerIfPossible
 * @see #locateMBeanServer
 * @see javax.management.MBeanServer
 * @see javax.management.MBeanServerFactory#findMBeanServer
 * @see javax.management.MBeanServerFactory#createMBeanServer
 * @see javax.management.MBeanServerFactory#newMBeanServer
 * @see MBeanServerConnectionFactoryBean
 * @see ConnectorServerFactoryBean
 */
public class MBeanServerFactoryBean implements FactoryBean<MBeanServer>, InitializingBean, DisposableBean {

	protected final Log logger = LogFactory.getLog(getClass());

	private boolean locateExistingServerIfPossible = false;

	private String agentId;

	private String defaultDomain;

	private boolean registerWithFactory = true;

	private MBeanServer server;

	private boolean newlyRegistered = false;


	/**
	 * Set whether or not the <code>MBeanServerFactoryBean</code> should attempt
	 * to locate a running <code>MBeanServer</code> before creating one.
	 * <p>Default is <code>false</code>.
	 */
	public void setLocateExistingServerIfPossible(boolean locateExistingServerIfPossible) {
		this.locateExistingServerIfPossible = locateExistingServerIfPossible;
	}

	/**
	 * Set the agent id of the <code>MBeanServer</code> to locate.
	 * <p>Default is none. If specified, this will result in an
	 * automatic attempt being made to locate the attendant MBeanServer,
	 * and (importantly) if said MBeanServer cannot be located no
	 * attempt will be made to create a new MBeanServer (and an
	 * MBeanServerNotFoundException will be thrown at resolution time).
	 * <p>Specifying the empty String indicates the platform MBeanServer.
	 * @see javax.management.MBeanServerFactory#findMBeanServer(String)
	 */
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	/**
	 * Set the default domain to be used by the <code>MBeanServer</code>,
	 * to be passed to <code>MBeanServerFactory.createMBeanServer()</code>
	 * or <code>MBeanServerFactory.findMBeanServer()<code>.
	 * <p>Default is none.
	 * @see javax.management.MBeanServerFactory#createMBeanServer(String)
	 * @see javax.management.MBeanServerFactory#findMBeanServer(String)
	 */
	public void setDefaultDomain(String defaultDomain) {
		this.defaultDomain = defaultDomain;
	}

	/**
	 * Set whether to register the <code>MBeanServer</code> with the
	 * <code>MBeanServerFactory</code>, making it available through
	 * <code>MBeanServerFactory.findMBeanServer()<code>.
	 * @see javax.management.MBeanServerFactory#createMBeanServer
	 * @see javax.management.MBeanServerFactory#findMBeanServer
	 */
	public void setRegisterWithFactory(boolean registerWithFactory) {
		this.registerWithFactory = registerWithFactory;
	}


	/**
	 * Creates the <code>MBeanServer</code> instance.
	 */
	public void afterPropertiesSet() throws MBeanServerNotFoundException {
		// Try to locate existing MBeanServer, if desired.
		if (this.locateExistingServerIfPossible || this.agentId != null) {
			try {
				this.server = locateMBeanServer(this.agentId);
			}
			catch (MBeanServerNotFoundException ex) {
				// If agentId was specified, we were only supposed to locate that
				// specific MBeanServer; so let's bail if we can't find it.
				if (this.agentId != null) {
					throw ex;
				}
				logger.info("No existing MBeanServer found - creating new one");
			}
		}

		// Create a new MBeanServer and register it, if desired.
		if (this.server == null) {
			this.server = createMBeanServer(this.defaultDomain, this.registerWithFactory);
			this.newlyRegistered = this.registerWithFactory;
		}
	}

	/**
	 * Attempt to locate an existing <code>MBeanServer</code>.
	 * Called if <code>locateExistingServerIfPossible</code> is set to <code>true</code>.
	 * <p>The default implementation attempts to find an <code>MBeanServer</code> using
	 * a standard lookup. Subclasses may override to add additional location logic.
	 * @param agentId the agent identifier of the MBeanServer to retrieve.
	 * If this parameter is <code>null</code>, all registered MBeanServers are
	 * considered.
	 * @return the <code>MBeanServer</code> if found
	 * @throws org.springframework.jmx.MBeanServerNotFoundException
	 * if no <code>MBeanServer</code> could be found
	 * @see #setLocateExistingServerIfPossible
	 * @see JmxUtils#locateMBeanServer(String)
	 * @see javax.management.MBeanServerFactory#findMBeanServer(String)
	 */
	protected MBeanServer locateMBeanServer(String agentId) throws MBeanServerNotFoundException {
		return JmxUtils.locateMBeanServer(agentId);
	}

	/**
	 * Create a new <code>MBeanServer</code> instance and register it with the
	 * <code>MBeanServerFactory</code>, if desired.
	 * @param defaultDomain the default domain, or <code>null</code> if none
	 * @param registerWithFactory whether to register the <code>MBeanServer</code>
	 * with the <code>MBeanServerFactory</code>
	 * @see javax.management.MBeanServerFactory#createMBeanServer
	 * @see javax.management.MBeanServerFactory#newMBeanServer
	 */
	protected MBeanServer createMBeanServer(String defaultDomain, boolean registerWithFactory) {
		if (registerWithFactory) {
			return MBeanServerFactory.createMBeanServer(defaultDomain);
		}
		else {
			return MBeanServerFactory.newMBeanServer(defaultDomain);
		}
	}


	public MBeanServer getObject() {
		return this.server;
	}

	public Class<? extends MBeanServer> getObjectType() {
		return (this.server != null ? this.server.getClass() : MBeanServer.class);
	}

	public boolean isSingleton() {
		return true;
	}


	/**
	 * Unregisters the <code>MBeanServer</code> instance, if necessary.
	 */
	public void destroy() {
		if (this.newlyRegistered) {
			MBeanServerFactory.releaseMBeanServer(this.server);
		}
	}

}
