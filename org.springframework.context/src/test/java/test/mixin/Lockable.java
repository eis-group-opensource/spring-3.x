/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package test.mixin;


/**
 * Simple interface to use for mixins
 * 
 * @author Rod Johnson
 *
 */
public interface Lockable {
	
	void lock();
	
	void unlock();
	
	boolean locked();
}