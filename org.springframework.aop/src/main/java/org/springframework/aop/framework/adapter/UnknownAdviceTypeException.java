/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.framework.adapter;

/**
 * Exception thrown when an attempt is made to use an unsupported
 * Advisor or Advice type.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @see org.aopalliance.aop.Advice
 * @see org.springframework.aop.Advisor
 */
public class UnknownAdviceTypeException extends IllegalArgumentException {

	/**
	 * Create a new UnknownAdviceTypeException for the given advice object.
	 * Will create a message text that says that the object is neither a
	 * subinterface of Advice nor an Advisor.
	 * @param advice the advice object of unknown type
	 */
	public UnknownAdviceTypeException(Object advice) {
		super("Advice object [" + advice + "] is neither a supported subinterface of " +
				"[org.aopalliance.aop.Advice] nor an [org.springframework.aop.Advisor]");
	}

	/**
	 * Create a new UnknownAdviceTypeException with the given message.
	 * @param message the message text
	 */
	public UnknownAdviceTypeException(String message) {
		super(message);
	}

}
