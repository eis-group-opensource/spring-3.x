/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.support;

import java.security.AccessControlContext;
import java.security.AccessController;

/**
 * Simple {@link SecurityContextProvider} implementation.
 * 
 * @author Costin Leau
 * @since 3.0
 */
public class SimpleSecurityContextProvider implements SecurityContextProvider {

	private final AccessControlContext acc;


	/**
	 * Construct a new <code>SimpleSecurityContextProvider</code> instance.
	 * <p>The security context will be retrieved on each call from the current
	 * thread.
	 */
	public SimpleSecurityContextProvider() {
		this(null);
	}

	/**
	 * Construct a new <code>SimpleSecurityContextProvider</code> instance.
	 * <p>If the given control context is null, the security context will be
	 * retrieved on each call from the current thread.
	 * @param acc access control context (can be <code>null</code>)
	 * @see AccessController#getContext()
	 */
	public SimpleSecurityContextProvider(AccessControlContext acc) {
		this.acc = acc;
	}


	public AccessControlContext getAccessControlContext() {
		return (this.acc != null ? acc : AccessController.getContext());
	}

}
