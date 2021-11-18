/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.transaction;

/**
 * Exception that gets thrown when an invalid isolation level is specified,
 * i.e. an isolation level that the transaction manager implementation
 * doesn't support.
 *
 * @author Juergen Hoeller
 * @since 12.05.2003
 */
public class InvalidIsolationLevelException extends TransactionUsageException {

	/**
	 * Constructor for InvalidIsolationLevelException.
	 * @param msg the detail message
	 */
	public InvalidIsolationLevelException(String msg) {
		super(msg);
	}

}
