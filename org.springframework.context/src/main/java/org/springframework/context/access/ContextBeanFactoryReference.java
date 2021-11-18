/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.access;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * ApplicationContext-specific implementation of BeanFactoryReference,
 * wrapping a newly created ApplicationContext, closing it on release.
 *
 * <p>As per BeanFactoryReference contract, <code>release</code> may be called
 * more than once, with subsequent calls not doing anything. However, calling
 * <code>getFactory</code> after a <code>release</code> call will cause an exception.
 *
 * @author Juergen Hoeller
 * @author Colin Sampaleanu
 * @since 13.02.2004
 * @see org.springframework.context.ConfigurableApplicationContext#close
 */
public class ContextBeanFactoryReference implements BeanFactoryReference {

	private ApplicationContext applicationContext;


	/**
	 * Create a new ContextBeanFactoryReference for the given context.
	 * @param applicationContext the ApplicationContext to wrap
	 */
	public ContextBeanFactoryReference(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}


	public BeanFactory getFactory() {
		if (this.applicationContext == null) {
			throw new IllegalStateException(
					"ApplicationContext owned by this BeanFactoryReference has been released");
		}
		return this.applicationContext;
	}

	public void release() {
		if (this.applicationContext != null) {
			ApplicationContext savedCtx;
			
			// We don't actually guarantee thread-safety, but it's not a lot of extra work.
			synchronized (this) {
				savedCtx = this.applicationContext;
				this.applicationContext = null;
			}

			if (savedCtx != null && savedCtx instanceof ConfigurableApplicationContext) {
				((ConfigurableApplicationContext) savedCtx).close();
			}
		}
	}

}
