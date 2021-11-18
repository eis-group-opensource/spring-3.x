/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.scheduling.annotation.AsyncAnnotationBeanPostProcessor;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.springframework.context.annotation.MetadataUtils.*;

/**
 * Tests that an ImportAware @Configuration classes gets injected with the
 * annotation metadata of the @Configuration class that imported it.
 *
 * @author Chris Beams
 * @since 3.1
 */
public class ImportAwareTests {

	@Test
	public void directlyAnnotatedWithImport() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(ImportingConfig.class);
		ctx.refresh();

		ctx.getBean("importedConfigBean");

		ImportedConfig importAwareConfig = ctx.getBean(ImportedConfig.class);
		AnnotationMetadata importMetadata = importAwareConfig.importMetadata;
		assertThat("import metadata was not injected", importMetadata, notNullValue());
		assertThat(importMetadata.getClassName(), is(ImportingConfig.class.getName()));
		AnnotationAttributes importAttribs = attributesFor(importMetadata, Import.class);
		Class<?>[] importedClasses = importAttribs.getClassArray("value");
		assertThat(importedClasses[0].getName(), is(ImportedConfig.class.getName()));
	}

	@Test
	public void indirectlyAnnotatedWithImport() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(IndirectlyImportingConfig.class);
		ctx.refresh();

		ctx.getBean("importedConfigBean");

		ImportedConfig importAwareConfig = ctx.getBean(ImportedConfig.class);
		AnnotationMetadata importMetadata = importAwareConfig.importMetadata;
		assertThat("import metadata was not injected", importMetadata, notNullValue());
		assertThat(importMetadata.getClassName(), is(IndirectlyImportingConfig.class.getName()));
		AnnotationAttributes enableAttribs = attributesFor(importMetadata, EnableImportedConfig.class);
		String foo = enableAttribs.getString("foo");
		assertThat(foo, is("xyz"));
	}


	@Configuration
	@Import(ImportedConfig.class)
	static class ImportingConfig {
	}

	@Configuration
	@EnableImportedConfig(foo="xyz")
	static class IndirectlyImportingConfig {
	}


	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	@Import(ImportedConfig.class)
	public @interface EnableImportedConfig {
		String foo() default "";
	}


	@Configuration
	static class ImportedConfig implements ImportAware {

		AnnotationMetadata importMetadata;

		public void setImportMetadata(AnnotationMetadata importMetadata) {
			this.importMetadata = importMetadata;
		}

		@Bean
		public BPP importedConfigBean() {
			return new BPP();
		}

		@Bean
		public AsyncAnnotationBeanPostProcessor asyncBPP() {
			return new AsyncAnnotationBeanPostProcessor();
		}
	}


	static class BPP implements BeanFactoryAware, BeanPostProcessor {

		public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
			return bean;
		}

		public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
			return bean;
		}

		public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		}
	}
}
