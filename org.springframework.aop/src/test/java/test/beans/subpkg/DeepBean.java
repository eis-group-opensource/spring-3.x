/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package test.beans.subpkg;

import org.springframework.aop.aspectj.AspectJExpressionPointcutTests;

/**
 * Used for testing pointcut matching.
 * 
 * @see AspectJExpressionPointcutTests#testWithinRootAndSubpackages()
 * 
 * @author Chris Beams
 */
public class DeepBean {
	public void aMethod(String foo) {
		// no-op
	}
}
