/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.instrument.classloading.glassfish;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javax.persistence.spi.ClassTransformer;

/**
 * Adapter that implements the JPA ClassTransformer interface (as required by GlassFish V1 and V2)
 * based on a given JDK 1.5 ClassFileTransformer.
 *
 * @author Costin Leau
 * @author Juergen Hoeller
 * @since 2.0.1
 */
class ClassTransformerAdapter implements ClassTransformer {

	private final ClassFileTransformer classFileTransformer;

	/**
	 * Build a new ClassTransformerAdapter for the given ClassFileTransformer.
	 * @param classFileTransformer the JDK 1.5 ClassFileTransformer to wrap
	 */
	public ClassTransformerAdapter(ClassFileTransformer classFileTransformer) {
		this.classFileTransformer = classFileTransformer;
	}

	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

		byte[] result = this.classFileTransformer.transform(loader, className, classBeingRedefined, protectionDomain,
				classfileBuffer);

		// If no transformation was done, return null.
		return (result == classfileBuffer ? null : result);
	}
}
