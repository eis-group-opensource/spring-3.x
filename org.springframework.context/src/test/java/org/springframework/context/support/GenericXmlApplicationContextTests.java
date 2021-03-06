/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.context.support;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;

/**
 * Unit tests for {@link GenericXmlApplicationContext}.
 *
 * See SPR-7530.
 *
 * @author Chris Beams
 */
public class GenericXmlApplicationContextTests {

	private static final Class<?> RELATIVE_CLASS = GenericXmlApplicationContextTests.class;
	private static final String RESOURCE_BASE_PATH = ClassUtils.classPackageAsResourcePath(RELATIVE_CLASS);
	private static final String RESOURCE_NAME = GenericXmlApplicationContextTests.class.getSimpleName() + "-context.xml";
	private static final String FQ_RESOURCE_PATH = RESOURCE_BASE_PATH + '/' + RESOURCE_NAME;
	private static final String TEST_BEAN_NAME = "testBean";


	@Test
	public void classRelativeResourceLoading_ctor() {
		ApplicationContext ctx = new GenericXmlApplicationContext(RELATIVE_CLASS, RESOURCE_NAME);
		assertThat(ctx.containsBean(TEST_BEAN_NAME), is(true));
	}

	@Test
	public void classRelativeResourceLoading_load() {
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
		ctx.load(RELATIVE_CLASS, RESOURCE_NAME);
		ctx.refresh();
		assertThat(ctx.containsBean(TEST_BEAN_NAME), is(true));
	}

	@Test
	public void fullyQualifiedResourceLoading_ctor() {
		ApplicationContext ctx = new GenericXmlApplicationContext(FQ_RESOURCE_PATH);
		assertThat(ctx.containsBean(TEST_BEAN_NAME), is(true));
	}

	@Test
	public void fullyQualifiedResourceLoading_load() {
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
		ctx.load(FQ_RESOURCE_PATH);
		ctx.refresh();
		assertThat(ctx.containsBean(TEST_BEAN_NAME), is(true));
	}
}
