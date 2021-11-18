/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.instrument.classloading.glassfish;

import java.lang.instrument.ClassFileTransformer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Reflective wrapper around the GlassFish class loader. Used to
 * encapsulate the classloader-specific methods (discovered and
 * called through reflection) from the load-time weaver.
 * 
 * <p>Supports GlassFish V1, V2 and V3 (currently in beta).
 * 
 * @author Costin Leau
 * @since 3.0
 */
class GlassFishClassLoaderAdapter {

	static final String INSTRUMENTABLE_CLASSLOADER_GLASSFISH_V2 = "com.sun.enterprise.loader.InstrumentableClassLoader";

	static final String INSTRUMENTABLE_CLASSLOADER_GLASSFISH_V3 = "org.glassfish.api.deployment.InstrumentableClassLoader";

	private static final String CLASS_TRANSFORMER = "javax.persistence.spi.ClassTransformer";


	private final ClassLoader classLoader;

	private final Method addTransformer;

	private final Method copy;

	private final boolean glassFishV3;


	public GlassFishClassLoaderAdapter(ClassLoader classLoader) {
		Class<?> instrumentableLoaderClass;
		boolean glassV3 = false;
		try {
			// try the V1/V2 API first
			instrumentableLoaderClass = classLoader.loadClass(INSTRUMENTABLE_CLASSLOADER_GLASSFISH_V2);
		}
		catch (ClassNotFoundException ex) {
			// fall back to V3
			try {
				instrumentableLoaderClass = classLoader.loadClass(INSTRUMENTABLE_CLASSLOADER_GLASSFISH_V3);
				glassV3 = true;
			}
			catch (ClassNotFoundException cnfe) {
				throw new IllegalStateException("Could not initialize GlassFish LoadTimeWeaver because " +
						"GlassFish (V1, V2 or V3) API classes are not available", ex);
			}
		}
		try {
			Class<?> classTransformerClass =
					(glassV3 ? ClassFileTransformer.class : classLoader.loadClass(CLASS_TRANSFORMER));

			this.addTransformer = instrumentableLoaderClass.getMethod("addTransformer", classTransformerClass);
			this.copy = instrumentableLoaderClass.getMethod("copy");
		}
		catch (Exception ex) {
			throw new IllegalStateException(
					"Could not initialize GlassFish LoadTimeWeaver because GlassFish API classes are not available", ex);
		}

		ClassLoader clazzLoader = null;
		// Detect transformation-aware ClassLoader by traversing the hierarchy
		// (as in GlassFish, Spring can be loaded by the WebappClassLoader).
		for (ClassLoader cl = classLoader; cl != null && clazzLoader == null; cl = cl.getParent()) {
			if (instrumentableLoaderClass.isInstance(cl)) {
				clazzLoader = cl;
			}
		}

		if (clazzLoader == null) {
			throw new IllegalArgumentException(classLoader + " and its parents are not suitable ClassLoaders: A [" +
					instrumentableLoaderClass.getName() + "] implementation is required.");
		}

		this.classLoader = clazzLoader;
		this.glassFishV3 = glassV3;
	}

	public void addTransformer(ClassFileTransformer transformer) {
		try {
			this.addTransformer.invoke(this.classLoader,
					(this.glassFishV3 ? transformer : new ClassTransformerAdapter(transformer)));
		}
		catch (InvocationTargetException ex) {
			throw new IllegalStateException("GlassFish addTransformer method threw exception ", ex.getCause());
		}
		catch (Exception ex) {
			throw new IllegalStateException("Could not invoke GlassFish addTransformer method", ex);
		}
	}

	public ClassLoader getClassLoader() {
		return this.classLoader;
	}

	public ClassLoader getThrowawayClassLoader() {
		try {
			return (ClassLoader) this.copy.invoke(this.classLoader);
		}
		catch (InvocationTargetException ex) {
			throw new IllegalStateException("GlassFish copy method threw exception ", ex.getCause());
		}
		catch (Exception ex) {
			throw new IllegalStateException("Could not invoke GlassFish copy method", ex);
		}
	}

}
