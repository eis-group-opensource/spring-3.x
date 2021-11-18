/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.wiring;

import junit.framework.TestCase;
import org.easymock.MockControl;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import test.beans.TestBean;

/**
 * @author Rick Evans
 * @author Juergen Hoeller
 */
public class BeanConfigurerSupportTests extends TestCase {

	public void testSupplyIncompatibleBeanFactoryImplementation() throws Exception {
		MockControl mock = MockControl.createControl(BeanFactory.class);
		mock.replay();
		try {
			new StubBeanConfigurerSupport().setBeanFactory((BeanFactory) mock.getMock());
			fail("Must have thrown an IllegalArgumentException by this point (incompatible BeanFactory implementation supplied)");
		}
		catch (IllegalArgumentException expected) {
		}
		mock.verify();
	}

	public void testConfigureBeanDoesNothingIfBeanWiringInfoResolverResolvesToNull() throws Exception {
		TestBean beanInstance = new TestBean();

		MockControl mock = MockControl.createControl(BeanWiringInfoResolver.class);
		BeanWiringInfoResolver resolver = (BeanWiringInfoResolver) mock.getMock();
		resolver.resolveWiringInfo(beanInstance);
		mock.setReturnValue(null);
		mock.replay();

		BeanConfigurerSupport configurer = new StubBeanConfigurerSupport();
		configurer.setBeanWiringInfoResolver(resolver);
		configurer.setBeanFactory(new DefaultListableBeanFactory());
		configurer.configureBean(beanInstance);
		mock.verify();
		assertNull(beanInstance.getName());
	}

	public void testConfigureBeanDoesNothingIfNoBeanFactoryHasBeenSet() throws Exception {
		TestBean beanInstance = new TestBean();
		BeanConfigurerSupport configurer = new StubBeanConfigurerSupport();
		configurer.configureBean(beanInstance);
		assertNull(beanInstance.getName());
	}

	public void testConfigureBeanReallyDoesDefaultToUsingTheFullyQualifiedClassNameOfTheSuppliedBeanInstance() throws Exception {
		TestBean beanInstance = new TestBean();
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(TestBean.class);
		builder.addPropertyValue("name", "Harriet Wheeler");

		DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
		factory.registerBeanDefinition(beanInstance.getClass().getName(), builder.getBeanDefinition());

		BeanConfigurerSupport configurer = new StubBeanConfigurerSupport();
		configurer.setBeanFactory(factory);
		configurer.afterPropertiesSet();
		configurer.configureBean(beanInstance);
		assertEquals("Bean is evidently not being configured (for some reason)", "Harriet Wheeler", beanInstance.getName());
	}

	public void testConfigureBeanPerformsAutowiringByNameIfAppropriateBeanWiringInfoResolverIsPluggedIn() throws Exception {
		TestBean beanInstance = new TestBean();
		// spouse for autowiring by name...
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(TestBean.class);
		builder.addConstructorArgValue("David Gavurin");

		DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
		factory.registerBeanDefinition("spouse", builder.getBeanDefinition());

		MockControl mock = MockControl.createControl(BeanWiringInfoResolver.class);
		BeanWiringInfoResolver resolver = (BeanWiringInfoResolver) mock.getMock();
		resolver.resolveWiringInfo(beanInstance);
		mock.setReturnValue(new BeanWiringInfo(BeanWiringInfo.AUTOWIRE_BY_NAME, false));
		mock.replay();

		BeanConfigurerSupport configurer = new StubBeanConfigurerSupport();
		configurer.setBeanFactory(factory);
		configurer.setBeanWiringInfoResolver(resolver);
		configurer.configureBean(beanInstance);
		assertEquals("Bean is evidently not being configured (for some reason)", "David Gavurin", beanInstance.getSpouse().getName());
		
		mock.verify();
	}

	public void testConfigureBeanPerformsAutowiringByTypeIfAppropriateBeanWiringInfoResolverIsPluggedIn() throws Exception {
		TestBean beanInstance = new TestBean();
		// spouse for autowiring by type...
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(TestBean.class);
		builder.addConstructorArgValue("David Gavurin");

		DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
		factory.registerBeanDefinition("Mmm, I fancy a salad!", builder.getBeanDefinition());

		MockControl mock = MockControl.createControl(BeanWiringInfoResolver.class);
		BeanWiringInfoResolver resolver = (BeanWiringInfoResolver) mock.getMock();
		resolver.resolveWiringInfo(beanInstance);
		mock.setReturnValue(new BeanWiringInfo(BeanWiringInfo.AUTOWIRE_BY_TYPE, false));
		mock.replay();

		BeanConfigurerSupport configurer = new StubBeanConfigurerSupport();
		configurer.setBeanFactory(factory);
		configurer.setBeanWiringInfoResolver(resolver);
		configurer.configureBean(beanInstance);
		assertEquals("Bean is evidently not being configured (for some reason)", "David Gavurin", beanInstance.getSpouse().getName());
		
		mock.verify();
	}


	private static class StubBeanConfigurerSupport extends BeanConfigurerSupport {
	}

}
