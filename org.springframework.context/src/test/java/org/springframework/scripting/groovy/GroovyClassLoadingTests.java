/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scripting.groovy;

import java.lang.reflect.Method;

import groovy.lang.GroovyClassLoader;
import junit.framework.TestCase;

import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.util.ReflectionUtils;

/**
 * @author Mark Fisher
 */
public class GroovyClassLoadingTests extends TestCase {

	public void testClassLoading() throws Exception {
		StaticApplicationContext context = new StaticApplicationContext();

		GroovyClassLoader gcl = new GroovyClassLoader();
		Class class1 = gcl.parseClass("class TestBean { def myMethod() { \"foo\" } }");
		Class class2 = gcl.parseClass("class TestBean { def myMethod() { \"bar\" } }");

		context.registerBeanDefinition("testBean", new RootBeanDefinition(class1));
		Object testBean1 = context.getBean("testBean");
		Method method1 = class1.getDeclaredMethod("myMethod", new Class[0]);
		Object result1 = ReflectionUtils.invokeMethod(method1, testBean1);
		assertEquals("foo", (String) result1);

		// ### uncommenting the next line causes the test to pass for Spring > 2.0.2 ###
		//context.removeBeanDefinition("testBean");

		context.registerBeanDefinition("testBean", new RootBeanDefinition(class2));
		Object testBean2 = context.getBean("testBean");
		Method method2 = class2.getDeclaredMethod("myMethod", new Class[0]);
		Object result2 = ReflectionUtils.invokeMethod(method2, testBean2);
		assertEquals("bar", (String) result2);
	}

}
