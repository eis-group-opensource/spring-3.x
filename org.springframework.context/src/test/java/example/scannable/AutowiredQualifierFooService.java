/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package example.scannable;

import java.util.concurrent.Future;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.AsyncResult;

/**
 * @author Mark Fisher
 * @author Juergen Hoeller
 */
@Lazy
public class AutowiredQualifierFooService implements FooService {

	@Autowired
	@Qualifier("testing")
	private FooDao fooDao;

	private boolean initCalled = false;

	@PostConstruct
	private void init() {
		if (this.initCalled) {
			throw new IllegalStateException("Init already called");
		}
		this.initCalled = true;
	}

	public String foo(int id) {
		return this.fooDao.findFoo(id);
	}

	public Future<String> asyncFoo(int id) {
		return new AsyncResult<String>(this.fooDao.findFoo(id));
	}

	public boolean isInitCalled() {
		return this.initCalled;
	}

}
