/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export.assembler;

import java.lang.reflect.Method;

/**
 * Simple subclass of <code>AbstractReflectiveMBeanInfoAssembler</code>
 * that always votes yes for method and property inclusion, effectively exposing
 * all public methods and properties as operations and attributes.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 1.2
 */
public class SimpleReflectiveMBeanInfoAssembler extends AbstractConfigurableMBeanInfoAssembler {

	/**
	 * Always returns <code>true</code>.
	 */
	@Override
	protected boolean includeReadAttribute(Method method, String beanKey) {
		return true;
	}

	/**
	 * Always returns <code>true</code>.
	 */
	@Override
	protected boolean includeWriteAttribute(Method method, String beanKey) {
		return true;
	}

  /**
	 * Always returns <code>true</code>.
	 */
	@Override
	protected boolean includeOperation(Method method, String beanKey) {
		return true;
	}

}
