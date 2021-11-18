/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.context.annotation;

import org.springframework.beans.factory.parsing.FailFastProblemReporter;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;

/**
 * Unit test proving that ASM-based {@link ConfigurationClassParser} correctly detects circular use of
 * the {@link Import @Import} annotation.
 * 
 * <p>While this test is the only subclass of {@link AbstractCircularImportDetectionTests}, the
 * hierarchy remains in place in case a JDT-based ConfigurationParser implementation needs to be
 * developed.
 * 
 * @author Chris Beams
 */
public class AsmCircularImportDetectionTests extends AbstractCircularImportDetectionTests {

	@Override
	protected ConfigurationClassParser newParser() {
		return new ConfigurationClassParser(
				new CachingMetadataReaderFactory(),
				new FailFastProblemReporter(),
				new StandardEnvironment(),
				new DefaultResourceLoader(),
				new AnnotationBeanNameGenerator(),
				new DefaultListableBeanFactory());
	}

	@Override
	protected String loadAsConfigurationSource(Class<?> clazz) throws Exception {
		return clazz.getName();
	}

}
