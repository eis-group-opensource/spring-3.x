/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scripting;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Twee advice that 'scrambles' the return value
 * of a {@link Messenger} invocation.
 *
 * @author Rick Evans
 */
public final class MessengerScrambler {

	public String scramble(ProceedingJoinPoint pjp) throws Throwable {
		String message = (String) pjp.proceed();
		return new StringBuffer(message).reverse().toString();
	}

}
