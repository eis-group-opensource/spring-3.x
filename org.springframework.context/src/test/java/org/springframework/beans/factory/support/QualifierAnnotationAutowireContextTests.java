/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.junit.Assert.*;
import org.junit.Test;

import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.support.GenericApplicationContext;

/**
 * Integration tests for handling {@link Qualifier} annotations.
 * 
 * @author Mark Fisher
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public class QualifierAnnotationAutowireContextTests {
	
	private static final String JUERGEN = "juergen";

	private static final String MARK = "mark";


	@Test
	public void testAutowiredFieldWithSingleNonQualifiedCandidate() {
		GenericApplicationContext context = new GenericApplicationContext();
		ConstructorArgumentValues cavs = new ConstructorArgumentValues();
		cavs.addGenericArgumentValue(JUERGEN);
		RootBeanDefinition person = new RootBeanDefinition(Person.class, cavs, null);
		context.registerBeanDefinition(JUERGEN, person);
		context.registerBeanDefinition("autowired", 
				new RootBeanDefinition(QualifiedFieldTestBean.class));
		AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
		try {
			context.refresh();
			fail("expected BeanCreationException");
		}
		catch (BeanCreationException e) {
			assertTrue(e.getRootCause() instanceof NoSuchBeanDefinitionException);
			assertEquals("autowired", e.getBeanName());
		}
	}

	@Test
	public void testAutowiredMethodParameterWithSingleNonQualifiedCandidate() {
		GenericApplicationContext context = new GenericApplicationContext();
		ConstructorArgumentValues cavs = new ConstructorArgumentValues();
		cavs.addGenericArgumentValue(JUERGEN);
		RootBeanDefinition person = new RootBeanDefinition(Person.class, cavs, null);
		context.registerBeanDefinition(JUERGEN, person);
		context.registerBeanDefinition("autowired", 
				new RootBeanDefinition(QualifiedMethodParameterTestBean.class));
		AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
		try {
			context.refresh();
			fail("expected BeanCreationException");
		}
		catch (BeanCreationException e) {
			assertTrue(e.getRootCause() instanceof NoSuchBeanDefinitionException);
			assertEquals("autowired", e.getBeanName());
		}
	}

	@Test
	public void testAutowiredConstructorArgumentWithSingleNonQualifiedCandidate() {
		GenericApplicationContext context = new GenericApplicationContext();
		ConstructorArgumentValues cavs = new ConstructorArgumentValues();
		cavs.addGenericArgumentValue(JUERGEN);
		RootBeanDefinition person = new RootBeanDefinition(Person.class, cavs, null);
		context.registerBeanDefinition(JUERGEN, person);
		context.registerBeanDefinition("autowired", 
				new RootBeanDefinition(QualifiedConstructorArgumentTestBean.class));
		AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
		try {
			context.refresh();
			fail("expected BeanCreationException");
		}
		catch (BeanCreationException e) {
			assertTrue(e instanceof UnsatisfiedDependencyException);
			assertEquals("autowired", e.getBeanName());
		}
	}

	@Test
	public void testAutowiredFieldWithSingleQualifiedCandidate() {
		GenericApplicationContext context = new GenericApplicationContext();
		ConstructorArgumentValues cavs = new ConstructorArgumentValues();
		cavs.addGenericArgumentValue(JUERGEN);
		RootBeanDefinition person = new RootBeanDefinition(Person.class, cavs, null);
		person.addQualifier(new AutowireCandidateQualifier(TestQualifier.class));
		context.registerBeanDefinition(JUERGEN, person);
		context.registerBeanDefinition("autowired", new RootBeanDefinition(QualifiedFieldTestBean.class));
		AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
		context.refresh();
		QualifiedFieldTestBean bean = (QualifiedFieldTestBean) context.getBean("autowired");
		assertEquals(JUERGEN, bean.getPerson().getName());
	}

	@Test
	public void testAutowiredMethodParameterWithSingleQualifiedCandidate() {
		GenericApplicationContext context = new GenericApplicationContext();
		ConstructorArgumentValues cavs = new ConstructorArgumentValues();
		cavs.addGenericArgumentValue(JUERGEN);
		RootBeanDefinition person = new RootBeanDefinition(Person.class, cavs, null);
		person.addQualifier(new AutowireCandidateQualifier(TestQualifier.class));
		context.registerBeanDefinition(JUERGEN, person);
		context.registerBeanDefinition("autowired", 
				new RootBeanDefinition(QualifiedMethodParameterTestBean.class));
		AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
		context.refresh();
		QualifiedMethodParameterTestBean bean = 
				(QualifiedMethodParameterTestBean) context.getBean("autowired");
		assertEquals(JUERGEN, bean.getPerson().getName());
	}

	@Test
	public void testAutowiredMethodParameterWithStaticallyQualifiedCandidate() {
		GenericApplicationContext context = new GenericApplicationContext();
		ConstructorArgumentValues cavs = new ConstructorArgumentValues();
		cavs.addGenericArgumentValue(JUERGEN);
		RootBeanDefinition person = new RootBeanDefinition(QualifiedPerson.class, cavs, null);
		context.registerBeanDefinition(JUERGEN,
				ScopedProxyUtils.createScopedProxy(new BeanDefinitionHolder(person, JUERGEN), context, true).getBeanDefinition());
		context.registerBeanDefinition("autowired",
				new RootBeanDefinition(QualifiedMethodParameterTestBean.class));
		AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
		context.refresh();
		QualifiedMethodParameterTestBean bean =
				(QualifiedMethodParameterTestBean) context.getBean("autowired");
		assertEquals(JUERGEN, bean.getPerson().getName());
	}

	@Test
	public void testAutowiredMethodParameterWithStaticallyQualifiedCandidateAmongOthers() {
		GenericApplicationContext context = new GenericApplicationContext();
		ConstructorArgumentValues cavs = new ConstructorArgumentValues();
		cavs.addGenericArgumentValue(JUERGEN);
		RootBeanDefinition person = new RootBeanDefinition(QualifiedPerson.class, cavs, null);
		ConstructorArgumentValues cavs2 = new ConstructorArgumentValues();
		cavs2.addGenericArgumentValue(MARK);
		RootBeanDefinition person2 = new RootBeanDefinition(Person.class, cavs2, null);
		context.registerBeanDefinition(JUERGEN, person);
		context.registerBeanDefinition(MARK, person2);
		context.registerBeanDefinition("autowired",
				new RootBeanDefinition(QualifiedMethodParameterTestBean.class));
		AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
		context.refresh();
		QualifiedMethodParameterTestBean bean =
				(QualifiedMethodParameterTestBean) context.getBean("autowired");
		assertEquals(JUERGEN, bean.getPerson().getName());
	}

	@Test
	public void testAutowiredConstructorArgumentWithSingleQualifiedCandidate() {
		GenericApplicationContext context = new GenericApplicationContext();
		ConstructorArgumentValues cavs = new ConstructorArgumentValues();
		cavs.addGenericArgumentValue(JUERGEN);
		RootBeanDefinition person = new RootBeanDefinition(Person.class, cavs, null);
		person.addQualifier(new AutowireCandidateQualifier(TestQualifier.class));
		context.registerBeanDefinition(JUERGEN, person);
		context.registerBeanDefinition("autowired", 
				new RootBeanDefinition(QualifiedConstructorArgumentTestBean.class));
		AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
		context.refresh();
		QualifiedConstructorArgumentTestBean bean = 
				(QualifiedConstructorArgumentTestBean) context.getBean("autowired");
		assertEquals(JUERGEN, bean.getPerson().getName());
	}

	@Test
	public void testAutowiredFieldWithMultipleNonQualifiedCandidates() {
		GenericApplicationContext context = new GenericApplicationContext();
		ConstructorArgumentValues cavs1 = new ConstructorArgumentValues();
		cavs1.addGenericArgumentValue(JUERGEN);
		RootBeanDefinition person1 = new RootBeanDefinition(Person.class, cavs1, null);
		ConstructorArgumentValues cavs2 = new ConstructorArgumentValues();
		cavs2.addGenericArgumentValue(MARK);
		RootBeanDefinition person2 = new RootBeanDefinition(Person.class, cavs2, null);
		context.registerBeanDefinition(JUERGEN, person1);
		context.registerBeanDefinition(MARK, person2);
		context.registerBeanDefinition("autowired", 
				new RootBeanDefinition(QualifiedFieldTestBean.class));
		AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
		try {
			context.refresh();
			fail("expected BeanCreationException");
		}
		catch (BeanCreationException e) {
			assertTrue(e.getRootCause() instanceof NoSuchBeanDefinitionException);
			assertEquals("autowired", e.getBeanName());
		}
	}

	@Test
	public void testAutowiredMethodParameterWithMultipleNonQualifiedCandidates() {
		GenericApplicationContext context = new GenericApplicationContext();
		ConstructorArgumentValues cavs1 = new ConstructorArgumentValues();
		cavs1.addGenericArgumentValue(JUERGEN);
		RootBeanDefinition person1 = new RootBeanDefinition(Person.class, cavs1, null);
		ConstructorArgumentValues cavs2 = new ConstructorArgumentValues();
		cavs2.addGenericArgumentValue(MARK);
		RootBeanDefinition person2 = new RootBeanDefinition(Person.class, cavs2, null);
		context.registerBeanDefinition(JUERGEN, person1);
		context.registerBeanDefinition(MARK, person2);
		context.registerBeanDefinition("autowired", 
				new RootBeanDefinition(QualifiedMethodParameterTestBean.class));
		AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
		try {
			context.refresh();
			fail("expected BeanCreationException");
		}
		catch (BeanCreationException e) {
			assertTrue(e.getRootCause() instanceof NoSuchBeanDefinitionException);
			assertEquals("autowired", e.getBeanName());
		}
	}

	@Test
	public void testAutowiredConstructorArgumentWithMultipleNonQualifiedCandidates() {
		GenericApplicationContext context = new GenericApplicationContext();
		ConstructorArgumentValues cavs1 = new ConstructorArgumentValues();
		cavs1.addGenericArgumentValue(JUERGEN);
		RootBeanDefinition person1 = new RootBeanDefinition(Person.class, cavs1, null);
		ConstructorArgumentValues cavs2 = new ConstructorArgumentValues();
		cavs2.addGenericArgumentValue(MARK);
		RootBeanDefinition person2 = new RootBeanDefinition(Person.class, cavs2, null);
		context.registerBeanDefinition(JUERGEN, person1);
		context.registerBeanDefinition(MARK, person2);
		context.registerBeanDefinition("autowired", 
				new RootBeanDefinition(QualifiedConstructorArgumentTestBean.class));
		AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
		try {
			context.refresh();
			fail("expected BeanCreationException");
		}
		catch (BeanCreationException e) {
			assertTrue(e instanceof UnsatisfiedDependencyException);
			assertEquals("autowired", e.getBeanName());
		}
	}

	@Test
	public void testAutowiredFieldResolvesQualifiedCandidate() {
		GenericApplicationContext context = new GenericApplicationContext();
		ConstructorArgumentValues cavs1 = new ConstructorArgumentValues();
		cavs1.addGenericArgumentValue(JUERGEN);
		RootBeanDefinition person1 = new RootBeanDefinition(Person.class, cavs1, null);
		person1.addQualifier(new AutowireCandidateQualifier(TestQualifier.class));
		ConstructorArgumentValues cavs2 = new ConstructorArgumentValues();
		cavs2.addGenericArgumentValue(MARK);
		RootBeanDefinition person2 = new RootBeanDefinition(Person.class, cavs2, null);
		context.registerBeanDefinition(JUERGEN, person1);
		context.registerBeanDefinition(MARK, person2);
		context.registerBeanDefinition("autowired", 
				new RootBeanDefinition(QualifiedFieldTestBean.class));
		AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
		context.refresh();
		QualifiedFieldTestBean bean = (QualifiedFieldTestBean) context.getBean("autowired");
		assertEquals(JUERGEN, bean.getPerson().getName());
	}

	@Test
	public void testAutowiredMethodParameterResolvesQualifiedCandidate() {
		GenericApplicationContext context = new GenericApplicationContext();
		ConstructorArgumentValues cavs1 = new ConstructorArgumentValues();
		cavs1.addGenericArgumentValue(JUERGEN);
		RootBeanDefinition person1 = new RootBeanDefinition(Person.class, cavs1, null);
		person1.addQualifier(new AutowireCandidateQualifier(TestQualifier.class));
		ConstructorArgumentValues cavs2 = new ConstructorArgumentValues();
		cavs2.addGenericArgumentValue(MARK);
		RootBeanDefinition person2 = new RootBeanDefinition(Person.class, cavs2, null);
		context.registerBeanDefinition(JUERGEN, person1);
		context.registerBeanDefinition(MARK, person2);
		context.registerBeanDefinition("autowired", 
				new RootBeanDefinition(QualifiedMethodParameterTestBean.class));
		AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
		context.refresh();
		QualifiedMethodParameterTestBean bean = 
				(QualifiedMethodParameterTestBean) context.getBean("autowired");
		assertEquals(JUERGEN, bean.getPerson().getName());
	}

	@Test
	public void testAutowiredConstructorArgumentResolvesQualifiedCandidate() {
		GenericApplicationContext context = new GenericApplicationContext();
		ConstructorArgumentValues cavs1 = new ConstructorArgumentValues();
		cavs1.addGenericArgumentValue(JUERGEN);
		RootBeanDefinition person1 = new RootBeanDefinition(Person.class, cavs1, null);
		person1.addQualifier(new AutowireCandidateQualifier(TestQualifier.class));
		ConstructorArgumentValues cavs2 = new ConstructorArgumentValues();
		cavs2.addGenericArgumentValue(MARK);
		RootBeanDefinition person2 = new RootBeanDefinition(Person.class, cavs2, null);
		context.registerBeanDefinition(JUERGEN, person1);
		context.registerBeanDefinition(MARK, person2);
		context.registerBeanDefinition("autowired", 
				new RootBeanDefinition(QualifiedConstructorArgumentTestBean.class));
		AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
		context.refresh();
		QualifiedConstructorArgumentTestBean bean = 
				(QualifiedConstructorArgumentTestBean) context.getBean("autowired");
		assertEquals(JUERGEN, bean.getPerson().getName());
	}

	@Test
	public void testAutowiredFieldResolvesQualifiedCandidateWithDefaultValueAndNoValueOnBeanDefinition() {
		GenericApplicationContext context = new GenericApplicationContext();
		ConstructorArgumentValues cavs1 = new ConstructorArgumentValues();
		cavs1.addGenericArgumentValue(JUERGEN);
		RootBeanDefinition person1 = new RootBeanDefinition(Person.class, cavs1, null);
		// qualifier added, but includes no value
		person1.addQualifier(new AutowireCandidateQualifier(TestQualifierWithDefaultValue.class));
		ConstructorArgumentValues cavs2 = new ConstructorArgumentValues();
		cavs2.addGenericArgumentValue(MARK);
		RootBeanDefinition person2 = new RootBeanDefinition(Person.class, cavs2, null);
		context.registerBeanDefinition(JUERGEN, person1);
		context.registerBeanDefinition(MARK, person2);
		context.registerBeanDefinition("autowired", 
				new RootBeanDefinition(QualifiedFieldWithDefaultValueTestBean.class));
		AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
		context.refresh();
		QualifiedFieldWithDefaultValueTestBean bean = 
				(QualifiedFieldWithDefaultValueTestBean) context.getBean("autowired");
		assertEquals(JUERGEN, bean.getPerson().getName());
	}

	@Test
	public void testAutowiredFieldDoesNotResolveCandidateWithDefaultValueAndConflictingValueOnBeanDefinition() {
		GenericApplicationContext context = new GenericApplicationContext();
		ConstructorArgumentValues cavs1 = new ConstructorArgumentValues();
		cavs1.addGenericArgumentValue(JUERGEN);
		RootBeanDefinition person1 = new RootBeanDefinition(Person.class, cavs1, null);
		// qualifier added, and non-default value specified
		person1.addQualifier(new AutowireCandidateQualifier(TestQualifierWithDefaultValue.class, "not the default"));
		ConstructorArgumentValues cavs2 = new ConstructorArgumentValues();
		cavs2.addGenericArgumentValue(MARK);
		RootBeanDefinition person2 = new RootBeanDefinition(Person.class, cavs2, null);
		context.registerBeanDefinition(JUERGEN, person1);
		context.registerBeanDefinition(MARK, person2);
		context.registerBeanDefinition("autowired", 
				new RootBeanDefinition(QualifiedFieldWithDefaultValueTestBean.class));
		AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
		try {
			context.refresh();
			fail("expected BeanCreationException");
		}
		catch (BeanCreationException e) {
			assertTrue(e.getRootCause() instanceof NoSuchBeanDefinitionException);
			assertEquals("autowired", e.getBeanName());
		}
	}

	@Test
	public void testAutowiredFieldResolvesWithDefaultValueAndExplicitDefaultValueOnBeanDefinition() {
		GenericApplicationContext context = new GenericApplicationContext();
		ConstructorArgumentValues cavs1 = new ConstructorArgumentValues();
		cavs1.addGenericArgumentValue(JUERGEN);
		RootBeanDefinition person1 = new RootBeanDefinition(Person.class, cavs1, null);
		// qualifier added, and value matches the default
		person1.addQualifier(new AutowireCandidateQualifier(TestQualifierWithDefaultValue.class, "default"));
		ConstructorArgumentValues cavs2 = new ConstructorArgumentValues();
		cavs2.addGenericArgumentValue(MARK);
		RootBeanDefinition person2 = new RootBeanDefinition(Person.class, cavs2, null);
		context.registerBeanDefinition(JUERGEN, person1);
		context.registerBeanDefinition(MARK, person2);
		context.registerBeanDefinition("autowired", 
				new RootBeanDefinition(QualifiedFieldWithDefaultValueTestBean.class));
		AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
		context.refresh();
		QualifiedFieldWithDefaultValueTestBean bean = 
				(QualifiedFieldWithDefaultValueTestBean) context.getBean("autowired");
		assertEquals(JUERGEN, bean.getPerson().getName());
	}

	@Test
	public void testAutowiredFieldResolvesWithMultipleQualifierValues() {
		GenericApplicationContext context = new GenericApplicationContext();
		ConstructorArgumentValues cavs1 = new ConstructorArgumentValues();
		cavs1.addGenericArgumentValue(JUERGEN);
		RootBeanDefinition person1 = new RootBeanDefinition(Person.class, cavs1, null);
		AutowireCandidateQualifier qualifier = new AutowireCandidateQualifier(TestQualifierWithMultipleAttributes.class);
		qualifier.setAttribute("number", 456);
		person1.addQualifier(qualifier);
		ConstructorArgumentValues cavs2 = new ConstructorArgumentValues();
		cavs2.addGenericArgumentValue(MARK);
		RootBeanDefinition person2 = new RootBeanDefinition(Person.class, cavs2, null);
		AutowireCandidateQualifier qualifier2 = new AutowireCandidateQualifier(TestQualifierWithMultipleAttributes.class);
		qualifier2.setAttribute("number", 123);
		person2.addQualifier(qualifier2);
		context.registerBeanDefinition(JUERGEN, person1);
		context.registerBeanDefinition(MARK, person2);
		context.registerBeanDefinition("autowired", 
				new RootBeanDefinition(QualifiedFieldWithMultipleAttributesTestBean.class));
		AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
		context.refresh();
		QualifiedFieldWithMultipleAttributesTestBean bean = 
				(QualifiedFieldWithMultipleAttributesTestBean) context.getBean("autowired");
		assertEquals(MARK, bean.getPerson().getName());
	}

	@Test
	public void testAutowiredFieldDoesNotResolveWithMultipleQualifierValuesAndConflictingDefaultValue() {
		GenericApplicationContext context = new GenericApplicationContext();
		ConstructorArgumentValues cavs1 = new ConstructorArgumentValues();
		cavs1.addGenericArgumentValue(JUERGEN);
		RootBeanDefinition person1 = new RootBeanDefinition(Person.class, cavs1, null);
		AutowireCandidateQualifier qualifier = new AutowireCandidateQualifier(TestQualifierWithMultipleAttributes.class);
		qualifier.setAttribute("number", 456);
		person1.addQualifier(qualifier);
		ConstructorArgumentValues cavs2 = new ConstructorArgumentValues();
		cavs2.addGenericArgumentValue(MARK);
		RootBeanDefinition person2 = new RootBeanDefinition(Person.class, cavs2, null);
		AutowireCandidateQualifier qualifier2 = new AutowireCandidateQualifier(TestQualifierWithMultipleAttributes.class);
		qualifier2.setAttribute("number", 123);
		qualifier2.setAttribute("value", "not the default");
		person2.addQualifier(qualifier2);
		context.registerBeanDefinition(JUERGEN, person1);
		context.registerBeanDefinition(MARK, person2);
		context.registerBeanDefinition("autowired", 
				new RootBeanDefinition(QualifiedFieldWithMultipleAttributesTestBean.class));
		AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
		try {
			context.refresh();
			fail("expected BeanCreationException");
		}
		catch (BeanCreationException e) {
			assertTrue(e.getRootCause() instanceof NoSuchBeanDefinitionException);
			assertEquals("autowired", e.getBeanName());
		}
	}

	@Test
	public void testAutowiredFieldResolvesWithMultipleQualifierValuesAndExplicitDefaultValue() {
		GenericApplicationContext context = new GenericApplicationContext();
		ConstructorArgumentValues cavs1 = new ConstructorArgumentValues();
		cavs1.addGenericArgumentValue(JUERGEN);
		RootBeanDefinition person1 = new RootBeanDefinition(Person.class, cavs1, null);
		AutowireCandidateQualifier qualifier = new AutowireCandidateQualifier(TestQualifierWithMultipleAttributes.class);
		qualifier.setAttribute("number", 456);
		person1.addQualifier(qualifier);
		ConstructorArgumentValues cavs2 = new ConstructorArgumentValues();
		cavs2.addGenericArgumentValue(MARK);
		RootBeanDefinition person2 = new RootBeanDefinition(Person.class, cavs2, null);
		AutowireCandidateQualifier qualifier2 = new AutowireCandidateQualifier(TestQualifierWithMultipleAttributes.class);
		qualifier2.setAttribute("number", 123);
		qualifier2.setAttribute("value", "default");
		person2.addQualifier(qualifier2);
		context.registerBeanDefinition(JUERGEN, person1);
		context.registerBeanDefinition(MARK, person2);
		context.registerBeanDefinition("autowired", 
				new RootBeanDefinition(QualifiedFieldWithMultipleAttributesTestBean.class));
		AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
		context.refresh();
		QualifiedFieldWithMultipleAttributesTestBean bean = 
				(QualifiedFieldWithMultipleAttributesTestBean) context.getBean("autowired");
		assertEquals(MARK, bean.getPerson().getName());
	}

	@Test
	public void testAutowiredFieldDoesNotResolveWithMultipleQualifierValuesAndMultipleMatchingCandidates() {
		GenericApplicationContext context = new GenericApplicationContext();
		ConstructorArgumentValues cavs1 = new ConstructorArgumentValues();
		cavs1.addGenericArgumentValue(JUERGEN);
		RootBeanDefinition person1 = new RootBeanDefinition(Person.class, cavs1, null);
		AutowireCandidateQualifier qualifier = new AutowireCandidateQualifier(TestQualifierWithMultipleAttributes.class);
		qualifier.setAttribute("number", 123);
		person1.addQualifier(qualifier);
		ConstructorArgumentValues cavs2 = new ConstructorArgumentValues();
		cavs2.addGenericArgumentValue(MARK);
		RootBeanDefinition person2 = new RootBeanDefinition(Person.class, cavs2, null);
		AutowireCandidateQualifier qualifier2 = new AutowireCandidateQualifier(TestQualifierWithMultipleAttributes.class);
		qualifier2.setAttribute("number", 123);
		qualifier2.setAttribute("value", "default");
		person2.addQualifier(qualifier2);
		context.registerBeanDefinition(JUERGEN, person1);
		context.registerBeanDefinition(MARK, person2);
		context.registerBeanDefinition("autowired", 
				new RootBeanDefinition(QualifiedFieldWithMultipleAttributesTestBean.class));
		AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
		try {
			context.refresh();
			fail("expected BeanCreationException");
		}
		catch (BeanCreationException e) {
			assertTrue(e.getRootCause() instanceof NoSuchBeanDefinitionException);
			assertEquals("autowired", e.getBeanName());
		}
	}

	@Test
	public void testAutowiredFieldResolvesWithBaseQualifierAndDefaultValue() {
		GenericApplicationContext context = new GenericApplicationContext();
		ConstructorArgumentValues cavs1 = new ConstructorArgumentValues();
		cavs1.addGenericArgumentValue(JUERGEN);
		RootBeanDefinition person1 = new RootBeanDefinition(Person.class, cavs1, null);
		ConstructorArgumentValues cavs2 = new ConstructorArgumentValues();
		cavs2.addGenericArgumentValue(MARK);
		RootBeanDefinition person2 = new RootBeanDefinition(Person.class, cavs2, null);
		person2.addQualifier(new AutowireCandidateQualifier(Qualifier.class));
		context.registerBeanDefinition(JUERGEN, person1);
		context.registerBeanDefinition(MARK, person2);
		context.registerBeanDefinition("autowired", 
				new RootBeanDefinition(QualifiedFieldWithBaseQualifierDefaultValueTestBean.class));
		AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
		context.refresh();
		QualifiedFieldWithBaseQualifierDefaultValueTestBean bean = 
				(QualifiedFieldWithBaseQualifierDefaultValueTestBean) context.getBean("autowired");
		assertEquals(MARK, bean.getPerson().getName());
	}

	@Test
	public void testAutowiredFieldResolvesWithBaseQualifierAndNonDefaultValue() {
		GenericApplicationContext context = new GenericApplicationContext();
		ConstructorArgumentValues cavs1 = new ConstructorArgumentValues();
		cavs1.addGenericArgumentValue("the real juergen");
		RootBeanDefinition person1 = new RootBeanDefinition(Person.class, cavs1, null);
		person1.addQualifier(new AutowireCandidateQualifier(Qualifier.class, "juergen"));
		ConstructorArgumentValues cavs2 = new ConstructorArgumentValues();
		cavs2.addGenericArgumentValue("juergen imposter");
		RootBeanDefinition person2 = new RootBeanDefinition(Person.class, cavs2, null);
		person2.addQualifier(new AutowireCandidateQualifier(Qualifier.class, "not really juergen"));
		context.registerBeanDefinition("juergen1", person1);
		context.registerBeanDefinition("juergen2", person2);
		context.registerBeanDefinition("autowired", 
				new RootBeanDefinition(QualifiedConstructorArgumentWithBaseQualifierNonDefaultValueTestBean.class));
		AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
		context.refresh();
		QualifiedConstructorArgumentWithBaseQualifierNonDefaultValueTestBean bean = 
				(QualifiedConstructorArgumentWithBaseQualifierNonDefaultValueTestBean) context.getBean("autowired");
		assertEquals("the real juergen", bean.getPerson().getName());
	}

	@Test
	public void testAutowiredFieldDoesNotResolveWithBaseQualifierAndNonDefaultValueAndMultipleMatchingCandidates() {
		GenericApplicationContext context = new GenericApplicationContext();
		ConstructorArgumentValues cavs1 = new ConstructorArgumentValues();
		cavs1.addGenericArgumentValue("the real juergen");
		RootBeanDefinition person1 = new RootBeanDefinition(Person.class, cavs1, null);
		person1.addQualifier(new AutowireCandidateQualifier(Qualifier.class, "juergen"));
		ConstructorArgumentValues cavs2 = new ConstructorArgumentValues();
		cavs2.addGenericArgumentValue("juergen imposter");
		RootBeanDefinition person2 = new RootBeanDefinition(Person.class, cavs2, null);
		person2.addQualifier(new AutowireCandidateQualifier(Qualifier.class, "juergen"));
		context.registerBeanDefinition("juergen1", person1);
		context.registerBeanDefinition("juergen2", person2);
		context.registerBeanDefinition("autowired", 
				new RootBeanDefinition(QualifiedConstructorArgumentWithBaseQualifierNonDefaultValueTestBean.class));
		AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
		try {
			context.refresh();
			fail("expected BeanCreationException");
		}
		catch (BeanCreationException e) {
			assertTrue(e instanceof UnsatisfiedDependencyException);
			assertEquals("autowired", e.getBeanName());
		}
	}


	private static class QualifiedFieldTestBean {

		@Autowired
		@TestQualifier
		private Person person;
		
		public Person getPerson() {
			return this.person;
		}
	}


	private static class QualifiedMethodParameterTestBean {

		private Person person;
		
		@Autowired
		public void setPerson(@TestQualifier Person person) {
			this.person = person;
		}

		public Person getPerson() {
			return this.person;
		}
	}
	
	
	private static class QualifiedConstructorArgumentTestBean {

		private Person person;
		
		@Autowired
		public QualifiedConstructorArgumentTestBean(@TestQualifier Person person) {
			this.person = person;
		}
		
		public Person getPerson() {
			return this.person;
		}

	}


	public static class QualifiedFieldWithDefaultValueTestBean {

		@Autowired
		@TestQualifierWithDefaultValue
		private Person person;
		
		public Person getPerson() {
			return this.person;
		}
	}


	public static class QualifiedFieldWithMultipleAttributesTestBean {

		@Autowired
		@TestQualifierWithMultipleAttributes(number=123)
		private Person person;
		
		public Person getPerson() {
			return this.person;
		}
	}


	private static class QualifiedFieldWithBaseQualifierDefaultValueTestBean {

		@Autowired
		@Qualifier
		private Person person;
		
		public Person getPerson() {
			return this.person;
		}
	}


	public static class QualifiedConstructorArgumentWithBaseQualifierNonDefaultValueTestBean {

		private Person person;

		@Autowired
		public QualifiedConstructorArgumentWithBaseQualifierNonDefaultValueTestBean(
				@Qualifier("juergen") Person person) {
			this.person = person;
		}
		
		public Person getPerson() {
			return this.person;
		}
	}


	private static class Person {

		private String name;

		public Person(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}


	@TestQualifier
	private static class QualifiedPerson extends Person {

		public QualifiedPerson() {
			super(null);
		}

		public QualifiedPerson(String name) {
			super(name);
		}
	}


	@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Qualifier
	public static @interface TestQualifier {
	}


	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	@Qualifier
	public static @interface TestQualifierWithDefaultValue {

		String value() default "default";
	}


	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	@Qualifier
	public static @interface TestQualifierWithMultipleAttributes {

		String value() default "default";

		int number();
	}

}
