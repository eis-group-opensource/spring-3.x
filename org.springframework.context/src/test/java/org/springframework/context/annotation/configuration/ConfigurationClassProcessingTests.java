/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.parsing.BeanDefinitionParsingException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.GenericApplicationContext;

import test.beans.ITestBean;
import test.beans.TestBean;

/**
 * Miscellaneous system tests covering {@link Bean} naming, aliases, scoping and error
 * handling within {@link Configuration} class definitions.
 *
 * @author Chris Beams
 * @author Juergen Hoeller
 */
public class ConfigurationClassProcessingTests {

	/**
	 * Creates a new {@link BeanFactory}, populates it with a {@link BeanDefinition} for
	 * each of the given {@link Configuration} <var>configClasses</var>, and then
	 * post-processes the factory using JavaConfig's {@link ConfigurationClassPostProcessor}.
	 * When complete, the factory is ready to service requests for any {@link Bean} methods
	 * declared by <var>configClasses</var>.
	 */
	private BeanFactory initBeanFactory(Class<?>... configClasses) {
		DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
		for (Class<?> configClass : configClasses) {
			String configBeanName = configClass.getName();
			factory.registerBeanDefinition(configBeanName, new RootBeanDefinition(configClass));
		}
		ConfigurationClassPostProcessor ccpp = new ConfigurationClassPostProcessor();
		ccpp.postProcessBeanDefinitionRegistry(factory);
		ccpp.postProcessBeanFactory(factory);
		RequiredAnnotationBeanPostProcessor rapp = new RequiredAnnotationBeanPostProcessor();
		rapp.setBeanFactory(factory);
		factory.addBeanPostProcessor(rapp);
		return factory;
	}


	@Test
	public void customBeanNameIsRespected() {
		GenericApplicationContext ac = new GenericApplicationContext();
		AnnotationConfigUtils.registerAnnotationConfigProcessors(ac);
		ac.registerBeanDefinition("config", new RootBeanDefinition(ConfigWithBeanWithCustomName.class));
		ac.refresh();
		assertSame(ac.getBean("customName"), ConfigWithBeanWithCustomName.testBean);

		// method name should not be registered
		try {
			ac.getBean("methodName");
			fail("bean should not have been registered with 'methodName'");
		}
		catch (NoSuchBeanDefinitionException ex) {
			// expected
		}
	}

	@Test
	public void aliasesAreRespected() {
		BeanFactory factory = initBeanFactory(ConfigWithBeanWithAliases.class);
		assertSame(factory.getBean("name1"), ConfigWithBeanWithAliases.testBean);
		String[] aliases = factory.getAliases("name1");
		for(String alias : aliases)
			assertSame(factory.getBean(alias), ConfigWithBeanWithAliases.testBean);

		// method name should not be registered
		try {
			factory.getBean("methodName");
			fail("bean should not have been registered with 'methodName'");
		} catch (NoSuchBeanDefinitionException ex) { /* expected */ }
	}

	@Test(expected=BeanDefinitionParsingException.class)
	public void testFinalBeanMethod() {
		initBeanFactory(ConfigWithFinalBean.class);
	}

	@Test
	public void simplestPossibleConfiguration() {
		BeanFactory factory = initBeanFactory(SimplestPossibleConfig.class);
		String stringBean = factory.getBean("stringBean", String.class);
		assertEquals(stringBean, "foo");
	}

	@Test
	public void configurationWithPrototypeScopedBeans() {
		BeanFactory factory = initBeanFactory(ConfigWithPrototypeBean.class);

		TestBean foo = factory.getBean("foo", TestBean.class);
		ITestBean bar = factory.getBean("bar", ITestBean.class);
		ITestBean baz = factory.getBean("baz", ITestBean.class);

		assertSame(foo.getSpouse(), bar);
		assertNotSame(bar.getSpouse(), baz);
	}

	@Test
	public void configurationWithPostProcessor() {
		AnnotationConfigApplicationContext factory = new AnnotationConfigApplicationContext();
		factory.register(ConfigWithPostProcessor.class);
		RootBeanDefinition placeholderConfigurer = new RootBeanDefinition(PropertyPlaceholderConfigurer.class);
		placeholderConfigurer.getPropertyValues().add("properties", "myProp=myValue");
		factory.registerBeanDefinition("placeholderConfigurer", placeholderConfigurer);
		factory.refresh();

		TestBean foo = factory.getBean("foo", TestBean.class);
		ITestBean bar = factory.getBean("bar", ITestBean.class);
		ITestBean baz = factory.getBean("baz", ITestBean.class);

		assertEquals("foo-processed-myValue", foo.getName());
		assertEquals("bar-processed-myValue", bar.getName());
		assertEquals("baz-processed-myValue", baz.getName());

		SpousyTestBean listener = factory.getBean("listenerTestBean", SpousyTestBean.class);
		assertTrue(listener.refreshed);
	}


	@Configuration
	static class ConfigWithBeanWithCustomName {
		static TestBean testBean = new TestBean();
		@Bean(name="customName")
		public TestBean methodName() {
			return testBean;
		}
	}


	@Configuration
	static class ConfigWithFinalBean {
		public final @Bean TestBean testBean() {
			return new TestBean();
		}
	}


	@Configuration
	static class SimplestPossibleConfig {
		public @Bean String stringBean() {
			return "foo";
		}
	}


	@Configuration
	static class ConfigWithBeanWithAliases {

		static TestBean testBean = new TestBean();

		@Bean(name={"name1", "alias1", "alias2", "alias3"})
		public TestBean methodName() {
			return testBean;
		}
	}


	@Configuration
	static class ConfigWithPrototypeBean {

		public @Bean TestBean foo() {
			TestBean foo = new SpousyTestBean("foo");
			foo.setSpouse(bar());
			return foo;
		}

		public @Bean TestBean bar() {
			TestBean bar = new SpousyTestBean("bar");
			bar.setSpouse(baz());
			return bar;
		}

		@Bean @Scope("prototype")
		public TestBean baz() {
			return new TestBean("baz");
		}
	}


	static class ConfigWithPostProcessor extends ConfigWithPrototypeBean {

		@Value("${myProp}")
		private String myProp;

		@Bean
		public POBPP beanPostProcessor() {
			return new POBPP() {
				String nameSuffix = "-processed-" + myProp;
				public void setNameSuffix(String nameSuffix) {
					this.nameSuffix = nameSuffix;
				}
				public Object postProcessBeforeInitialization(Object bean, String beanName) {
					if (bean instanceof ITestBean) {
						((ITestBean) bean).setName(((ITestBean) bean).getName() + nameSuffix);
					}
					return bean;
				}
				public Object postProcessAfterInitialization(Object bean, String beanName) {
					return bean;
				}
				public int getOrder() {
					return 0;
				}
			};
		}

		//@Bean
		public BeanFactoryPostProcessor beanFactoryPostProcessor() {
			return new BeanFactoryPostProcessor() {
				public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
					BeanDefinition bd = beanFactory.getBeanDefinition("beanPostProcessor");
					bd.getPropertyValues().addPropertyValue("nameSuffix", "-processed-" + myProp);
				}
			};
		}

		@Bean
		public ITestBean listenerTestBean() {
			return new SpousyTestBean("listener");
		}
	}


	public interface POBPP extends BeanPostProcessor {
	}


	private static class SpousyTestBean extends TestBean implements ApplicationListener<ContextRefreshedEvent> {

		public boolean refreshed = false;

		public SpousyTestBean(String name) {
			super(name);
		}

		@Override
		@Required
		public void setSpouse(ITestBean spouse) {
			super.setSpouse(spouse);
		}

		public void onApplicationEvent(ContextRefreshedEvent event) {
			this.refreshed = true;
		}
	}

}
