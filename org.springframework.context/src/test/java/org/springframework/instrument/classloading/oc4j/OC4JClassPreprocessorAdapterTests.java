/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.instrument.classloading.oc4j;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotNull;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.junit.Test;

/**
 * Unit tests for the {@link OC4JClassPreprocessorAdapter} class.
 *
 * @author Rick Evans
 * @author Chris Beams
 */
public final class OC4JClassPreprocessorAdapterTests {

	@Test
	public void testClassNameIsUnMangledPriorToTransformation() throws IllegalClassFormatException {
		final byte[] classBytes = "CAFEBABE".getBytes();
		final ClassLoader classLoader = getClass().getClassLoader();

		ClassFileTransformer transformer = createMock(ClassFileTransformer.class);

		expect(
				transformer.transform(eq(classLoader), eq("com/foo/Bar"), (Class<?>)isNull(), (ProtectionDomain)isNull(), isA(byte[].class))
			).andReturn(classBytes);
		replay(transformer);

		OC4JClassPreprocessorAdapter processor = new OC4JClassPreprocessorAdapter(transformer);
		byte[] bytes = processor.processClass("com.foo.Bar", classBytes, 0, 0, null, classLoader);
		assertNotNull(bytes);

		verify(transformer);
	}
}