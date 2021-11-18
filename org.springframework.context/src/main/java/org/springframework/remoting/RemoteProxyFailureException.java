/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.remoting;

/**
 * RemoteAccessException subclass to be thrown in case of a failure
 * within the client-side proxy for a remote service, for example
 * when a method was not found on the underlying RMI stub.
 *
 * @author Juergen Hoeller
 * @since 1.2.8
 * @see RemoteInvocationFailureException
 */
public class RemoteProxyFailureException extends RemoteAccessException {

	/**
	 * Constructor for RemoteProxyFailureException.
	 * @param msg the detail message
	 * @param cause the root cause from the remoting API in use
	 */
	public RemoteProxyFailureException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
