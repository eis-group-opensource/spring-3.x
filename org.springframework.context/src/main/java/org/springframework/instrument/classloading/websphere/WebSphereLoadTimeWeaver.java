/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.instrument.classloading.websphere;

import java.lang.instrument.ClassFileTransformer;

import org.springframework.instrument.classloading.LoadTimeWeaver;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * {@link LoadTimeWeaver} implementation for WebSphere's instrumentable ClassLoader.
 * Compatible with WebSphere 7 as well as 8.
 *
 * @author Costin Leau
 * @since 3.1
 */
public class WebSphereLoadTimeWeaver implements LoadTimeWeaver {

	private final WebSphereClassLoaderAdapter classLoader;


	/**
	 * Create a new instance of the {@link WebSphereLoadTimeWeaver} class using
	 * the default {@link ClassLoader class loader}.
	 * @see org.springframework.util.ClassUtils#getDefaultClassLoader()
	 */
	public WebSphereLoadTimeWeaver() {
		this(ClassUtils.getDefaultClassLoader());
	}

	/**
	 * Create a new instance of the {@link WebSphereLoadTimeWeaver} class using
	 * the supplied {@link ClassLoader}.
	 * @param classLoader the <code>ClassLoader</code> to delegate to for weaving
	 * (must not be <code>null</code>)
	 */
	public WebSphereLoadTimeWeaver(ClassLoader classLoader) {
		Assert.notNull(classLoader, "ClassLoader must not be null");
		this.classLoader = new WebSphereClassLoaderAdapter(classLoader);
	}


	public void addTransformer(ClassFileTransformer transformer) {
		this.classLoader.addTransformer(transformer);
	}

	public ClassLoader getInstrumentableClassLoader() {
		return this.classLoader.getClassLoader();
	}

	public ClassLoader getThrowawayClassLoader() {
		return this.classLoader.getThrowawayClassLoader();
	}

}
