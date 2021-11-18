/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.transaction.interceptor;

import org.springframework.core.NestedRuntimeException;

/**
 * An example {@link RuntimeException} for use in testing rollback rules.
 * 
 * @author Chris Beams
 */
@SuppressWarnings("serial")
class MyRuntimeException extends NestedRuntimeException {
	public MyRuntimeException(String msg) {
		super(msg);
	}
}
