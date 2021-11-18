/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.config;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * Unit tests for {@link CustomScopeConfigurer}.
 *
 * @author Rick Evans
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public final class CustomScopeConfigurerTests {

	private static final String FOO_SCOPE = "fooScope";
	private ConfigurableListableBeanFactory factory;

	@Before
	public void setUp() {
		factory = new DefaultListableBeanFactory();
	}

	@Test
	public void testWithNoScopes() throws Exception {
		Scope scope = createMock(Scope.class);
		replay(scope);
		CustomScopeConfigurer figurer = new CustomScopeConfigurer();
		figurer.postProcessBeanFactory(factory);
		verify(scope);
	}

	@Test
	public void testSunnyDayWithBonaFideScopeInstance() throws Exception {
		Scope scope = createMock(Scope.class);
		replay(scope);
		factory.registerScope(FOO_SCOPE, scope);
		Map<String, Object> scopes = new HashMap<String, Object>();
		scopes.put(FOO_SCOPE, scope);
		CustomScopeConfigurer figurer = new CustomScopeConfigurer();
		figurer.setScopes(scopes);
		figurer.postProcessBeanFactory(factory);
		verify(scope);
	}

	@Test
	public void testSunnyDayWithBonaFideScopeClass() throws Exception {
		Map<String, Object> scopes = new HashMap<String, Object>();
		scopes.put(FOO_SCOPE, NoOpScope.class);
		CustomScopeConfigurer figurer = new CustomScopeConfigurer();
		figurer.setScopes(scopes);
		figurer.postProcessBeanFactory(factory);
		assertTrue(factory.getRegisteredScope(FOO_SCOPE) instanceof NoOpScope);
	}

	@Test
	public void testSunnyDayWithBonaFideScopeClassname() throws Exception {
		Map<String, Object> scopes = new HashMap<String, Object>();
		scopes.put(FOO_SCOPE, NoOpScope.class.getName());
		CustomScopeConfigurer figurer = new CustomScopeConfigurer();
		figurer.setScopes(scopes);
		figurer.postProcessBeanFactory(factory);
		assertTrue(factory.getRegisteredScope(FOO_SCOPE) instanceof NoOpScope);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testWhereScopeMapHasNullScopeValueInEntrySet() throws Exception {
		Map<String, Object> scopes = new HashMap<String, Object>();
		scopes.put(FOO_SCOPE, null);
		CustomScopeConfigurer figurer = new CustomScopeConfigurer();
		figurer.setScopes(scopes);
		figurer.postProcessBeanFactory(factory);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testWhereScopeMapHasNonScopeInstanceInEntrySet() throws Exception {
		Map<String, Object> scopes = new HashMap<String, Object>();
		scopes.put(FOO_SCOPE, this); // <-- not a valid value...
		CustomScopeConfigurer figurer = new CustomScopeConfigurer();
		figurer.setScopes(scopes);
		figurer.postProcessBeanFactory(factory);
	}

	@SuppressWarnings("unchecked")
	@Test(expected=ClassCastException.class)
	public void testWhereScopeMapHasNonStringTypedScopeNameInKeySet() throws Exception {
		Map scopes = new HashMap();
		scopes.put(this, new NoOpScope()); // <-- not a valid value (the key)...
		CustomScopeConfigurer figurer = new CustomScopeConfigurer();
		figurer.setScopes(scopes);
		figurer.postProcessBeanFactory(factory);
	}

}
