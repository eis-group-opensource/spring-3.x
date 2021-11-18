/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jndi;

import javax.naming.NamingException;

import org.springframework.core.NestedRuntimeException;

/**
 * RuntimeException to be thrown in case of JNDI lookup failures,
 * in particular from code that does not declare JNDI's checked
 * {@link javax.naming.NamingException}: for example, from Spring's
 * {@link JndiObjectTargetSource}.
 *
 * @author Juergen Hoeller
 * @since 2.0.3
 */
public class JndiLookupFailureException extends NestedRuntimeException {

	/**
	 * Construct a new JndiLookupFailureException,
	 * wrapping the given JNDI NamingException.
	 * @param msg the detail message
	 * @param cause the NamingException root cause
	 */
	public JndiLookupFailureException(String msg, NamingException cause) {
		super(msg, cause);
	}

}
