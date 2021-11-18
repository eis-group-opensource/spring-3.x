/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import example.profilescan.ProfileAnnotatedComponent;
import example.scannable.AutowiredQualifierFooService;

/**
 * @author Mark Fisher
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public class ComponentScanParserTests {

	@Test
	public void testAspectJTypeFilter() {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"org/springframework/context/annotation/aspectjTypeFilterTests.xml");
		assertTrue(context.containsBean("fooServiceImpl"));
		assertTrue(context.containsBean("stubFooDao"));
		assertFalse(context.containsBean("scopedProxyTestBean"));
	}

	@Test
	public void testNonMatchingResourcePattern() {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"org/springframework/context/annotation/nonMatchingResourcePatternTests.xml");
		assertFalse(context.containsBean("fooServiceImpl"));
	}

	@Test
	public void testMatchingResourcePattern() {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"org/springframework/context/annotation/matchingResourcePatternTests.xml");
		assertTrue(context.containsBean("fooServiceImpl"));
	}

	@Test
	public void testComponentScanWithAutowiredQualifier() {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"org/springframework/context/annotation/componentScanWithAutowiredQualifierTests.xml");
		AutowiredQualifierFooService fooService = (AutowiredQualifierFooService) context.getBean("fooService");
		assertTrue(fooService.isInitCalled());
		assertEquals("bar", fooService.foo(123));
	}

	@Test
	public void testCustomAnnotationUsedForBothComponentScanAndQualifier() {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"org/springframework/context/annotation/customAnnotationUsedForBothComponentScanAndQualifierTests.xml");
		CustomAnnotationAutowiredBean testBean = (CustomAnnotationAutowiredBean) context.getBean("testBean");
		assertNotNull(testBean.getDependency());
	}

	@Test
	public void testCustomTypeFilter() {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"org/springframework/context/annotation/customTypeFilterTests.xml");
		CustomAnnotationAutowiredBean testBean = (CustomAnnotationAutowiredBean) context.getBean("testBean");
		assertNotNull(testBean.getDependency());
	}

	@Test
	public void testComponentScanRespectsProfileAnnotation() {
		String xmlLocation = "org/springframework/context/annotation/componentScanRespectsProfileAnnotationTests.xml";
		{ // should exclude the profile-annotated bean if active profiles remains unset
			GenericXmlApplicationContext context = new GenericXmlApplicationContext();
			context.load(xmlLocation);
			context.refresh();
			assertThat(context.containsBean(ProfileAnnotatedComponent.BEAN_NAME), is(false));
		}
		{ // should include the profile-annotated bean with active profiles set
			GenericXmlApplicationContext context = new GenericXmlApplicationContext();
			context.getEnvironment().setActiveProfiles(ProfileAnnotatedComponent.PROFILE_NAME);
			context.load(xmlLocation);
			context.refresh();
			assertThat(context.containsBean(ProfileAnnotatedComponent.BEAN_NAME), is(true));
		}
		{ // ensure the same works for AbstractRefreshableApplicationContext impls too
			ConfigurableApplicationContext context =
				new ClassPathXmlApplicationContext(new String[]{xmlLocation}, false);
			context.getEnvironment().setActiveProfiles(ProfileAnnotatedComponent.PROFILE_NAME);
			context.refresh();
			assertThat(context.containsBean(ProfileAnnotatedComponent.BEAN_NAME), is(true));
		}
	}


	@Target({ElementType.TYPE, ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface CustomAnnotation {	
	}


	public static class CustomAnnotationAutowiredBean {

		@Autowired
		@CustomAnnotation
		private CustomAnnotationDependencyBean dependency;

		public CustomAnnotationDependencyBean getDependency() {
			return this.dependency;
		}
	}


	@CustomAnnotation
	public static class CustomAnnotationDependencyBean {	
	}


	public static class CustomTypeFilter implements TypeFilter {

		public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) {
			return metadataReader.getClassMetadata().getClassName().contains("Custom");
		}
	}

}
