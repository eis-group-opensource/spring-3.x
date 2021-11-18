/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.aspectj;

import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.TypePatternMatcher;

import org.springframework.aop.ClassFilter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Spring AOP {@link ClassFilter} implementation using AspectJ type matching.
 *
 * @author Rod Johnson
 * @since 2.0
 */
public class TypePatternClassFilter implements ClassFilter {

	private String typePattern;

	private TypePatternMatcher aspectJTypePatternMatcher;


	/**
	 * Creates a new instance of the {@link TypePatternClassFilter} class.
	 * <p>This is the JavaBean constructor; be sure to set the
	 * {@link #setTypePattern(String) typePattern} property, else a
	 * no doubt fatal {@link IllegalStateException} will be thrown
	 * when the {@link #matches(Class)} method is first invoked.
	 */
	public TypePatternClassFilter() {
	}

	/**
	 * Create a fully configured {@link TypePatternClassFilter} using the  
	 * given type pattern.
	 * @param typePattern the type pattern that AspectJ weaver should parse
	 * @throws IllegalArgumentException if the supplied <code>typePattern</code> is <code>null</code>
	 * or is recognized as invalid 
	 */
	public TypePatternClassFilter(String typePattern) {
		setTypePattern(typePattern);
	}


	/**
	 * Set the AspectJ type pattern to match.
	 * <p>Examples include:
	 * <code class="code">
	 * org.springframework.beans.*
	 * </code>
	 * This will match any class or interface in the given package.
	 * <code class="code">
	 * org.springframework.beans.ITestBean+
	 * </code>
	 * This will match the <code>ITestBean</code> interface and any class
	 * that implements it.
	 * <p>These conventions are established by AspectJ, not Spring AOP.
	 * @param typePattern the type pattern that AspectJ weaver should parse
	 * @throws IllegalArgumentException if the supplied <code>typePattern</code> is <code>null</code>
	 * or is recognized as invalid 
	 */
	public void setTypePattern(String typePattern) {
		Assert.notNull(typePattern);
		this.typePattern = typePattern;
		this.aspectJTypePatternMatcher =
				PointcutParser.getPointcutParserSupportingAllPrimitivesAndUsingContextClassloaderForResolution().
				parseTypePattern(replaceBooleanOperators(typePattern));
	}

	public String getTypePattern() {
		return typePattern;
	}

	/**
	 * Should the pointcut apply to the given interface or target class?
	 * @param clazz candidate target class
	 * @return whether the advice should apply to this candidate target class
	 * @throws IllegalStateException if no {@link #setTypePattern(String)} has been set
	 */
	public boolean matches(Class clazz) {
		if (this.aspectJTypePatternMatcher == null) {
			throw new IllegalStateException("No 'typePattern' has been set via ctor/setter.");
		}
		return this.aspectJTypePatternMatcher.matches(clazz);
	}

	/**
	 * If a type pattern has been specified in XML, the user cannot
	 * write <code>and</code> as "&&" (though &amp;&amp; will work).
	 * We also allow <code>and</code> between two sub-expressions.
	 * <p>This method converts back to <code>&&</code> for the AspectJ pointcut parser.
	 */
	private String replaceBooleanOperators(String pcExpr) {
		pcExpr = StringUtils.replace(pcExpr," and "," && ");
		pcExpr = StringUtils.replace(pcExpr, " or ", " || ");
		pcExpr = StringUtils.replace(pcExpr, " not ", " ! ");
		return pcExpr;
	}
}
