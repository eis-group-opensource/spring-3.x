/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.aspectj.autoproxy;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.ITestBean;
import org.springframework.beans.TestBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Rod Johnson
 * @author Rob Harrop
 * @author Chris Beams
 */
public final class AspectJAutoProxyCreatorAndLazyInitTargetSourceTests {

	@Test
	public void testAdrian() {
		ClassPathXmlApplicationContext ctx =
			new ClassPathXmlApplicationContext(getClass().getSimpleName() + "-context.xml", getClass());
		
		ITestBean adrian = (ITestBean) ctx.getBean("adrian");
		assertEquals(0, LazyTestBean.instantiations);
		assertNotNull(adrian);
		adrian.getAge();
		assertEquals(68, adrian.getAge());
		assertEquals(1, LazyTestBean.instantiations);
	}

}


class LazyTestBean extends TestBean {

	public static int instantiations;

	public LazyTestBean() {
		++instantiations;
	}

}
