/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation.configuration;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Test;
import test.beans.ITestBean;
import test.beans.TestBean;

import org.springframework.beans.factory.parsing.BeanDefinitionParsingException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.annotation.Import;


/**
 * System tests for {@link Import} annotation support.
 * 
 * @author Chris Beams
 */
public class ImportTests {

	private DefaultListableBeanFactory processConfigurationClasses(Class<?>... classes) {
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		for (Class<?> clazz : classes) {
			beanFactory.registerBeanDefinition(clazz.getSimpleName(), new RootBeanDefinition(clazz));
		}
		ConfigurationClassPostProcessor pp = new ConfigurationClassPostProcessor();
		pp.postProcessBeanFactory(beanFactory);
		return beanFactory;
	}

	private void assertBeanDefinitionCount(int expectedCount, Class<?>... classes) {
		DefaultListableBeanFactory beanFactory = processConfigurationClasses(classes);
		assertThat(beanFactory.getBeanDefinitionCount(), equalTo(expectedCount));
	}

	@Test
	public void testProcessImportsWithAsm() {
		int configClasses = 2;
		int beansInClasses = 2;
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		beanFactory.registerBeanDefinition("config", new RootBeanDefinition(ConfigurationWithImportAnnotation.class.getName()));
		ConfigurationClassPostProcessor pp = new ConfigurationClassPostProcessor();
		pp.postProcessBeanFactory(beanFactory);
		assertThat(beanFactory.getBeanDefinitionCount(), equalTo(configClasses + beansInClasses));
	}

	@Test
	public void testProcessImportsWithDoubleImports() {
		int configClasses = 3;
		int beansInClasses = 3;
		assertBeanDefinitionCount((configClasses + beansInClasses), ConfigurationWithImportAnnotation.class, OtherConfigurationWithImportAnnotation.class);
	}

	@Test
	public void testProcessImportsWithExplicitOverridingBefore() {
		int configClasses = 2;
		int beansInClasses = 2;
		assertBeanDefinitionCount((configClasses + beansInClasses), OtherConfiguration.class, ConfigurationWithImportAnnotation.class);
	}

	@Test
	public void testProcessImportsWithExplicitOverridingAfter() {
		int configClasses = 2;
		int beansInClasses = 2;
		assertBeanDefinitionCount((configClasses + beansInClasses), ConfigurationWithImportAnnotation.class, OtherConfiguration.class);
	}

	@Configuration
	@Import(OtherConfiguration.class)
	static class ConfigurationWithImportAnnotation {
		@Bean
		public ITestBean one() {
			return new TestBean();
		}
	}

	@Configuration
	@Import(OtherConfiguration.class)
	static class OtherConfigurationWithImportAnnotation {
		@Bean
		public ITestBean two() {
			return new TestBean();
		}
	}

	@Configuration
	static class OtherConfiguration {
		@Bean
		public ITestBean three() {
			return new TestBean();
		}
	}

	// ------------------------------------------------------------------------

	@Test
	public void testImportAnnotationWithTwoLevelRecursion() {
		int configClasses = 2;
		int beansInClasses = 3;

		assertBeanDefinitionCount((configClasses + beansInClasses), AppConfig.class);
	}

	@Configuration
	@Import(DataSourceConfig.class)
	static class AppConfig {
		
		@Bean
		public ITestBean transferService() {
			return new TestBean(accountRepository());
		}

		@Bean
		public ITestBean accountRepository() {
			return new TestBean();
		}
	}

	@Configuration
	static class DataSourceConfig {
		@Bean
		public ITestBean dataSourceA() {
			return new TestBean();
		}
	}

	// ------------------------------------------------------------------------

	@Test
	public void testImportAnnotationWithThreeLevelRecursion() {
		int configClasses = 3;
		int beansInClasses = 5;

		assertBeanDefinitionCount((configClasses + beansInClasses), FirstLevel.class);
	}

	// ------------------------------------------------------------------------

	@Test
	public void testImportAnnotationWithMultipleArguments() {
		int configClasses = 3;
		int beansInClasses = 3;

		assertBeanDefinitionCount((configClasses + beansInClasses),
				WithMultipleArgumentsToImportAnnotation.class);
	}


	@Test
	public void testImportAnnotationWithMultipleArgumentsResultingInOverriddenBeanDefinition() {
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		beanFactory.registerBeanDefinition("config", new RootBeanDefinition(
				WithMultipleArgumentsThatWillCauseDuplication.class));
		ConfigurationClassPostProcessor pp = new ConfigurationClassPostProcessor();
		pp.postProcessBeanFactory(beanFactory);
		assertThat(beanFactory.getBeanDefinitionCount(), equalTo(4));
		assertThat(beanFactory.getBean("foo", ITestBean.class).getName(), equalTo("foo2"));
	}

	@Configuration
	@Import( { Foo1.class, Foo2.class })
	static class WithMultipleArgumentsThatWillCauseDuplication {
	}

	@Configuration
	static class Foo1 {
		@Bean
		public ITestBean foo() {
			return new TestBean("foo1");
		}
	}

	@Configuration
	static class Foo2 {
		@Bean
		public ITestBean foo() {
			return new TestBean("foo2");
		}
	}

	// ------------------------------------------------------------------------

	@Test
	public void testImportAnnotationOnInnerClasses() {
		int configClasses = 2;
		int beansInClasses = 2;

		assertBeanDefinitionCount((configClasses + beansInClasses), OuterConfig.InnerConfig.class);
	}

	@Configuration
	static class OuterConfig {
		@Bean
		String whatev() {
			return "whatev";
		}

		@Configuration
		@Import(ExternalConfig.class)
		static class InnerConfig {
			@Bean
			public ITestBean innerBean() {
				return new TestBean();
			}
		}
	}

	@Configuration
	static class ExternalConfig {
		@Bean
		public ITestBean extBean() {
			return new TestBean();
		}
	}

	// ------------------------------------------------------------------------

	@Configuration
	@Import(SecondLevel.class)
	static class FirstLevel {
		@Bean
		public TestBean m() {
			return new TestBean();
		}
	}

	@Configuration
	@Import(ThirdLevel.class)
	static class SecondLevel {
		@Bean
		public TestBean n() {
			return new TestBean();
		}
	}

	@Configuration
	static class ThirdLevel {
		@Bean
		public ITestBean thirdLevelA() {
			return new TestBean();
		}

		@Bean
		public ITestBean thirdLevelB() {
			return new TestBean();
		}

		@Bean
		public ITestBean thirdLevelC() {
			return new TestBean();
		}
	}

	@Configuration
	@Import( { LeftConfig.class, RightConfig.class })
	static class WithMultipleArgumentsToImportAnnotation {
		@Bean
		public TestBean m() {
			return new TestBean();
		}
	}

	@Configuration
	static class LeftConfig {
		@Bean
		public ITestBean left() {
			return new TestBean();
		}
	}

	@Configuration
	static class RightConfig {
		@Bean
		public ITestBean right() {
			return new TestBean();
		}
	}

	// ------------------------------------------------------------------------

	@Test(expected=BeanDefinitionParsingException.class)
	public void testImportNonConfigurationAnnotationClassCausesError() {
		processConfigurationClasses(ConfigAnnotated.class);
	}

	@Configuration
	@Import(NonConfigAnnotated.class)
	static class ConfigAnnotated { }

	static class NonConfigAnnotated { }

	// ------------------------------------------------------------------------

	/**
	 * Test that values supplied to @Configuration(value="...") are propagated as the
	 * bean name for the configuration class even in the case of inclusion via @Import
	 * or in the case of automatic registration via nesting
	 */
	@Test
	public void reproSpr9023() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(B.class);
		ctx.refresh();
		System.out.println(ctx.getBeanFactory());
		assertThat(ctx.getBeanNamesForType(B.class)[0], is("config-b"));
		assertThat(ctx.getBeanNamesForType(A.class)[0], is("config-a"));
	}

	@Configuration("config-a")
	static class A { }

	@Configuration("config-b")
	@Import(A.class)
	static class B { }

	@Test
	public void testProcessImports() {
		int configClasses = 2;
		int beansInClasses = 2;
		assertBeanDefinitionCount((configClasses + beansInClasses), ConfigurationWithImportAnnotation.class);
	}

}
