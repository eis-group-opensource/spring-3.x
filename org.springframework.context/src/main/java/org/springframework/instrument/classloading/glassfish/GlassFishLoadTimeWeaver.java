/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.instrument.classloading.glassfish;

import java.lang.instrument.ClassFileTransformer;

import org.springframework.instrument.classloading.LoadTimeWeaver;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * {@link LoadTimeWeaver} implementation for GlassFish's {@link InstrumentableClassLoader}.
 * 
 * <p>As of Spring 3.0, GlassFish V3 is supported as well.
 *
 * @author Costin Leau
 * @author Juergen Hoeller
 * @since 2.0.1
 */
public class GlassFishLoadTimeWeaver implements LoadTimeWeaver {

	private final GlassFishClassLoaderAdapter classLoader;


	/**
	 * Creates a new instance of the <code>GlassFishLoadTimeWeaver</code> class
	 * using the default {@link ClassLoader}.
	 * @see #GlassFishLoadTimeWeaver(ClassLoader)
	 */
	public GlassFishLoadTimeWeaver() {
		this(ClassUtils.getDefaultClassLoader());
	}

	/**
	 * Creates a new instance of the <code>GlassFishLoadTimeWeaver</code> class.
	 * @param classLoader the specific {@link ClassLoader} to use; must not be <code>null</code>
	 * @throws IllegalArgumentException if the supplied <code>classLoader</code> is <code>null</code>;
	 * or if the supplied <code>classLoader</code> is not an {@link InstrumentableClassLoader}
	 */
	public GlassFishLoadTimeWeaver(ClassLoader classLoader) {
		Assert.notNull(classLoader, "ClassLoader must not be null");
		this.classLoader = new GlassFishClassLoaderAdapter(classLoader);
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
