/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.remoting.support;

import java.util.HashSet;
import java.util.Set;

import org.springframework.core.JdkVersion;

/**
 * General utilities for handling remote invocations.
 * 
 * <p>Mainly intended for use within the remoting framework.
 *
 * @author Juergen Hoeller
 * @since 2.0
 */
public abstract class RemoteInvocationUtils {

	/**
	 * Fill the current client-side stack trace into the given exception.
	 * <p>The given exception is typically thrown on the server and serialized
	 * as-is, with the client wanting it to contain the client-side portion
	 * of the stack trace as well. What we can do here is to update the
	 * <code>StackTraceElement</code> array with the current client-side stack
	 * trace, provided that we run on JDK 1.4+.
	 * @param ex the exception to update
	 * @see java.lang.Throwable#getStackTrace()
	 * @see java.lang.Throwable#setStackTrace(StackTraceElement[])
	 */
	public static void fillInClientStackTraceIfPossible(Throwable ex) {
		if (ex != null) {
			StackTraceElement[] clientStack = new Throwable().getStackTrace();
			Set<Throwable> visitedExceptions = new HashSet<Throwable>();
			Throwable exToUpdate = ex;
			while (exToUpdate != null && !visitedExceptions.contains(exToUpdate)) {
				StackTraceElement[] serverStack = exToUpdate.getStackTrace();
				StackTraceElement[] combinedStack = new StackTraceElement[serverStack.length + clientStack.length];
				System.arraycopy(serverStack, 0, combinedStack, 0, serverStack.length);
				System.arraycopy(clientStack, 0, combinedStack, serverStack.length, clientStack.length);
				exToUpdate.setStackTrace(combinedStack);
				visitedExceptions.add(exToUpdate);
				exToUpdate = exToUpdate.getCause();
			}
		}
	}

}
