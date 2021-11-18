/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package test.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class TwoAdviceAspect {
	private int totalCalls;

	@Around("execution(* getAge())")
	public int returnCallCount(ProceedingJoinPoint pjp) throws Exception {
		return totalCalls;
	}

	@Before("execution(* setAge(int)) && args(newAge)")
	public void countSet(int newAge) throws Exception {
		++totalCalls;
	}
}