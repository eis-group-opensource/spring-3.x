/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package example.scannable;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import org.springframework.stereotype.Component;

/**
 * @author Mark Fisher
 */
@Component
@Aspect
public class ServiceInvocationCounter {

	private int useCount;

	private static final ThreadLocal<Integer> threadLocalCount = new ThreadLocal<Integer>();


	@Pointcut("execution(* example.scannable.FooService+.*(..))")
	public void serviceExecution() {}

	@Before("serviceExecution()")
	public void countUse() {
		this.useCount++;
		this.threadLocalCount.set(this.useCount);
		System.out.println("");
	}
	
	public int getCount() {
		return this.useCount;
	}

	public static Integer getThreadLocalCount() {
		return threadLocalCount.get();
	}

}
