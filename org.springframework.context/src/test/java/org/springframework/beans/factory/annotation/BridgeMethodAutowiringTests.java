/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.annotation;

import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

public class BridgeMethodAutowiringTests {

	@Test
	public void SPR_8434() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(UserServiceImpl.class, Foo.class);
		ctx.refresh();
		assertNotNull(ctx.getBean(UserServiceImpl.class).object);
	}

}


abstract class GenericServiceImpl<D extends Object> {

	public abstract void setObject(D object);

}


class UserServiceImpl extends GenericServiceImpl<Foo> {

	protected Foo object;

	@Inject
	@Named("userObject")
	public void setObject(Foo object) {
		this.object = object;
	}
}

@Component("userObject")
class Foo { }

