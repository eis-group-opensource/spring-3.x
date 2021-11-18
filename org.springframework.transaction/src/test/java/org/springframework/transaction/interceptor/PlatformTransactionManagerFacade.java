/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.transaction.interceptor;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

/**
 * Used for testing only (for example, when we must replace the
 * behavior of a PlatformTransactionManager bean we don't have access to).
 *
 * <p>Allows behavior of an entire class to change with static delegate change.
 * Not multi-threaded.
 *
 * @author Rod Johnson
 * @since 26.04.2003
 */
public class PlatformTransactionManagerFacade implements PlatformTransactionManager {
	
	/**
	 * This member can be changed to change behavior class-wide.
	 */
	public static PlatformTransactionManager delegate;

	public TransactionStatus getTransaction(TransactionDefinition definition) {
		return delegate.getTransaction(definition);
	}

	public void commit(TransactionStatus status) {
		delegate.commit(status);
	}

	public void rollback(TransactionStatus status) {
		delegate.rollback(status);
	}

}
