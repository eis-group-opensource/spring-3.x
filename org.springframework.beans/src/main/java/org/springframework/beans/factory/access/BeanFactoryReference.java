/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.access;

import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.BeanFactory;

/**
 * Used to track a reference to a {@link BeanFactory} obtained through
 * a {@link BeanFactoryLocator}.
 *
 * <p>It is safe to call {@link #release()} multiple times, but
 * {@link #getFactory()} must not be called after calling release.
 *
 * @author Colin Sampaleanu
 * @see BeanFactoryLocator
 * @see org.springframework.context.access.ContextBeanFactoryReference
 */
public interface BeanFactoryReference {

	/**
	 * Return the {@link BeanFactory} instance held by this reference.
	 * @throws IllegalStateException if invoked after <code>release()</code> has been called
	 */
	BeanFactory getFactory();

	/**
	 * Indicate that the {@link BeanFactory} instance referred to by this object is not
	 * needed any longer by the client code which obtained the {@link BeanFactoryReference}.
	 * <p>Depending on the actual implementation of {@link BeanFactoryLocator}, and
	 * the actual type of <code>BeanFactory</code>, this may possibly not actually
	 * do anything; alternately in the case of a 'closeable' <code>BeanFactory</code>
	 * or derived class (such as {@link org.springframework.context.ApplicationContext})
	 * may 'close' it, or may 'close' it once no more references remain.
	 * <p>In an EJB usage scenario this would normally be called from
	 * <code>ejbRemove()</code> and <code>ejbPassivate()</code>.
	 * <p>This is safe to call multiple times.
	 * @throws FatalBeanException if the <code>BeanFactory</code> cannot be released
	 * @see BeanFactoryLocator
	 * @see org.springframework.context.access.ContextBeanFactoryReference
	 * @see org.springframework.context.ConfigurableApplicationContext#close()
	 */
	void release() throws FatalBeanException;

}
