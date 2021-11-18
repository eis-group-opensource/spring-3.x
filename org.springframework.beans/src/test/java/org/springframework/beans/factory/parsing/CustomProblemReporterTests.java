/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.parsing;

import static org.junit.Assert.*;
import static test.util.TestResourceUtils.qualifiedResource;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;

import test.beans.TestBean;

/**
 * @author Rob Harrop
 * @author Chris Beams
 * @since 2.0
 */
public final class CustomProblemReporterTests {
	
	private static final Resource CONTEXT = qualifiedResource(CustomProblemReporterTests.class, "context.xml");

	private CollatingProblemReporter problemReporter;

	private DefaultListableBeanFactory beanFactory;

	private XmlBeanDefinitionReader reader;


	@Before
	public void setUp() {
		this.problemReporter = new CollatingProblemReporter();
		this.beanFactory = new DefaultListableBeanFactory();
		this.reader = new XmlBeanDefinitionReader(this.beanFactory);
		this.reader.setProblemReporter(this.problemReporter);
	}

	@Test
	public void testErrorsAreCollated() {
		this.reader.loadBeanDefinitions(CONTEXT);
		assertEquals("Incorrect number of errors collated", 4, this.problemReporter.getErrors().length);

		TestBean bean = (TestBean) this.beanFactory.getBean("validBean");
		assertNotNull(bean);
	}


	private static class CollatingProblemReporter implements ProblemReporter {

		private List<Problem> errors = new ArrayList<Problem>();

		private List<Problem> warnings = new ArrayList<Problem>();


		public void fatal(Problem problem) {
			throw new BeanDefinitionParsingException(problem);
		}

		public void error(Problem problem) {
			System.out.println(problem);
			this.errors.add(problem);
		}

		public Problem[] getErrors() {
			return this.errors.toArray(new Problem[this.errors.size()]);
		}

		public void warning(Problem problem) {
			System.out.println(problem);
			this.warnings.add(problem);
		}

		public Problem[] getWarnings() {
			return this.warnings.toArray(new Problem[this.warnings.size()]);
		}
	}

}
