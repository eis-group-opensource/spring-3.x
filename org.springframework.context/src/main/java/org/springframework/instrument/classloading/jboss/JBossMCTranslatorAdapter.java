/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.instrument.classloading.jboss;

import java.lang.instrument.ClassFileTransformer;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;

/**
 * Adapter that implements JBoss Translator interface, delegating to a
 * standard JDK {@link ClassFileTransformer} underneath.
 * 
 * <p>To avoid compile time checks again the vendor API, a dynamic proxy is
 * being used.
 * 
 * @author Costin Leau
 * @since 3.1
 */
class JBossMCTranslatorAdapter implements InvocationHandler {

	private final ClassFileTransformer transformer;

	/**
	 * Creates a new {@link JBossMCTranslatorAdapter}.
	 * @param transformer the {@link ClassFileTransformer} to be adapted (must
	 * not be <code>null</code>)
	 */
	public JBossMCTranslatorAdapter(ClassFileTransformer transformer) {
		this.transformer = transformer;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String name = method.getName();

		if ("equals".equals(name)) {
			return (Boolean.valueOf(proxy == args[0]));
		} else if ("hashCode".equals(name)) {
			return hashCode();
		} else if ("toString".equals(name)) {
			return toString();
		} else if ("transform".equals(name)) {
			return transform((ClassLoader) args[0], (String) args[1], (Class<?>) args[2], (ProtectionDomain) args[3],
					(byte[]) args[4]);
		} else if ("unregisterClassLoader".equals(name)) {
			unregisterClassLoader((ClassLoader) args[0]);
			return null;

		} else {
			throw new IllegalArgumentException("Unknown method: " + method);
		}
	}

	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws Exception {
		return transformer.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
	}

	public void unregisterClassLoader(ClassLoader loader) {
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(getClass().getName());
		builder.append(" for transformer: ");
		builder.append(this.transformer);
		return builder.toString();
	}
}