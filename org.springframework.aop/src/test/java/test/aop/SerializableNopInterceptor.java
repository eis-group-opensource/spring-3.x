/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package test.aop;

import java.io.Serializable;



/**
 * Subclass of NopInterceptor that is serializable and
 * can be used to test proxy serialization.
 * 
 * @author Rod Johnson
 * @author Chris Beams
 */
@SuppressWarnings("serial")
public class SerializableNopInterceptor extends NopInterceptor implements Serializable {
	
	/**
	 * We must override this field and the related methods as
	 * otherwise count won't be serialized from the non-serializable
	 * NopInterceptor superclass.
	 */
	private int count;
	
	public int getCount() {
		return this.count;
	}
	
	protected void increment() {
		++count;
	}
	
}