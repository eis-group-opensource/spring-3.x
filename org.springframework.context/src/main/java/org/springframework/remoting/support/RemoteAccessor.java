/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.remoting.support;

/**
 * Abstract base class for classes that access a remote service.
 * Provides a "serviceInterface" bean property.
 *
 * <p>Note that the service interface being used will show some signs of
 * remotability, like the granularity of method calls that it offers.
 * Furthermore, it has to have serializable arguments etc.
 *
 * <p>Accessors are supposed to throw Spring's generic
 * {@link org.springframework.remoting.RemoteAccessException} in case
 * of remote invocation failure, provided that the service interface
 * does not declare <code>java.rmi.RemoteException</code>.
 *
 * @author Juergen Hoeller
 * @since 13.05.2003
 * @see org.springframework.remoting.RemoteAccessException
 * @see java.rmi.RemoteException
 */
public abstract class RemoteAccessor extends RemotingSupport {

	private Class serviceInterface;


	/**
	 * Set the interface of the service to access.
	 * The interface must be suitable for the particular service and remoting strategy.
	 * <p>Typically required to be able to create a suitable service proxy,
	 * but can also be optional if the lookup returns a typed proxy.
	 */
	public void setServiceInterface(Class serviceInterface) {
		if (serviceInterface != null && !serviceInterface.isInterface()) {
			throw new IllegalArgumentException("'serviceInterface' must be an interface");
		}
		this.serviceInterface = serviceInterface;
	}

	/**
	 * Return the interface of the service to access.
	 */
	public Class getServiceInterface() {
		return this.serviceInterface;
	}

}
