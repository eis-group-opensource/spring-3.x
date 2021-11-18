/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.remoting;

/**
 * RemoteAccessException subclass to be thrown in case of a lookup failure,
 * typically if the lookup happens on demand for each method invocation.
 *
 * @author Juergen Hoeller
 * @since 1.1
 */
public class RemoteLookupFailureException extends RemoteAccessException {

	/**
	 * Constructor for RemoteLookupFailureException.
	 * @param msg the detail message
	 */
	public RemoteLookupFailureException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for RemoteLookupFailureException.
	 * @param msg message
	 * @param cause the root cause from the remoting API in use
	 */
	public RemoteLookupFailureException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
