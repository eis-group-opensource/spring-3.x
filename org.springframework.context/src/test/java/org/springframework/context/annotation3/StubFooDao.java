/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation3;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import example.scannable.FooDao;

/**
 * @author Mark Fisher
 */
@Repository
@Qualifier("testing")
public class StubFooDao implements FooDao {

	public String findFoo(int id) {
		return "bar";
	}

}
