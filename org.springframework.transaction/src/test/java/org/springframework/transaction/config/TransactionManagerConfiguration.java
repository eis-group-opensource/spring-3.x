/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.transaction.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.CallCountingTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author Juergen Hoeller
 */
@Configuration
public class TransactionManagerConfiguration {

	@Bean
	@Qualifier("synch")
	public PlatformTransactionManager transactionManager1() {
		return new CallCountingTransactionManager();
	}

	@Bean
	@Qualifier("noSynch")
	public PlatformTransactionManager transactionManager2() {
		CallCountingTransactionManager tm = new CallCountingTransactionManager();
		tm.setTransactionSynchronization(CallCountingTransactionManager.SYNCHRONIZATION_NEVER);
		return tm;
	}

}
