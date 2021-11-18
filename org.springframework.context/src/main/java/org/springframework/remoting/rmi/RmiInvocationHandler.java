/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.remoting.rmi;

import java.lang.reflect.InvocationTargetException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import org.springframework.remoting.support.RemoteInvocation;

/**
 * Interface for RMI invocation handlers instances on the server,
 * wrapping exported services. A client uses a stub implementing
 * this interface to access such a service.
 *
 * <p>This is an SPI interface, not to be used directly by applications.
 *
 * @author Juergen Hoeller
 * @since 14.05.2003
 */
public interface RmiInvocationHandler extends Remote {

	/**
	 * Return the name of the target interface that this invoker operates on.
	 * @return the name of the target interface, or <code>null</code> if none
	 * @throws RemoteException in case of communication errors
	 * @see RmiServiceExporter#getServiceInterface()
	 */
	public String getTargetInterfaceName() throws RemoteException;

	/**
	 * Apply the given invocation to the target object.
	 * <p>Called by
	 * {@link RmiClientInterceptor#doInvoke(org.aopalliance.intercept.MethodInvocation, RmiInvocationHandler)}.
	 * @param invocation object that encapsulates invocation parameters
	 * @return the object returned from the invoked method, if any
	 * @throws RemoteException in case of communication errors
	 * @throws NoSuchMethodException if the method name could not be resolved
	 * @throws IllegalAccessException if the method could not be accessed
	 * @throws InvocationTargetException if the method invocation resulted in an exception
	 */
	public Object invoke(RemoteInvocation invocation)
	    throws RemoteException, NoSuchMethodException, IllegalAccessException, InvocationTargetException;

}
