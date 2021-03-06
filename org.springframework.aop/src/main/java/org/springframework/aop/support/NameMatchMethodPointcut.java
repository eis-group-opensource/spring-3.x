/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.support;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;

import org.springframework.util.ObjectUtils;
import org.springframework.util.PatternMatchUtils;

/**
 * Pointcut bean for simple method name matches, as alternative to regexp patterns.
 * Does not handle overloaded methods: all methods *with a given name will be eligible.
 *
 * @author Juergen Hoeller
 * @author Rod Johnson
 * @author Rob Harrop
 * @since 11.02.2004
 * @see #isMatch
 */
public class NameMatchMethodPointcut extends StaticMethodMatcherPointcut implements Serializable {

	private List<String> mappedNames = new LinkedList<String>();


	/**
	 * Convenience method when we have only a single method name to match.
	 * Use either this method or <code>setMappedNames</code>, not both.
	 * @see #setMappedNames
	 */
	public void setMappedName(String mappedName) {
		setMappedNames(new String[] {mappedName});
	}

	/**
	 * Set the method names defining methods to match.
	 * Matching will be the union of all these; if any match,
	 * the pointcut matches.
	 */
	public void setMappedNames(String[] mappedNames) {
		this.mappedNames = new LinkedList<String>();
		if (mappedNames != null) {
			this.mappedNames.addAll(Arrays.asList(mappedNames));
		}
	}

	/**
	 * Add another eligible method name, in addition to those already named.
	 * Like the set methods, this method is for use when configuring proxies,
	 * before a proxy is used.
	 * <p><b>NB:</b> This method does not work after the proxy is in
	 * use, as advice chains will be cached.
	 * @param name name of the additional method that will match
	 * @return this pointcut to allow for multiple additions in one line
	 */
	public NameMatchMethodPointcut addMethodName(String name) {
		this.mappedNames.add(name);
		return this;
	}


	public boolean matches(Method method, Class targetClass) {
		for (String mappedName : this.mappedNames) {
			if (mappedName.equals(method.getName()) || isMatch(method.getName(), mappedName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Return if the given method name matches the mapped name.
	 * <p>The default implementation checks for "xxx*", "*xxx" and "*xxx*" matches,
	 * as well as direct equality. Can be overridden in subclasses.
	 * @param methodName the method name of the class
	 * @param mappedName the name in the descriptor
	 * @return if the names match
	 * @see org.springframework.util.PatternMatchUtils#simpleMatch(String, String)
	 */
	protected boolean isMatch(String methodName, String mappedName) {
		return PatternMatchUtils.simpleMatch(mappedName, methodName);
	}


	@Override
	public boolean equals(Object other) {
		return (this == other || (other instanceof NameMatchMethodPointcut &&
				ObjectUtils.nullSafeEquals(this.mappedNames, ((NameMatchMethodPointcut) other).mappedNames)));
	}

	@Override
	public int hashCode() {
		return (this.mappedNames != null ? this.mappedNames.hashCode() : 0);
	}

}
