/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.access;

import org.springframework.beans.BeansException;

/**
 * Defines a contract for the lookup, use, and release of a
 * {@link org.springframework.beans.factory.BeanFactory},
 * or a <code>BeanFactory</code> subclass such as an
 * {@link org.springframework.context.ApplicationContext}.
 *
 * <p>Where this interface is implemented as a singleton class such as
 * {@link SingletonBeanFactoryLocator}, the Spring team <strong>strongly</strong>
 * suggests that it be used sparingly and with caution. By far the vast majority
 * of the code inside an application is best written in a Dependency Injection
 * style, where that code is served out of a
 * <code>BeanFactory</code>/<code>ApplicationContext</code> container, and has
 * its own dependencies supplied by the container when it is created. However,
 * even such a singleton implementation sometimes has its use in the small glue
 * layers of code that is sometimes needed to tie other code together. For
 * example, third party code may try to construct new objects directly, without
 * the ability to force it to get these objects out of a <code>BeanFactory</code>.
 * If the object constructed by the third party code is just a small stub or
 * proxy, which then uses an implementation of this class to get a
 * <code>BeanFactory</code> from which it gets the real object, to which it
 * delegates, then proper Dependency Injection has been achieved.
 *
 * <p>As another example, in a complex J2EE app with multiple layers, with each
 * layer having its own <code>ApplicationContext</code> definition (in a
 * hierarchy), a class like <code>SingletonBeanFactoryLocator</code> may be used
 * to demand load these contexts.
 *
 * @author Colin Sampaleanu
 * @see org.springframework.beans.factory.BeanFactory
 * @see org.springframework.context.access.DefaultLocatorFactory
 * @see org.springframework.context.ApplicationContext
 */
public interface BeanFactoryLocator {

	/**
	 * Use the {@link org.springframework.beans.factory.BeanFactory} (or derived
	 * interface such as {@link org.springframework.context.ApplicationContext})
	 * specified by the <code>factoryKey</code> parameter.
	 * <p>The definition is possibly loaded/created as needed.
	 * @param factoryKey a resource name specifying which <code>BeanFactory</code> the
	 * <code>BeanFactoryLocator</code> must return for usage. The actual meaning of the
	 * resource name is specific to the implementation of <code>BeanFactoryLocator</code>.
	 * @return the <code>BeanFactory</code> instance, wrapped as a {@link BeanFactoryReference} object
	 * @throws BeansException if there is an error loading or accessing the <code>BeanFactory</code>
	 */
	BeanFactoryReference useBeanFactory(String factoryKey) throws BeansException;

}
