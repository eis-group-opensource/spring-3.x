/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.support;

import java.util.List;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author Scott Andrews
 * @author Juergen Hoeller
 */
public class Spr7283Tests {

    @Test
    public void testListWithInconsistentElementType() {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spr7283.xml", getClass());
		List list = ctx.getBean("list", List.class);
		assertEquals(2, list.size());
		assertTrue(list.get(0) instanceof A);
		assertTrue(list.get(1) instanceof B);
    }


    public static class A {
    	public A() {}
    }

    public static class B {
    	public B() {}
    }
        
}
