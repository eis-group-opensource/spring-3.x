/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jndi;

import javax.naming.NamingException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

/**
 * Convenient superclass for JNDI-based service locators,
 * providing configurable lookup of a specific JNDI resource.
 *
 * <p>Exposes a {@link #setJndiName "jndiName"} property. This may or may not
 * include the "java:comp/env/" prefix expected by J2EE applications when
 * accessing a locally mapped (Environmental Naming Context) resource. If it
 * doesn't, the "java:comp/env/" prefix will be prepended if the "resourceRef"
 * property is true (the default is <strong>false</strong>) and no other scheme
 * (e.g. "java:") is given.
 *
 * <p>Subclasses may invoke the {@link #lookup()} method whenever it is appropriate.
 * Some classes might do this on initialization, while others might do it
 * on demand. The latter strategy is more flexible in that it allows for
 * initialization of the locator before the JNDI object is available.
 *
 * @author Juergen Hoeller
 * @since 1.1
 * @see #setJndiName
 * @see #setJndiTemplate
 * @see #setJndiEnvironment
 * @see #setResourceRef
 * @see #lookup()
 */
public abstract class JndiObjectLocator extends JndiLocatorSupport implements InitializingBean {

	private String jndiName;

	private Class<?> expectedType;


	/**
	 * Specify the JNDI name to look up. If it doesn't begin with "java:comp/env/"
	 * this prefix is added automatically if "resourceRef" is set to "true".
	 * @param jndiName the JNDI name to look up
	 * @see #setResourceRef
	 */
	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}

	/**
	 * Return the JNDI name to look up.
	 */
	public String getJndiName() {
		return this.jndiName;
	}

	/**
	 * Specify the type that the located JNDI object is supposed
	 * to be assignable to, if any.
	 */
	public void setExpectedType(Class<?> expectedType) {
		this.expectedType = expectedType;
	}

	/**
	 * Return the type that the located JNDI object is supposed
	 * to be assignable to, if any.
	 */
	public Class<?> getExpectedType() {
		return this.expectedType;
	}

	public void afterPropertiesSet() throws IllegalArgumentException, NamingException {
		if (!StringUtils.hasLength(getJndiName())) {
			throw new IllegalArgumentException("Property 'jndiName' is required");
		}
	}


	/**
	 * Perform the actual JNDI lookup for this locator's target resource.
	 * @return the located target object
	 * @throws NamingException if the JNDI lookup failed or if the
	 * located JNDI object is not assigable to the expected type
	 * @see #setJndiName
	 * @see #setExpectedType
	 * @see #lookup(String, Class)
	 */
	protected Object lookup() throws NamingException {
		return lookup(getJndiName(), getExpectedType());
	}

}
