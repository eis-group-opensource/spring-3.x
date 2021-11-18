/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.transaction.interceptor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Inherits fallback behavior from AbstractFallbackTransactionAttributeSource.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 */
public class MapTransactionAttributeSource extends AbstractFallbackTransactionAttributeSource {

	private final Map<Object, TransactionAttribute> attributeMap = new HashMap<Object, TransactionAttribute>();


	public void register(Method method, TransactionAttribute txAtt) {
		this.attributeMap.put(method, txAtt);
	}

	public void register(Class clazz, TransactionAttribute txAtt) {
		this.attributeMap.put(clazz, txAtt);
	}


	protected TransactionAttribute findTransactionAttribute(Method method) {
		return this.attributeMap.get(method);
	}

	protected TransactionAttribute findTransactionAttribute(Class clazz) {
		return this.attributeMap.get(clazz);
	}

}
