/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.mock.jndi;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.NamingException;

import org.springframework.core.CollectionFactory;
import org.springframework.jndi.JndiTemplate;

/**
 * Simple extension of the JndiTemplate class that always returns
 * a given object. Very useful for testing. Effectively a mock object.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 */
public class ExpectedLookupTemplate extends JndiTemplate {

	private final Map<String, Object> jndiObjects = new ConcurrentHashMap<String, Object>();


	/**
	 * Construct a new JndiTemplate that will always return given objects
	 * for given names. To be populated through <code>addObject</code> calls.
	 * @see #addObject(String, Object)
	 */
	public ExpectedLookupTemplate() {
	}

	/**
	 * Construct a new JndiTemplate that will always return the
	 * given object, but honour only requests for the given name.
	 * @param name the name the client is expected to look up
	 * @param object the object that will be returned
	 */
	public ExpectedLookupTemplate(String name, Object object) {
		addObject(name, object);
	}


	/**
	 * Add the given object to the list of JNDI objects that this
	 * template will expose.
	 * @param name the name the client is expected to look up
	 * @param object the object that will be returned
	 */
	public void addObject(String name, Object object) {
		this.jndiObjects.put(name, object);
	}


	/**
	 * If the name is the expected name specified in the constructor,
	 * return the object provided in the constructor. If the name is
	 * unexpected, a respective NamingException gets thrown.
	 */
	public Object lookup(String name) throws NamingException {
		Object object = this.jndiObjects.get(name);
		if (object == null) {
			throw new NamingException("Unexpected JNDI name '" + name + "': expecting " + this.jndiObjects.keySet());
		}
		return object;
	}

}
