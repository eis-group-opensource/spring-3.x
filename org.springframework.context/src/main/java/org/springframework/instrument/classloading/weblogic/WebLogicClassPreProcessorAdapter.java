/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.instrument.classloading.weblogic;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Hashtable;

/**
 * Adapter that implements WebLogic ClassPreProcessor interface, delegating to a
 * standard JDK {@link ClassFileTransformer} underneath.
 *
 * <p>To avoid compile time checks again the vendor API, a dynamic proxy is
 * being used.
 *
 * @author Costin Leau
 * @author Juergen Hoeller
 * @since 2.5
 */
class WebLogicClassPreProcessorAdapter implements InvocationHandler {

	private final ClassFileTransformer transformer;

	private final ClassLoader loader;

	/**
	 * Creates a new {@link WebLogicClassPreProcessorAdapter}.
	 * @param transformer the {@link ClassFileTransformer} to be adapted (must
	 * not be <code>null</code>)
	 */
	public WebLogicClassPreProcessorAdapter(ClassFileTransformer transformer, ClassLoader loader) {
		this.transformer = transformer;
		this.loader = loader;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String name = method.getName();

		if ("equals".equals(name)) {
			return (Boolean.valueOf(proxy == args[0]));
		} else if ("hashCode".equals(name)) {
			return hashCode();
		} else if ("toString".equals(name)) {
			return toString();
		} else if ("initialize".equals(name)) {
			initialize((Hashtable) args[0]);
			return null;
		} else if ("preProcess".equals(name)) {
			return preProcess((String) args[0], (byte[]) args[1]);
		} else {
			throw new IllegalArgumentException("Unknown method: " + method);
		}
	}

	public void initialize(Hashtable params) {
	}

	public byte[] preProcess(String className, byte[] classBytes) {
		try {
			byte[] result = this.transformer.transform(this.loader, className, null, null, classBytes);
			return (result != null ? result : classBytes);
		} catch (IllegalClassFormatException ex) {
			throw new IllegalStateException("Cannot transform due to illegal class format", ex);
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(getClass().getName());
		builder.append(" for transformer: ");
		builder.append(this.transformer);
		return builder.toString();
	}
}