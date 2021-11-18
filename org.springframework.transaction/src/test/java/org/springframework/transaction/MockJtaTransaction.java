/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.transaction;

import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.xa.XAResource;

/**
 * @author Juergen Hoeller
 * @since 31.08.2004
 */
public class MockJtaTransaction implements javax.transaction.Transaction {

	private Synchronization synchronization;

	public int getStatus() {
		return Status.STATUS_ACTIVE;
	}

	public void registerSynchronization(Synchronization synchronization) {
		this.synchronization = synchronization;
	}

	public Synchronization getSynchronization() {
		return synchronization;
	}

	public boolean enlistResource(XAResource xaResource) {
		return false;
	}

	public boolean delistResource(XAResource xaResource, int i) {
		return false;
	}

	public void commit() {
	}

	public void rollback() {
	}

	public void setRollbackOnly() {
	}

}
