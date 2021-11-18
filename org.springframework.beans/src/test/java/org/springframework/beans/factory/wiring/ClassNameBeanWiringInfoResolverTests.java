/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.wiring;

import junit.framework.TestCase;

/**
 * Unit tests for the ClassNameBeanWiringInfoResolver class.
 *
 * @author Rick Evans
 */
public final class ClassNameBeanWiringInfoResolverTests extends TestCase {

	public void testResolveWiringInfoWithNullBeanInstance() throws Exception {
		try {
			new ClassNameBeanWiringInfoResolver().resolveWiringInfo(null);
			fail("Must have thrown an IllegalArgumentException by this point (null argument).");
		}
		catch (IllegalArgumentException expected) {
		}
	}

	public void testResolveWiringInfo() {
		ClassNameBeanWiringInfoResolver resolver = new ClassNameBeanWiringInfoResolver();
		Long beanInstance = new Long(1);
		BeanWiringInfo info = resolver.resolveWiringInfo(beanInstance);
		assertNotNull(info);
		assertEquals("Not resolving bean name to the class name of the supplied bean instance as per class contract.",
				beanInstance.getClass().getName(), info.getBeanName());
	}

}
