/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.validation.beanvalidation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

import org.hibernate.validator.method.MethodConstraintViolationException;
import org.junit.Test;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.validation.annotation.Validated;

import static org.junit.Assert.*;

/**
 * @author Juergen Hoeller
 * @since 3.1
 */
public class MethodValidationTests {

	@Test
	public void testMethodValidationInterceptor() {
		MyValidBean bean = new MyValidBean();
		ProxyFactory proxyFactory = new ProxyFactory(bean);
		proxyFactory.addAdvice(new MethodValidationInterceptor());
		doTestProxyValidation((MyValidInterface) proxyFactory.getProxy());
	}

	@Test
	public void testMethodValidationPostProcessor() {
		StaticApplicationContext ac = new StaticApplicationContext();
		ac.registerSingleton("mvpp", MethodValidationPostProcessor.class);
		ac.registerSingleton("bean", MyValidBean.class);
		ac.refresh();
		doTestProxyValidation(ac.getBean("bean", MyValidInterface.class));
	}


	private void doTestProxyValidation(MyValidInterface proxy) {
		assertNotNull(proxy.myValidMethod("value", 5));
		try {
			assertNotNull(proxy.myValidMethod("value", 15));
			fail("Should have thrown MethodConstraintViolationException");
		}
		catch (MethodConstraintViolationException ex) {
			// expected
		}
		try {
			assertNotNull(proxy.myValidMethod(null, 5));
			fail("Should have thrown MethodConstraintViolationException");
		}
		catch (MethodConstraintViolationException ex) {
			// expected
		}
		try {
			assertNotNull(proxy.myValidMethod("value", 0));
			fail("Should have thrown MethodConstraintViolationException");
		}
		catch (MethodConstraintViolationException ex) {
			// expected
		}
	}


	@MyStereotype
	public static class MyValidBean implements MyValidInterface {

		public Object myValidMethod(String arg1, int arg2) {
			return (arg2 == 0 ? null : "value");
		}
	}


	public interface MyValidInterface {

		@NotNull Object myValidMethod(@NotNull(groups = MyGroup.class) String arg1, @Max(10) int arg2);
	}


	public interface MyGroup {
	}


	@Validated({MyGroup.class, Default.class})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface MyStereotype {

	}

}
