/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.context.annotation.componentscan.cycle.left.LeftConfig;
import org.springframework.context.annotation.componentscan.level1.Level1Config;
import org.springframework.context.annotation.componentscan.level2.Level2Config;
import org.springframework.context.annotation.componentscan.level3.Level3Component;

/**
 * Tests ensuring that configuration classes marked with @ComponentScan
 * may be processed recursively
 *
 * @author Chris Beams
 * @since 3.1
 */
public class ComponentScanAnnotationRecursionTests {

	@Test
	public void recursion() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(Level1Config.class);
		ctx.refresh();

		// assert that all levels have been detected
		ctx.getBean(Level1Config.class);
		ctx.getBean(Level2Config.class);
		ctx.getBean(Level3Component.class);

		// assert that enhancement is working
		assertThat(ctx.getBean("level1Bean"), sameInstance(ctx.getBean("level1Bean")));
		assertThat(ctx.getBean("level2Bean"), sameInstance(ctx.getBean("level2Bean")));
	}

	public void evenCircularScansAreSupported() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(LeftConfig.class); // left scans right, and right scans left
		ctx.refresh();
		ctx.getBean("leftConfig");      // but this is handled gracefully
		ctx.getBean("rightConfig");     // and beans from both packages are available
	}

}
