/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core;

/**
 * Interface to be implemented by objects that can return information about
 * the current call stack. Useful in AOP (as in AspectJ cflow concept)
 * but not AOP-specific.
 *
 * @author Rod Johnson
 * @since 02.02.2004
 */
public interface ControlFlow {

	/**
	 * Detect whether we're under the given class,
	 * according to the current stack trace.
	 * @param clazz the clazz to look for
	 */
	boolean under(Class clazz);

	/**
	 * Detect whether we're under the given class and method,
	 * according to the current stack trace.
	 * @param clazz the clazz to look for
	 * @param methodName the name of the method to look for
	 */
	boolean under(Class clazz, String methodName);

	/**
	 * Detect whether the current stack trace contains the given token.
	 * @param token the token to look for
	 */
	boolean underToken(String token);

}
