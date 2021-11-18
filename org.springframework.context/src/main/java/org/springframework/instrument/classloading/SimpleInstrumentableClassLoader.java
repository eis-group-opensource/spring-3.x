/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.instrument.classloading;

import java.lang.instrument.ClassFileTransformer;

import org.springframework.core.OverridingClassLoader;

/**
 * Simplistic implementation of an instrumentable <code>ClassLoader</code>.
 *
 * <p>Usable in tests and standalone environments.
 *
 * @author Rod Johnson
 * @author Costin Leau
 * @since 2.0
 */
public class SimpleInstrumentableClassLoader extends OverridingClassLoader  {

	private final WeavingTransformer weavingTransformer;


	/**
	 * Create a new <code>SimpleLoadTimeWeaver</code> for the given
	 * <code>ClassLoader</code>.
	 * @param parent the <code>ClassLoader</code> to build a simple
	 * instrumentable <code>ClassLoader</code> for
	 */
	public SimpleInstrumentableClassLoader(ClassLoader parent) {
		super(parent);
		this.weavingTransformer = new WeavingTransformer(parent);
	}


	/**
	 * Add a <code>ClassFileTransformer</code> to be applied by this
	 * <code>ClassLoader</code>.
	 * @param transformer the <code>ClassFileTransformer</code> to register
	 */
	public void addTransformer(ClassFileTransformer transformer) {
		this.weavingTransformer.addTransformer(transformer);
	}


	@Override
	protected byte[] transformIfNecessary(String name, byte[] bytes) {
		return this.weavingTransformer.transformIfNecessary(name, bytes);
	}

}
