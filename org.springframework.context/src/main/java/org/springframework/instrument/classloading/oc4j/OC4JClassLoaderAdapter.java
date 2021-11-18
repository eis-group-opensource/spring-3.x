/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.instrument.classloading.oc4j;

import java.lang.instrument.ClassFileTransformer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.util.Assert;

/**
 * Reflective wrapper around a OC4J class loader. Used to
 * encapsulate the classloader-specific methods (discovered and
 * called through reflection) from the load-time weaver.
 * 
 * @author Costin Leau
 */
class OC4JClassLoaderAdapter {

	private static final String CL_UTILS = "oracle.classloader.util.ClassLoaderUtilities";
	private static final String PREPROCESS_UTILS = "oracle.classloader.util.ClassPreprocessor";

	private final ClassLoader classLoader;
	private final Class<?> processorClass;
	private final Method addTransformer;
	private final Method copy;

	public OC4JClassLoaderAdapter(ClassLoader classLoader) {
		try {
			// Since OC4J 10.1.3's PolicyClassLoader is going to be removed,
			// we rely on the ClassLoaderUtilities API instead.
			Class<?> utilClass = classLoader.loadClass(CL_UTILS);
			this.processorClass = classLoader.loadClass(PREPROCESS_UTILS);

			this.addTransformer = utilClass.getMethod("addPreprocessor", new Class[] { ClassLoader.class,
					this.processorClass });
			this.copy = utilClass.getMethod("copy", new Class[] { ClassLoader.class });

		} catch (Exception ex) {
			throw new IllegalStateException(
					"Could not initialize OC4J LoadTimeWeaver because OC4J API classes are not available", ex);
		}

		this.classLoader = classLoader;
	}

	public void addTransformer(ClassFileTransformer transformer) {
		Assert.notNull(transformer, "ClassFileTransformer must not be null");
		try {
			OC4JClassPreprocessorAdapter adapter = new OC4JClassPreprocessorAdapter(transformer);
			Object adapterInstance = Proxy.newProxyInstance(this.processorClass.getClassLoader(),
					new Class[] { this.processorClass }, adapter);
			this.addTransformer.invoke(null, new Object[] { this.classLoader, adapterInstance });
		} catch (InvocationTargetException ex) {
			throw new IllegalStateException("OC4J addPreprocessor method threw exception", ex.getCause());
		} catch (Exception ex) {
			throw new IllegalStateException("Could not invoke OC4J addPreprocessor method", ex);
		}
	}

	public ClassLoader getClassLoader() {
		return this.classLoader;
	}

	public ClassLoader getThrowawayClassLoader() {
		try {
			return (ClassLoader) this.copy.invoke(null, new Object[] { this.classLoader });
		} catch (InvocationTargetException ex) {
			throw new IllegalStateException("OC4J copy method failed", ex.getCause());
		} catch (Exception ex) {
			throw new IllegalStateException("Could not copy OC4J classloader", ex);
		}
	}
}