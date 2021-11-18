/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.serviceloader;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.ServiceLoader;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.JdkVersion;

/**
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public class ServiceLoaderTests {

	@Test
	public void testServiceLoaderFactoryBean() {
		if (JdkVersion.getMajorJavaVersion() < JdkVersion.JAVA_16 ||
				!ServiceLoader.load(DocumentBuilderFactory.class).iterator().hasNext()){
			return;
		}

		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		RootBeanDefinition bd = new RootBeanDefinition(ServiceLoaderFactoryBean.class);
		bd.getPropertyValues().add("serviceType", DocumentBuilderFactory.class.getName());
		bf.registerBeanDefinition("service", bd);
		ServiceLoader<?> serviceLoader = (ServiceLoader<?>) bf.getBean("service");
		assertTrue(serviceLoader.iterator().next() instanceof DocumentBuilderFactory);
	}

	@Test
	public void testServiceFactoryBean() {
		if (JdkVersion.getMajorJavaVersion() < JdkVersion.JAVA_16 ||
				!ServiceLoader.load(DocumentBuilderFactory.class).iterator().hasNext()){
			return;
		}

		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		RootBeanDefinition bd = new RootBeanDefinition(ServiceFactoryBean.class);
		bd.getPropertyValues().add("serviceType", DocumentBuilderFactory.class.getName());
		bf.registerBeanDefinition("service", bd);
		assertTrue(bf.getBean("service") instanceof DocumentBuilderFactory);
	}

	@Test
	public void testServiceListFactoryBean() {
		if (JdkVersion.getMajorJavaVersion() < JdkVersion.JAVA_16 ||
				!ServiceLoader.load(DocumentBuilderFactory.class).iterator().hasNext()){
			return;
		}

		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		RootBeanDefinition bd = new RootBeanDefinition(ServiceListFactoryBean.class);
		bd.getPropertyValues().add("serviceType", DocumentBuilderFactory.class.getName());
		bf.registerBeanDefinition("service", bd);
		List<?> serviceList = (List<?>) bf.getBean("service");
		assertTrue(serviceList.get(0) instanceof DocumentBuilderFactory);
	}

}
