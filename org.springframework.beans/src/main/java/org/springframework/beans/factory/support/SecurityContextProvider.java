/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.support;

import java.security.AccessControlContext;

/**
 * Provider of the security context of the code running inside the bean factory.
 *
 * @author Costin Leau
 * @since 3.0
 */
public interface SecurityContextProvider {

	/**
	 * Provides a security access control context relevant to a bean factory.
	 * @return bean factory security control context
	 */
	AccessControlContext getAccessControlContext();

}
