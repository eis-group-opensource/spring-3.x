/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.instrument.classloading;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassFileTransformer-based weaver, allowing for a list of transformers to be
 * applied on a class byte array. Normally used inside class loaders.
 *
 * <p>Note: This class is deliberately implemented for minimal external dependencies,
 * since it is included in weaver jars (to be deployed into application servers).
 *
 * @author Rod Johnson
 * @author Costin Leau
 * @author Juergen Hoeller
 * @since 2.0
 */
public class WeavingTransformer {

	private final ClassLoader classLoader;

	private final List<ClassFileTransformer> transformers = new ArrayList<ClassFileTransformer>();


	/**
	 * Create a new WeavingTransformer for the given class loader.
	 * @param classLoader the ClassLoader to build a transformer for
	 */
	public WeavingTransformer(ClassLoader classLoader) {
		if (classLoader == null) {
			throw new IllegalArgumentException("ClassLoader must not be null");
		}
		this.classLoader = classLoader;
	}


	/**
	 * Add a class file transformer to be applied by this weaver.
	 * @param transformer the class file transformer to register
	 */
	public void addTransformer(ClassFileTransformer transformer) {
		if (transformer == null) {
			throw new IllegalArgumentException("Transformer must not be null");
		}
		this.transformers.add(transformer);
	}


	/**
	 * Apply transformation on a given class byte definition.
	 * The method will always return a non-null byte array (if no transformation has taken place
	 * the array content will be identical to the original one).
	 * @param className the full qualified name of the class in dot format (i.e. some.package.SomeClass)
	 * @param bytes class byte definition
	 * @return (possibly transformed) class byte definition
	 */
	public byte[] transformIfNecessary(String className, byte[] bytes) {
		String internalName = className.replace(".", "/");
		return transformIfNecessary(className, internalName, bytes, null);
	}

	/**
	 * Apply transformation on a given class byte definition.
	 * The method will always return a non-null byte array (if no transformation has taken place
	 * the array content will be identical to the original one).
	 * @param className the full qualified name of the class in dot format (i.e. some.package.SomeClass)
	 * @param internalName class name internal name in / format (i.e. some/package/SomeClass)
	 * @param bytes class byte definition
	 * @param pd protection domain to be used (can be null)
	 * @return (possibly transformed) class byte definition
	 */
	public byte[] transformIfNecessary(String className, String internalName, byte[] bytes, ProtectionDomain pd) {
		byte[] result = bytes;
		for (ClassFileTransformer cft : this.transformers) {
			try {
				byte[] transformed = cft.transform(this.classLoader, internalName, null, pd, result);
				if (transformed != null) {
					result = transformed;
				}
			}
			catch (IllegalClassFormatException ex) {
				throw new IllegalStateException("Class file transformation failed", ex);
			}
		}
		return result;
	}

}
