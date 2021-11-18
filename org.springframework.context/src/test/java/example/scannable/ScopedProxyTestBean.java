/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package example.scannable;

import java.util.concurrent.Future;

import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.AsyncResult;

/**
 * @author Mark Fisher
 * @author Juergen Hoeller
 */
@Scope("myScope")
public class ScopedProxyTestBean implements FooService {

	public String foo(int id) {
		return "bar";
	}

	public Future<String> asyncFoo(int id) {
		return new AsyncResult<String>("bar");
	}

	public boolean isInitCalled() {
		return false;
	}

}
