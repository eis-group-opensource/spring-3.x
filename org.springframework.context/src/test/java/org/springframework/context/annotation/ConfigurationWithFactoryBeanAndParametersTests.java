/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;


/**
 * Test case cornering the bug initially raised with SPR-8762, in which a
 * NullPointerException would be raised if a FactoryBean-returning @Bean method also
 * accepts parameters
 *
 * @author Chris Beams
 * @since 3.1
 */
public class ConfigurationWithFactoryBeanAndParametersTests {
	@Test
	public void test() {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class, Bar.class);
		assertNotNull(ctx.getBean(Bar.class).foo);
	}
}

@Configuration
class Config {
	@Bean
	public FactoryBean<Foo> fb(@Value("42") String answer) {
		return new FooFactoryBean();
	}
}

class Foo {
}

class Bar {
	Foo foo;

	@Autowired
	public Bar(Foo foo) {
		this.foo = foo;
	}
}

class FooFactoryBean implements FactoryBean<Foo> {

	public Foo getObject() {
		return new Foo();
	}

	public Class<Foo> getObjectType() {
		return Foo.class;
	}

	public boolean isSingleton() {
		return true;
	}
}
