/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.transaction.interceptor;

/**
 * Tag subclass of {@link RollbackRuleAttribute} that has the opposite behavior
 * to the <code>RollbackRuleAttribute</code> superclass.
 *
 * @author Rod Johnson
 * @since 09.04.2003
 */
public class NoRollbackRuleAttribute extends RollbackRuleAttribute {

	/**
	 * Create a new instance of the <code>NoRollbackRuleAttribute</code> class
	 * for the supplied {@link Throwable} class.
	 * @param clazz the <code>Throwable</code> class
	 * @see RollbackRuleAttribute#RollbackRuleAttribute(Class)
	 */
	public NoRollbackRuleAttribute(Class clazz) {
		super(clazz);
	}

	/**
	 * Create a new instance of the <code>NoRollbackRuleAttribute</code> class
	 * for the supplied <code>exceptionName</code>.
	 * @param exceptionName the exception name pattern
	 * @see RollbackRuleAttribute#RollbackRuleAttribute(String)
	 */
	public NoRollbackRuleAttribute(String exceptionName) {
		super(exceptionName);
	}

	@Override
	public String toString() {
		return "No" + super.toString();
	}

}
