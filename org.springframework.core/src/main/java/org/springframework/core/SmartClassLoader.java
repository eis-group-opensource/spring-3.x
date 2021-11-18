/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core;

/**
 * Interface to be implemented by a reloading-aware ClassLoader
 * (e.g. a Groovy-based ClassLoader). Detected for example by
 * Spring's CGLIB proxy factory for making a caching decision.
 *
 * <p>If a ClassLoader does <i>not</i> implement this interface,
 * then all of the classes obtained from it should be considered
 * as not reloadable (i.e. cacheable).
 *
 * @author Juergen Hoeller
 * @since 2.5.1
 */
public interface SmartClassLoader {

	/**
	 * Determine whether the given class is reloadable (in this ClassLoader).
	 * <p>Typically used to check whether the result may be cached (for this
	 * ClassLoader) or whether it should be reobtained every time.
	 * @param clazz the class to check (usually loaded from this ClassLoader)
	 * @return whether the class should be expected to appear in a reloaded
	 * version (with a different <code>Class</code> object) later on
	 */
	boolean isClassReloadable(Class clazz);

}
