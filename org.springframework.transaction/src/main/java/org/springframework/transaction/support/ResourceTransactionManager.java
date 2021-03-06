/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.transaction.support;

import org.springframework.transaction.PlatformTransactionManager;

/**
 * Extension of the {@link org.springframework.transaction.PlatformTransactionManager}
 * interface, indicating a native resource transaction manager, operating on a single
 * target resource. Such transaction managers differ from JTA transaction managers in
 * that they do not use XA transaction enlistment for an open number of resources but
 * rather focus on leveraging the native power and simplicity of a single target resource.
 *
 * <p>This interface is mainly used for abstract introspection of a transaction manager,
 * giving clients a hint on what kind of transaction manager they have been given
 * and on what concrete resource the transaction manager is operating on.
 *
 * @author Juergen Hoeller
 * @since 2.0.4
 * @see TransactionSynchronizationManager
 */
public interface ResourceTransactionManager extends PlatformTransactionManager {

	/**
	 * Return the resource factory that this transaction manager operates on,
	 * e.g. a JDBC DataSource or a JMS ConnectionFactory.
	 * <p>This target resource factory is usually used as resource key for
	 * {@link TransactionSynchronizationManager}'s resource bindings per thread.
	 * @return the target resource factory (never <code>null</code>)
	 * @see TransactionSynchronizationManager#bindResource
	 * @see TransactionSynchronizationManager#getResource
	 */
	Object getResourceFactory();

}
