/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.access;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Unit test for {@link ContextBeanFactoryReference}
 * 
 * @author Colin Sampaleanu
 * @author Chris Beams
 */
public class ContextBeanFactoryReferenceTests {
	
	@Test
	public void testAllOperations() {
		ConfigurableApplicationContext ctx = createMock(ConfigurableApplicationContext.class);

		ctx.close();
		replay(ctx);

		ContextBeanFactoryReference bfr = new ContextBeanFactoryReference(ctx);

		assertNotNull(bfr.getFactory());
		bfr.release();

		try {
			bfr.getFactory();
		}
		catch (IllegalStateException e) {
			// expected
		}

		verify(ctx);
	}
}
