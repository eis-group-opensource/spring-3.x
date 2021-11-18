/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.serializer.support;

import org.springframework.core.NestedRuntimeException;

/**
 * Wrapper for the native IOException (or similar) when a
 * {@link org.springframework.core.serializer.Serializer} or
 * {@link org.springframework.core.serializer.Deserializer} failed.
 * Thrown by {@link SerializingConverter} and {@link DeserializingConverter}.
 *
 * @author Gary Russell
 * @author Juergen Hoeller
 * @since 3.0.5
 */
public class SerializationFailedException extends NestedRuntimeException {

	/**
	 * Construct a <code>SerializationException</code> with the specified detail message.
	 * @param message the detail message
	 */
	public SerializationFailedException(String message) {
		super(message);
	}

	/**
	 * Construct a <code>SerializationException</code> with the specified detail message
	 * and nested exception.
	 * @param message the detail message
	 * @param cause the nested exception
	 */
	public SerializationFailedException(String message, Throwable cause) {
		super(message, cause);
	}

}
