/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.transaction.interceptor;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.transaction.TransactionDefinition;

/**
 * Unit tests for the various {@link TransactionAttributeSource} implementations.
 *
 * @author Colin Sampaleanu
 * @author Juergen Hoeller
 * @author Rick Evans
 * @author Chris Beams
 * @since 15.10.2003
 * @see org.springframework.transaction.interceptor.TransactionProxyFactoryBean
 */
public final class TransactionAttributeSourceTests {

	@Test
	public void testMatchAlwaysTransactionAttributeSource() throws Exception {
		MatchAlwaysTransactionAttributeSource tas = new MatchAlwaysTransactionAttributeSource();
		TransactionAttribute ta = tas.getTransactionAttribute(
				Object.class.getMethod("hashCode", (Class[]) null), null);
		assertNotNull(ta);
		assertTrue(TransactionDefinition.PROPAGATION_REQUIRED == ta.getPropagationBehavior());

		tas.setTransactionAttribute(new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_SUPPORTS));
		ta = tas.getTransactionAttribute(
				IOException.class.getMethod("getMessage", (Class[]) null), IOException.class);
		assertNotNull(ta);
		assertTrue(TransactionDefinition.PROPAGATION_SUPPORTS == ta.getPropagationBehavior());
	}

	@SuppressWarnings("unchecked")
	@Ignore // no longer works now that setMethodMap has been parameterized
	@Test
	public void testMethodMapTransactionAttributeSource() throws NoSuchMethodException {
		MethodMapTransactionAttributeSource tas = new MethodMapTransactionAttributeSource();
		Map methodMap = new HashMap();
		methodMap.put(Object.class.getName() + ".hashCode", TransactionDefinition.PROPAGATION_REQUIRED);
		methodMap.put(Object.class.getName() + ".toString",
				new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_SUPPORTS));
		tas.setMethodMap(methodMap);
		tas.afterPropertiesSet();
		TransactionAttribute ta = tas.getTransactionAttribute(
				Object.class.getMethod("hashCode", (Class[]) null), null);
		assertNotNull(ta);
		assertEquals(TransactionDefinition.PROPAGATION_REQUIRED, ta.getPropagationBehavior());
		ta = tas.getTransactionAttribute(Object.class.getMethod("toString", (Class[]) null), null);
		assertNotNull(ta);
		assertEquals(TransactionDefinition.PROPAGATION_SUPPORTS, ta.getPropagationBehavior());
	}

	@SuppressWarnings("unchecked")
	@Ignore // no longer works now that setMethodMap has been parameterized
	@Test
	public void testMethodMapTransactionAttributeSourceWithLazyInit() throws NoSuchMethodException {
		MethodMapTransactionAttributeSource tas = new MethodMapTransactionAttributeSource();
		Map methodMap = new HashMap();
		methodMap.put(Object.class.getName() + ".hashCode", "PROPAGATION_REQUIRED");
		methodMap.put(Object.class.getName() + ".toString",
				new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_SUPPORTS));
		tas.setMethodMap(methodMap);
		TransactionAttribute ta = tas.getTransactionAttribute(
				Object.class.getMethod("hashCode", (Class[]) null), null);
		assertNotNull(ta);
		assertEquals(TransactionDefinition.PROPAGATION_REQUIRED, ta.getPropagationBehavior());
		ta = tas.getTransactionAttribute(Object.class.getMethod("toString", (Class[]) null), null);
		assertNotNull(ta);
		assertEquals(TransactionDefinition.PROPAGATION_SUPPORTS, ta.getPropagationBehavior());
	}

	@SuppressWarnings("unchecked")
	@Ignore // no longer works now that setMethodMap has been parameterized
	@Test
	public void testNameMatchTransactionAttributeSource() throws NoSuchMethodException {
		NameMatchTransactionAttributeSource tas = new NameMatchTransactionAttributeSource();
		Map methodMap = new HashMap();
		methodMap.put("hashCode", "PROPAGATION_REQUIRED");
		methodMap.put("toString", new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_SUPPORTS));
		tas.setNameMap(methodMap);
		TransactionAttribute ta = tas.getTransactionAttribute(
				Object.class.getMethod("hashCode", (Class[]) null), null);
		assertNotNull(ta);
		assertEquals(TransactionDefinition.PROPAGATION_REQUIRED, ta.getPropagationBehavior());
		ta = tas.getTransactionAttribute(Object.class.getMethod("toString", (Class[]) null), null);
		assertNotNull(ta);
		assertEquals(TransactionDefinition.PROPAGATION_SUPPORTS, ta.getPropagationBehavior());
	}

	@Test
	public void testNameMatchTransactionAttributeSourceWithStarAtStartOfMethodName() throws NoSuchMethodException {
		NameMatchTransactionAttributeSource tas = new NameMatchTransactionAttributeSource();
		Properties attributes = new Properties();
		attributes.put("*ashCode", "PROPAGATION_REQUIRED");
		tas.setProperties(attributes);
		TransactionAttribute ta = tas.getTransactionAttribute(
				Object.class.getMethod("hashCode", (Class[]) null), null);
		assertNotNull(ta);
		assertEquals(TransactionDefinition.PROPAGATION_REQUIRED, ta.getPropagationBehavior());
	}

	@Test
	public void testNameMatchTransactionAttributeSourceWithStarAtEndOfMethodName() throws NoSuchMethodException {
		NameMatchTransactionAttributeSource tas = new NameMatchTransactionAttributeSource();
		Properties attributes = new Properties();
		attributes.put("hashCod*", "PROPAGATION_REQUIRED");
		tas.setProperties(attributes);
		TransactionAttribute ta = tas.getTransactionAttribute(
				Object.class.getMethod("hashCode", (Class[]) null), null);
		assertNotNull(ta);
		assertEquals(TransactionDefinition.PROPAGATION_REQUIRED, ta.getPropagationBehavior());
	}

	@Test
	public void testNameMatchTransactionAttributeSourceMostSpecificMethodNameIsDefinitelyMatched() throws NoSuchMethodException {
		NameMatchTransactionAttributeSource tas = new NameMatchTransactionAttributeSource();
		Properties attributes = new Properties();
		attributes.put("*", "PROPAGATION_REQUIRED");
		attributes.put("hashCode", "PROPAGATION_MANDATORY");
		tas.setProperties(attributes);
		TransactionAttribute ta = tas.getTransactionAttribute(
				Object.class.getMethod("hashCode", (Class[]) null), null);
		assertNotNull(ta);
		assertEquals(TransactionDefinition.PROPAGATION_MANDATORY, ta.getPropagationBehavior());
	}

	@Test
	public void testNameMatchTransactionAttributeSourceWithEmptyMethodName() throws NoSuchMethodException {
		NameMatchTransactionAttributeSource tas = new NameMatchTransactionAttributeSource();
		Properties attributes = new Properties();
		attributes.put("", "PROPAGATION_MANDATORY");
		tas.setProperties(attributes);
		TransactionAttribute ta = tas.getTransactionAttribute(
				Object.class.getMethod("hashCode", (Class[]) null), null);
		assertNull(ta);
	}

}
