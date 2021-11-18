/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.transaction;

/**
 * Superclass for exceptions caused by inappropriate usage of 
 * a Spring transaction API.
 *
 * @author Rod Johnson
 * @since 22.03.2003
 */
public class TransactionUsageException extends TransactionException {

	/**
	 * Constructor for TransactionUsageException.
	 * @param msg the detail message
	 */
	public TransactionUsageException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for TransactionUsageException.
	 * @param msg the detail message
	 * @param cause the root cause from the transaction API in use
	 */
	public TransactionUsageException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
