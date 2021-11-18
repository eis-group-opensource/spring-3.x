/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export.assembler;

/**
 * Extends the <code>MBeanInfoAssembler</code> to add autodetection logic.
 * Implementations of this interface are given the opportunity by the
 * <code>MBeanExporter</code> to include additional beans in the registration process.
 *
 * <p>The exact mechanism for deciding which beans to include is left to
 * implementing classes.
 *
 * @author Rob Harrop
 * @since 1.2
 * @see org.springframework.jmx.export.MBeanExporter
 */
public interface AutodetectCapableMBeanInfoAssembler extends MBeanInfoAssembler {

	/**
	 * Indicate whether a particular bean should be included in the registration
	 * process, if it is not specified in the <code>beans</code> map of the
	 * <code>MBeanExporter</code>.
	 * @param beanClass the class of the bean (might be a proxy class)
	 * @param beanName the name of the bean in the bean factory
	 */
	boolean includeBean(Class<?> beanClass, String beanName);

}
