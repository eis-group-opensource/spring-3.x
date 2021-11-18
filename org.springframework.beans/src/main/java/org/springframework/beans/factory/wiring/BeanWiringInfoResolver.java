/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.wiring;

/**
 * Strategy interface to be implemented by objects than can resolve bean name
 * information, given a newly instantiated bean object. Invocations to the
 * {@link #resolveWiringInfo} method on this interface will be driven by
 * the AspectJ pointcut in the relevant concrete aspect.
 *
 * <p>Metadata resolution strategy can be pluggable. A good default is
 * {@link ClassNameBeanWiringInfoResolver}, which uses the fully-qualified
 * class name as bean name.
 *
 * @author Rod Johnson
 * @since 2.0
 * @see BeanWiringInfo
 * @see ClassNameBeanWiringInfoResolver
 * @see org.springframework.beans.factory.annotation.AnnotationBeanWiringInfoResolver
 */
public interface BeanWiringInfoResolver {

	/**
	 * Resolve the BeanWiringInfo for the given bean instance.
	 * @param beanInstance the bean instance to resolve info for
	 * @return the BeanWiringInfo, or <code>null</code> if not found
	 */
	BeanWiringInfo resolveWiringInfo(Object beanInstance);

}
