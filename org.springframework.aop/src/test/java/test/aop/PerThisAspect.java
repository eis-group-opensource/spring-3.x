/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package test.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect("perthis(execution(* getAge()))")
public class PerThisAspect {

	private int invocations = 0;

	public int getInvocations() {
		return this.invocations;
	}

	@Around("execution(* getAge())")
	public int changeAge(ProceedingJoinPoint pjp) throws Throwable {
		return invocations++;
	}

}
