/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.support;

import java.io.IOException;
import java.io.ObjectInputStream;

import org.aopalliance.aop.Advice;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.util.Assert;

/**
 * Abstract BeanFactory-based PointcutAdvisor that allows for any Advice
 * to be configured as reference to an Advice bean in a BeanFactory.
 *
 * <p>Specifying the name of an advice bean instead of the advice object itself
 * (if running within a BeanFactory) increases loose coupling at initialization time,
 * in order to not initialize the advice object until the pointcut actually matches.
 *
 * @author Juergen Hoeller
 * @since 2.0.2
 * @see #setAdviceBeanName
 * @see DefaultBeanFactoryPointcutAdvisor
 */
public abstract class AbstractBeanFactoryPointcutAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

	private String adviceBeanName;

	private BeanFactory beanFactory;

	private transient Advice advice;

	private transient volatile Object adviceMonitor = new Object();


	/**
	 * Specify the name of the advice bean that this advisor should refer to.
	 * <p>An instance of the specified bean will be obtained on first access
	 * of this advisor's advice. This advisor will only ever obtain at most one
	 * single instance of the advice bean, caching the instance for the lifetime
	 * of the advisor.
	 * @see #getAdvice()
	 */
	public void setAdviceBeanName(String adviceBeanName) {
		this.adviceBeanName = adviceBeanName;
	}

	/**
	 * Return the name of the advice bean that this advisor refers to, if any.
	 */
	public String getAdviceBeanName() {
		return this.adviceBeanName;
	}

	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public void setAdvice(Advice advice) {
		synchronized (this.adviceMonitor) {
			this.advice = advice;
		}
	}

	public Advice getAdvice() {
		synchronized (this.adviceMonitor) {
			if (this.advice == null && this.adviceBeanName != null) {
				Assert.state(this.beanFactory != null, "BeanFactory must be set to resolve 'adviceBeanName'");
				this.advice = this.beanFactory.getBean(this.adviceBeanName, Advice.class);
			}
			return this.advice;
		}
	}

	@Override
	public String toString() {
		return getClass().getName() + ": advice bean '" + getAdviceBeanName() + "'";
	}


	//---------------------------------------------------------------------
	// Serialization support
	//---------------------------------------------------------------------

	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		// Rely on default serialization, just initialize state after deserialization.
		ois.defaultReadObject();

		// Initialize transient fields.
		this.adviceMonitor = new Object();
	}

}
