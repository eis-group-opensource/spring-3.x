/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.support;

import java.util.Hashtable;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 * Helper class for the creation of {@link javax.management.ObjectName} instances.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 1.2
 * @see javax.management.ObjectName#getInstance(String)
 */
public class ObjectNameManager {

	/**
	 * Retrieve the <code>ObjectName</code> instance corresponding to the supplied name.
	 * @param objectName the <code>ObjectName</code> in <code>ObjectName</code> or
	 * <code>String</code> format
	 * @return the <code>ObjectName</code> instance
	 * @throws MalformedObjectNameException in case of an invalid object name specification
	 * @see ObjectName#ObjectName(String)
	 * @see ObjectName#getInstance(String)
	 */
	public static ObjectName getInstance(Object objectName) throws MalformedObjectNameException {
		if (objectName instanceof ObjectName) {
			return (ObjectName) objectName;
		}
		if (!(objectName instanceof String)) {
			throw new MalformedObjectNameException("Invalid ObjectName value type [" +
					objectName.getClass().getName() + "]: only ObjectName and String supported.");
		}
		return getInstance((String) objectName);
	}

	/**
	 * Retrieve the <code>ObjectName</code> instance corresponding to the supplied name.
	 * @param objectName the <code>ObjectName</code> in <code>String</code> format
	 * @return the <code>ObjectName</code> instance
	 * @throws MalformedObjectNameException in case of an invalid object name specification
	 * @see ObjectName#ObjectName(String)
	 * @see ObjectName#getInstance(String)
	 */
	public static ObjectName getInstance(String objectName) throws MalformedObjectNameException {
		return ObjectName.getInstance(objectName);
	}

	/**
	 * Retrieve an <code>ObjectName</code> instance for the specified domain and a
	 * single property with the supplied key and value.
	 * @param domainName the domain name for the <code>ObjectName</code>
	 * @param key the key for the single property in the <code>ObjectName</code>
	 * @param value the value for the single property in the <code>ObjectName</code>
	 * @return the <code>ObjectName</code> instance
	 * @throws MalformedObjectNameException in case of an invalid object name specification
	 * @see ObjectName#ObjectName(String, String, String)
	 * @see ObjectName#getInstance(String, String, String)
	 */
	public static ObjectName getInstance(String domainName, String key, String value)
			throws MalformedObjectNameException {

		return ObjectName.getInstance(domainName, key, value);
	}

	/**
	 * Retrieve an <code>ObjectName</code> instance with the specified domain name
	 * and the supplied key/name properties.
	 * @param domainName the domain name for the <code>ObjectName</code>
	 * @param properties the properties for the <code>ObjectName</code>
	 * @return the <code>ObjectName</code> instance
	 * @throws MalformedObjectNameException in case of an invalid object name specification
	 * @see ObjectName#ObjectName(String, java.util.Hashtable)
	 * @see ObjectName#getInstance(String, java.util.Hashtable)
	 */
	public static ObjectName getInstance(String domainName, Hashtable<String, String> properties)
			throws MalformedObjectNameException {

		return ObjectName.getInstance(domainName, properties);
	}

}
