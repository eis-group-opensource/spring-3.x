/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.support;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.aopalliance.aop.Advice;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.DynamicIntroductionAdvice;
import org.springframework.aop.IntroductionAdvisor;
import org.springframework.aop.IntroductionInfo;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * Simple {@link org.springframework.aop.IntroductionAdvisor} implementation
 * that by default applies to any class.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 11.11.2003
 */
@SuppressWarnings({ "unchecked", "serial" })
public class DefaultIntroductionAdvisor implements IntroductionAdvisor, ClassFilter, Ordered, Serializable {

	private final Advice advice;
	
	private final Set<Class> interfaces = new HashSet<Class>();

	private int order = Integer.MAX_VALUE;


	/**
	 * Create a DefaultIntroductionAdvisor for the given advice.
	 * @param advice the Advice to apply (may implement the
	 * {@link org.springframework.aop.IntroductionInfo} interface)
	 * @see #addInterface
	 */
	public DefaultIntroductionAdvisor(Advice advice) {
		this(advice, (advice instanceof IntroductionInfo ? (IntroductionInfo) advice : null));
	}

	/**
	 * Create a DefaultIntroductionAdvisor for the given advice.
	 * @param advice the Advice to apply
	 * @param introductionInfo the IntroductionInfo that describes
	 * the interface to introduce (may be <code>null</code>)
	 */
	public DefaultIntroductionAdvisor(Advice advice, IntroductionInfo introductionInfo) {
		Assert.notNull(advice, "Advice must not be null");
		this.advice = advice;
		if (introductionInfo != null) {
			Class[] introducedInterfaces = introductionInfo.getInterfaces();
			if (introducedInterfaces.length == 0) {
				throw new IllegalArgumentException("IntroductionAdviceSupport implements no interfaces");
			}
			for (Class ifc : introducedInterfaces) {
				addInterface(ifc);
			}
		}
	}

	/**
	 * Create a DefaultIntroductionAdvisor for the given advice.
	 * @param advice the Advice to apply
	 * @param intf the interface to introduce
	 */
	public DefaultIntroductionAdvisor(DynamicIntroductionAdvice advice, Class intf) {
		Assert.notNull(advice, "Advice must not be null");
		this.advice = advice;
		addInterface(intf);
	}


	/**
	 * Add the specified interface to the list of interfaces to introduce.
	 * @param intf the interface to introduce
	 */
	public void addInterface(Class intf) {
		Assert.notNull(intf, "Interface must not be null");
		if (!intf.isInterface()) {
			throw new IllegalArgumentException("Specified class [" + intf.getName() + "] must be an interface");
		}
		this.interfaces.add(intf);
	}

	public Class[] getInterfaces() {
		return this.interfaces.toArray(new Class[this.interfaces.size()]);
	}

	public void validateInterfaces() throws IllegalArgumentException {
		for (Class ifc : this.interfaces) {
			if (this.advice instanceof DynamicIntroductionAdvice &&
					!((DynamicIntroductionAdvice) this.advice).implementsInterface(ifc)) {
			 throw new IllegalArgumentException("DynamicIntroductionAdvice [" + this.advice + "] " +
					 "does not implement interface [" + ifc.getName() + "] specified for introduction");
			}
		}
	}


	public void setOrder(int order) {
		this.order = order;
	}

	public int getOrder() {
		return this.order;
	}


	public Advice getAdvice() {
		return this.advice;
	}

	public boolean isPerInstance() {
		return true;
	}

	public ClassFilter getClassFilter() {
		return this;
	}

	public boolean matches(Class clazz) {
		return true;
	}


	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof DefaultIntroductionAdvisor)) {
			return false;
		}
		DefaultIntroductionAdvisor otherAdvisor = (DefaultIntroductionAdvisor) other;
		return (this.advice.equals(otherAdvisor.advice) && this.interfaces.equals(otherAdvisor.interfaces));
	}

	@Override
	public int hashCode() {
		return this.advice.hashCode() * 13 + this.interfaces.hashCode();
	}

	@Override
	public String toString() {
		return ClassUtils.getShortName(getClass()) + ": advice [" + this.advice + "]; interfaces " +
				ClassUtils.classNamesToString(this.interfaces);
	}

}
