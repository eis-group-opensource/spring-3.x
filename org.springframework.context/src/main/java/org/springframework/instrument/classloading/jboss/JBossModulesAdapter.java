/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.instrument.classloading.jboss;

import java.lang.instrument.ClassFileTransformer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * JBoss 7 adapter.
 *
 * @author Costin Leau
 * @since 3.1
 */
class JBossModulesAdapter implements JBossClassLoaderAdapter {

	private static final String TRANSFORMER_FIELD_NAME = "transformer";
	private static final String TRANSFORMER_ADD_METHOD_NAME = "addTransformer";
	private static final String DELEGATING_TRANSFORMER_CLASS_NAME = "org.jboss.as.server.deployment.module.DelegatingClassFileTransformer";
	private final ClassLoader classLoader;
	private final Method addTransformer;
	private final Object delegatingTransformer;

	public JBossModulesAdapter(ClassLoader loader) {
		this.classLoader = loader;

		try {
			Field transformers = ReflectionUtils.findField(classLoader.getClass(), TRANSFORMER_FIELD_NAME);
			transformers.setAccessible(true);

			delegatingTransformer = transformers.get(classLoader);

			Assert.state(delegatingTransformer.getClass().getName().equals(DELEGATING_TRANSFORMER_CLASS_NAME),
					"Transformer not of the expected type: " + delegatingTransformer.getClass().getName());
			addTransformer = ReflectionUtils.findMethod(delegatingTransformer.getClass(), TRANSFORMER_ADD_METHOD_NAME,
					ClassFileTransformer.class);
			addTransformer.setAccessible(true);
		} catch (Exception ex) {
			throw new IllegalStateException("Could not initialize JBoss 7 LoadTimeWeaver", ex);
		}
	}

	public void addTransformer(ClassFileTransformer transformer) {
		try {
			addTransformer.invoke(delegatingTransformer, transformer);
		} catch (Exception ex) {
			throw new IllegalStateException("Could not add transformer on JBoss 7 classloader " + classLoader, ex);
		}
	}

	public ClassLoader getInstrumentableClassLoader() {
		return classLoader;
	}
}