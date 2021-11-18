/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.remoting;

/**
 * RemoteAccessException subclass to be thrown when no connection
 * could be established with a remote service.
 *
 * @author Juergen Hoeller
 * @since 1.1
 */
public class RemoteConnectFailureException extends RemoteAccessException {

	/**
	 * Constructor for RemoteConnectFailureException.
	 * @param msg the detail message
	 * @param cause the root cause from the remoting API in use
	 */
	public RemoteConnectFailureException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
