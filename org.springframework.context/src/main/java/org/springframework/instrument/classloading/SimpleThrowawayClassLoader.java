/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.instrument.classloading;

import org.springframework.core.OverridingClassLoader;

/**
 * ClassLoader that can be used to load classes without bringing them
 * into the parent loader. Intended to support JPA "temp class loader"
 * requirement, but not JPA-specific.
 *
 * @author Rod Johnson
 * @since 2.0
 */
public class SimpleThrowawayClassLoader extends OverridingClassLoader {

	/**
	 * Create a new SimpleThrowawayClassLoader for the given class loader.
	 * @param parent the ClassLoader to build a throwaway ClassLoader for
	 */
	public SimpleThrowawayClassLoader(ClassLoader parent) {
		super(parent);
	}

}
