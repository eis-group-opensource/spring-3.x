/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.instrument.classloading.oc4j;

import java.lang.instrument.ClassFileTransformer;

import org.springframework.instrument.classloading.LoadTimeWeaver;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * {@link LoadTimeWeaver} implementation for OC4J's instrumentable ClassLoader.
 *
 * <p><b>NOTE:</b> Requires Oracle OC4J version 10.1.3.1 or higher.
 *
 * <p>Many thanks to <a href="mailto:mike.keith@oracle.com">Mike Keith</a>
 * for his assistance.
 *
 * @author Costin Leau
 * @author Juergen Hoeller
 * @since 2.0
 */
public class OC4JLoadTimeWeaver implements LoadTimeWeaver {

	private final OC4JClassLoaderAdapter classLoader;

	/**
	 * Creates a new instance of thie {@link OC4JLoadTimeWeaver} class
	 * using the default {@link ClassLoader class loader}.
	 * @see org.springframework.util.ClassUtils#getDefaultClassLoader() 
	 */
	public OC4JLoadTimeWeaver() {
		this(ClassUtils.getDefaultClassLoader());
	}

	/**
	 * Creates a new instance of the {@link OC4JLoadTimeWeaver} class
	 * using the supplied {@link ClassLoader}.
	 * @param classLoader the <code>ClassLoader</code> to delegate to for weaving
	 */
	public OC4JLoadTimeWeaver(ClassLoader classLoader) {
		Assert.notNull(classLoader, "ClassLoader must not be null");
		this.classLoader = new OC4JClassLoaderAdapter(classLoader);
	}

	public void addTransformer(ClassFileTransformer transformer) {
		Assert.notNull(transformer, "Transformer must not be null");
		// Since OC4J 10.1.3's PolicyClassLoader is going to be removed,
		// we rely on the ClassLoaderUtilities API instead.
		classLoader.addTransformer(transformer);
	}

	public ClassLoader getInstrumentableClassLoader() {
		return classLoader.getClassLoader();
	}

	public ClassLoader getThrowawayClassLoader() {
		return classLoader.getThrowawayClassLoader();
	}
}