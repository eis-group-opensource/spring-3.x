/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.context.annotation;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.parsing.BeanDefinitionParsingException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConfigurationClassParser;
import org.springframework.context.annotation.Import;

import test.beans.TestBean;


/**
 * TCK-style unit tests for handling circular use of the {@link Import} annotation. Explore
 * subclass hierarchy for specific concrete implementations.
 * 
 * @author Chris Beams
 */
public abstract class AbstractCircularImportDetectionTests {

	protected abstract ConfigurationClassParser newParser();

	protected abstract String loadAsConfigurationSource(Class<?> clazz) throws Exception;
	
	@Test
	public void simpleCircularImportIsDetected() throws Exception {
		boolean threw = false;
		try {
			newParser().parse(loadAsConfigurationSource(A.class), "A");
		} catch (BeanDefinitionParsingException ex) {
			assertTrue("Wrong message. Got: " + ex.getMessage(),
					ex.getMessage().contains(
						"Illegal attempt by @Configuration class 'AbstractCircularImportDetectionTests.B' " +
						"to import class 'AbstractCircularImportDetectionTests.A'"));
			threw = true;
		}

		assertTrue(threw);
	}


	@Test
	public void complexCircularImportIsDetected() throws Exception {
		boolean threw = false;
		try {
			newParser().parse(loadAsConfigurationSource(X.class), "X");
		}
		catch (BeanDefinitionParsingException ex) {
			assertTrue("Wrong message. Got: " + ex.getMessage(),
					ex.getMessage().contains(
						"Illegal attempt by @Configuration class 'AbstractCircularImportDetectionTests.Z2' " +
						"to import class 'AbstractCircularImportDetectionTests.Z'"));
			threw = true;
		}

		assertTrue(threw);
	}

	@Configuration
	@Import(B.class)
	static class A {
		@Bean
		TestBean b1() {
			return new TestBean();
		}
	}

	@Configuration
	@Import(A.class)
	static class B {
		@Bean
		TestBean b2() {
			return new TestBean();
		}
	}

	@Configuration
	@Import( { Y.class, Z.class })
	class X {
		@Bean
		TestBean x() {
			return new TestBean();
		}
	}

	@Configuration
	class Y {
		@Bean
		TestBean y() {
			return new TestBean();
		}
	}

	@Configuration
	@Import( { Z1.class, Z2.class })
	class Z {
		@Bean
		TestBean z() {
			return new TestBean();
		}
	}

	@Configuration
	class Z1 {
		@Bean
		TestBean z1() {
			return new TestBean();
		}
	}

	@Configuration
	@Import(Z.class)
	class Z2 {
		@Bean
		TestBean z2() {
			return new TestBean();
		}
	}

}
