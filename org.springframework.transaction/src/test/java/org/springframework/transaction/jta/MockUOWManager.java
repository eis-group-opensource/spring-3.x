/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.transaction.jta;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.transaction.Synchronization;

import com.ibm.wsspi.uow.UOWAction;
import com.ibm.wsspi.uow.UOWActionException;
import com.ibm.wsspi.uow.UOWException;
import com.ibm.wsspi.uow.UOWManager;

/**
 * @author Juergen Hoeller
 */
public class MockUOWManager implements UOWManager {

	private int type = UOW_TYPE_GLOBAL_TRANSACTION;

	private boolean joined;

	private int timeout;

	private boolean rollbackOnly;

	private int status = UOW_STATUS_NONE;

	private final Map resources = new HashMap();

	private final List synchronizations = new LinkedList();


	public void runUnderUOW(int type, boolean join, UOWAction action) throws UOWActionException, UOWException {
		this.type = type;
		this.joined = join;
		try {
			this.status = UOW_STATUS_ACTIVE;
			action.run();
			this.status = (this.rollbackOnly ? UOW_STATUS_ROLLEDBACK : UOW_STATUS_COMMITTED);
		}
		catch (Error err) {
			this.status = UOW_STATUS_ROLLEDBACK;
			throw err;
		}
		catch (RuntimeException ex) {
			this.status = UOW_STATUS_ROLLEDBACK;
			throw ex;
		}
		catch (Exception ex) {
			this.status = UOW_STATUS_ROLLEDBACK;
			throw new UOWActionException(ex);
		}
	}

	public int getUOWType() {
		return this.type;
	}

	public boolean getJoined() {
		return this.joined;
	}

	public long getLocalUOWId() {
		return 0;
	}

	public void setUOWTimeout(int uowType, int timeout) {
		this.timeout = timeout;
	}

	public int getUOWTimeout() {
		return this.timeout;
	}

	public void setRollbackOnly() {
		this.rollbackOnly = true;
	}

	public boolean getRollbackOnly() {
		return this.rollbackOnly;
	}

	public void setUOWStatus(int status) {
		this.status = status;
	}

	public int getUOWStatus() {
		return this.status;
	}

	public void putResource(Object key, Object value) {
		this.resources.put(key, value);
	}

	public Object getResource(Object key) throws NullPointerException {
		return this.resources.get(key);
	}

	public void registerInterposedSynchronization(Synchronization sync) {
		this.synchronizations.add(sync);
	}

	public List getSynchronizations() {
		return this.synchronizations;
	}

}
