/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.transaction.interceptor;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.springframework.util.ObjectUtils;

/**
 * Very simple implementation of TransactionAttributeSource which will always return
 * the same TransactionAttribute for all methods fed to it. The TransactionAttribute
 * may be specified, but will otherwise default to PROPAGATION_REQUIRED. This may be
 * used in the cases where you want to use the same transaction attribute with all
 * methods being handled by a transaction interceptor.
 *
 * @author Colin Sampaleanu
 * @since 15.10.2003
 * @see org.springframework.transaction.interceptor.TransactionProxyFactoryBean
 * @see org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator
 */
public class MatchAlwaysTransactionAttributeSource implements TransactionAttributeSource, Serializable {
  
	private TransactionAttribute transactionAttribute = new DefaultTransactionAttribute();


	/**
	 * Allows a transaction attribute to be specified, using the String form, for
	 * example, "PROPAGATION_REQUIRED".
	 * @param transactionAttribute The String form of the transactionAttribute to use.
	 * @see org.springframework.transaction.interceptor.TransactionAttributeEditor
	 */
	public void setTransactionAttribute(TransactionAttribute transactionAttribute) {
		this.transactionAttribute = transactionAttribute;
	}


	public TransactionAttribute getTransactionAttribute(Method method, Class<?> targetClass) {
		return this.transactionAttribute;
	}


	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MatchAlwaysTransactionAttributeSource)) {
			return false;
		}
		MatchAlwaysTransactionAttributeSource otherTas = (MatchAlwaysTransactionAttributeSource) other;
		return ObjectUtils.nullSafeEquals(this.transactionAttribute, otherTas.transactionAttribute);
	}

	@Override
	public int hashCode() {
		return MatchAlwaysTransactionAttributeSource.class.hashCode();
	}

	@Override
	public String toString() {
		return getClass().getName() + ": " + this.transactionAttribute;
	}

}
