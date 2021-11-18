/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package example.scannable;

import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;

/**
 * @author Mark Fisher
 * @author Juergen Hoeller
 */
public interface FooService {

	String foo(int id);

	@Async
	Future<String> asyncFoo(int id);

	boolean isInitCalled();

}
