/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jca.cci.connection;

import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionSpec;

import org.springframework.core.NamedThreadLocal;

/**
 * An adapter for a target CCI {@link javax.resource.cci.ConnectionFactory},
 * applying the given ConnectionSpec to every standard <code>getConnection()</code>
 * call, that is, implicitly invoking <code>getConnection(ConnectionSpec)</code>
 * on the target. All other methods simply delegate to the corresponding methods
 * of the target ConnectionFactory.
 *
 * <p>Can be used to proxy a target JNDI ConnectionFactory that does not have a
 * ConnectionSpec configured. Client code can work with the ConnectionFactory
 * without passing in a ConnectionSpec on every <code>getConnection()</code> call.
 *
 * <p>In the following example, client code can simply transparently work with
 * the preconfigured "myConnectionFactory", implicitly accessing
 * "myTargetConnectionFactory" with the specified user credentials.
 *
 * <pre class="code">
 * &lt;bean id="myTargetConnectionFactory" class="org.springframework.jndi.JndiObjectFactoryBean"&gt;
 *   &lt;property name="jndiName" value="java:comp/env/cci/mycf"/&gt;
 * &lt;/bean>
 *
 * &lt;bean id="myConnectionFactory" class="org.springframework.jca.cci.connection.ConnectionSpecConnectionFactoryAdapter"&gt;
 *   &lt;property name="targetConnectionFactory" ref="myTargetConnectionFactory"/&gt;
 *   &lt;property name="connectionSpec"&gt;
 *     &lt;bean class="your.resource.adapter.ConnectionSpecImpl"&gt;
 *       &lt;property name="username" value="myusername"/&gt;
 *       &lt;property name="password" value="mypassword"/&gt;
 *     &lt;/bean&gt;
 *   &lt;/property&gt;
 * &lt;/bean&gt;</pre>
 *
 * <p>If the "connectionSpec" is empty, this proxy will simply delegate to the
 * standard <code>getConnection()</code> method of the target ConnectionFactory.
 * This can be used to keep a UserCredentialsConnectionFactoryAdapter bean definition
 * just for the <i>option</i> of implicitly passing in a ConnectionSpec if the
 * particular target ConnectionFactory requires it.
 *
 * @author Juergen Hoeller
 * @since 1.2
 * @see #getConnection
 */
public class ConnectionSpecConnectionFactoryAdapter extends DelegatingConnectionFactory {

	private ConnectionSpec connectionSpec;

	private final ThreadLocal<ConnectionSpec> threadBoundSpec =
			new NamedThreadLocal<ConnectionSpec>("Current CCI ConnectionSpec");


	/**
	 * Set the ConnectionSpec that this adapter should use for retrieving Connections.
	 * Default is none.
	 */
	public void setConnectionSpec(ConnectionSpec connectionSpec) {
		this.connectionSpec = connectionSpec;
	}

	/**
	 * Set a ConnectionSpec for this proxy and the current thread.
	 * The given ConnectionSpec will be applied to all subsequent
	 * <code>getConnection()</code> calls on this ConnectionFactory proxy.
	 * <p>This will override any statically specified "connectionSpec" property.
	 * @param spec the ConnectionSpec to apply
	 * @see #removeConnectionSpecFromCurrentThread
	 */
	public void setConnectionSpecForCurrentThread(ConnectionSpec spec) {
		this.threadBoundSpec.set(spec);
	}

	/**
	 * Remove any ConnectionSpec for this proxy from the current thread.
	 * A statically specified ConnectionSpec applies again afterwards.
	 * @see #setConnectionSpecForCurrentThread
	 */
	public void removeConnectionSpecFromCurrentThread() {
		this.threadBoundSpec.remove();
	}


	/**
	 * Determine whether there is currently a thread-bound ConnectionSpec,
	 * using it if available, falling back to the statically specified
	 * "connectionSpec" property else.
	 * @see #doGetConnection
	 */
	@Override
	public final Connection getConnection() throws ResourceException {
		ConnectionSpec threadSpec = this.threadBoundSpec.get();
		if (threadSpec != null) {
			return doGetConnection(threadSpec);
		}
		else {
			return doGetConnection(this.connectionSpec);
		}
	}

	/**
	 * This implementation delegates to the <code>getConnection(ConnectionSpec)</code>
	 * method of the target ConnectionFactory, passing in the specified user credentials.
	 * If the specified username is empty, it will simply delegate to the standard
	 * <code>getConnection()</code> method of the target ConnectionFactory.
	 * @param spec the ConnectionSpec to apply
	 * @return the Connection
	 * @see javax.resource.cci.ConnectionFactory#getConnection(javax.resource.cci.ConnectionSpec)
	 * @see javax.resource.cci.ConnectionFactory#getConnection()
	 */
	protected Connection doGetConnection(ConnectionSpec spec) throws ResourceException {
		if (getTargetConnectionFactory() == null) {
			throw new IllegalStateException("targetConnectionFactory is required");
		}
		if (spec != null) {
			return getTargetConnectionFactory().getConnection(spec);
		}
		else {
			return getTargetConnectionFactory().getConnection();
		}
	}

}
