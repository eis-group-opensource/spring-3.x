/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation;

import org.junit.Test;
import org.springframework.context.annotation.componentscan.simple.SimpleComponent;

/**
 * Tests covering overlapping use of @ComponentScan and @Import annotations.
 *
 * @author Chris Beams
 * @since 3.1
 */
public class ComponentScanAndImportAnnotationInteractionTests {

	@Test
	public void componentScanOverlapsWithImport() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(Config1.class);
		ctx.register(Config2.class);
		ctx.refresh(); // no conflicts found trying to register SimpleComponent
		ctx.getBean(SimpleComponent.class); // succeeds -> there is only one bean of type SimpleComponent
	}


	@Configuration
	@ComponentScan("org.springframework.context.annotation.componentscan.simple")
	static class Config1 {

	}


	@Configuration
	@Import(org.springframework.context.annotation.componentscan.simple.SimpleComponent.class)
	static class Config2 {

	}
}
