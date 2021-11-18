/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.instrument.classloading;

import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.springframework.util.ClassUtils;

/**
 * @author Costin Leau
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public class InstrumentableClassLoaderTests {

	@Test
	public void testDefaultLoadTimeWeaver() {
		ClassLoader loader = new SimpleInstrumentableClassLoader(ClassUtils.getDefaultClassLoader());
		ReflectiveLoadTimeWeaver handler = new ReflectiveLoadTimeWeaver(loader);
		assertSame(loader, handler.getInstrumentableClassLoader());
	}

}
