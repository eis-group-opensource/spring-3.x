/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation.configuration.spr9031;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.configuration.spr9031.scanpackage.Spr9031Component;

/**
 * Unit tests cornering bug SPR-9031.
 *
 * @author Chris Beams
 * @since 3.1.1
 */
public class Spr9031Tests {

	/**
	 * Use of @Import to register LowLevelConfig results in ASM-based annotation
	 * processing.
	 */
	@Test
	public void withAsmAnnotationProcessing() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(HighLevelConfig.class);
		ctx.refresh();
		assertThat(ctx.getBean(LowLevelConfig.class).scanned, not(nullValue()));
	}

	/**
	 * Direct registration of LowLevelConfig results in reflection-based annotation
	 * processing.
	 */
	@Test
	public void withoutAsmAnnotationProcessing() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(LowLevelConfig.class);
		ctx.refresh();
		assertThat(ctx.getBean(LowLevelConfig.class).scanned, not(nullValue()));
	}

	@Configuration
	@Import(LowLevelConfig.class)
	static class HighLevelConfig {}

	@Configuration
	@ComponentScan(
			basePackages = "org.springframework.context.annotation.configuration.spr9031.scanpackage",
			includeFilters = { @Filter(MarkerAnnotation.class) })
	static class LowLevelConfig {
		// fails to wire when LowLevelConfig is processed with ASM because nested @Filter
		// annotation is not parsed
		@Autowired Spr9031Component scanned;
	}

	public @interface MarkerAnnotation {}
}
