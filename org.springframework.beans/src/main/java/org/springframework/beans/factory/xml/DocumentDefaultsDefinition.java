/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.xml;

import org.springframework.beans.factory.parsing.DefaultsDefinition;

/**
 * Simple JavaBean that holds the defaults specified at the <code>&lt;beans&gt;</code>
 * level in a standard Spring XML bean definition document:
 * <code>default-lazy-init</code>, <code>default-autowire</code>, etc.
 *
 * @author Juergen Hoeller
 * @since 2.0.2
 */
public class DocumentDefaultsDefinition implements DefaultsDefinition {

	private String lazyInit;

	private String merge;

	private String autowire;

	private String dependencyCheck;

	private String autowireCandidates;

	private String initMethod;

	private String destroyMethod;

	private Object source;


	/**
	 * Set the default lazy-init flag for the document that's currently parsed.
	 */
	public void setLazyInit(String lazyInit) {
		this.lazyInit = lazyInit;
	}

	/**
	 * Return the default lazy-init flag for the document that's currently parsed.
	 */
	public String getLazyInit() {
		return this.lazyInit;
	}

	/**
	 * Set the default merge setting for the document that's currently parsed.
	 */
	public void setMerge(String merge) {
		this.merge = merge;
	}

	/**
	 * Return the default merge setting for the document that's currently parsed.
	 */
	public String getMerge() {
		return this.merge;
	}

	/**
	 * Set the default autowire setting for the document that's currently parsed.
	 */
	public void setAutowire(String autowire) {
		this.autowire = autowire;
	}

	/**
	 * Return the default autowire setting for the document that's currently parsed.
	 */
	public String getAutowire() {
		return this.autowire;
	}

	/**
	 * Set the default dependency-check setting for the document that's currently parsed.
	 */
	public void setDependencyCheck(String dependencyCheck) {
		this.dependencyCheck = dependencyCheck;
	}

	/**
	 * Return the default dependency-check setting for the document that's currently parsed.
	 */
	public String getDependencyCheck() {
		return this.dependencyCheck;
	}

	/**
	 * Set the default autowire-candidate pattern for the document that's currently parsed.
	 * Also accepts a comma-separated list of patterns.
	 */
	public void setAutowireCandidates(String autowireCandidates) {
		this.autowireCandidates = autowireCandidates;
	}

	/**
	 * Return the default autowire-candidate pattern for the document that's currently parsed.
	 * May also return a comma-separated list of patterns.
	 */
	public String getAutowireCandidates() {
		return this.autowireCandidates;
	}

	/**
	 * Set the default init-method setting for the document that's currently parsed.
	 */
	public void setInitMethod(String initMethod) {
		this.initMethod = initMethod;
	}

	/**
	 * Return the default init-method setting for the document that's currently parsed.
	 */
	public String getInitMethod() {
		return this.initMethod;
	}

	/**
	 * Set the default destroy-method setting for the document that's currently parsed.
	 */
	public void setDestroyMethod(String destroyMethod) {
		this.destroyMethod = destroyMethod;
	}

	/**
	 * Return the default destroy-method setting for the document that's currently parsed.
	 */
	public String getDestroyMethod() {
		return this.destroyMethod;
	}

	/**
	 * Set the configuration source <code>Object</code> for this metadata element.
	 * <p>The exact type of the object will depend on the configuration mechanism used.
	 */
	public void setSource(Object source) {
		this.source = source;
	}

	public Object getSource() {
		return this.source;
	}

}
