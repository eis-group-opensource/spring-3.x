/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.remoting.rmi;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.util.Assert;

/**
 * Server-side implementation of {@link RmiInvocationHandler}. An instance
 * of this class exists for each remote object. Automatically created
 * by {@link RmiServiceExporter} for non-RMI service implementations.
 *
 * <p>This is an SPI class, not to be used directly by applications.
 *
 * @author Juergen Hoeller
 * @since 14.05.2003
 * @see RmiServiceExporter
 */
class RmiInvocationWrapper implements RmiInvocationHandler {

	private final Object wrappedObject;

	private final RmiBasedExporter rmiExporter;


	/**
	 * Create a new RmiInvocationWrapper for the given object
	 * @param wrappedObject the object to wrap with an RmiInvocationHandler
	 * @param rmiExporter the RMI exporter to handle the actual invocation
	 */
	public RmiInvocationWrapper(Object wrappedObject, RmiBasedExporter rmiExporter) {
		Assert.notNull(wrappedObject, "Object to wrap is required");
		Assert.notNull(rmiExporter, "RMI exporter is required");
		this.wrappedObject = wrappedObject;
		this.rmiExporter = rmiExporter;
	}


	/**
	 * Exposes the exporter's service interface, if any, as target interface.
	 * @see RmiBasedExporter#getServiceInterface()
	 */
	public String getTargetInterfaceName() {
		Class ifc = this.rmiExporter.getServiceInterface();
		return (ifc != null ? ifc.getName() : null);
	}

	/**
	 * Delegates the actual invocation handling to the RMI exporter.
	 * @see RmiBasedExporter#invoke(org.springframework.remoting.support.RemoteInvocation, Object)
	 */
	public Object invoke(RemoteInvocation invocation)
	    throws RemoteException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

		return this.rmiExporter.invoke(invocation, this.wrappedObject);
	}

}
