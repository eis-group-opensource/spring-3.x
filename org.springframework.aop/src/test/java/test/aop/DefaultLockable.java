/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package test.aop;

/**
 * Simple implementation of Lockable interface for use in mixins.
 * 
 * @author Rod Johnson
 */
public class DefaultLockable implements Lockable {

	private boolean locked;

	public void lock() {
		this.locked = true;
	}

	public void unlock() {
		this.locked = false;
	}

	public boolean locked() {
		return this.locked;
	}

}
