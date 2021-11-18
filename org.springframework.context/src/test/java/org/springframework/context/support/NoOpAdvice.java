/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.support;

import org.springframework.aop.ThrowsAdvice;

/**
 * Advice object that implements <i>multiple</i> Advice interfaces.
 *
 * @author Chris Beams
 */
public class NoOpAdvice implements ThrowsAdvice {

	public void afterThrowing(Exception ex) throws Throwable {
	    // no-op
	}

}
