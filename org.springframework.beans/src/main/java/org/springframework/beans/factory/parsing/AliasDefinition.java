/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.parsing;

import org.springframework.beans.BeanMetadataElement;
import org.springframework.util.Assert;

/**
 * Representation of an alias that has been registered during the parsing process.
 *
 * @author Juergen Hoeller
 * @since 2.0
 * @see ReaderEventListener#aliasRegistered(AliasDefinition)
 */
public class AliasDefinition implements BeanMetadataElement {

	private final String beanName;

	private final String alias;

	private final Object source;


	/**
	 * Create a new AliasDefinition.
	 * @param beanName the canonical name of the bean
	 * @param alias the alias registered for the bean
	 */
	public AliasDefinition(String beanName, String alias) {
		this(beanName, alias, null);
	}

	/**
	 * Create a new AliasDefinition.
	 * @param beanName the canonical name of the bean
	 * @param alias the alias registered for the bean
	 * @param source the source object (may be <code>null</code>)
	 */
	public AliasDefinition(String beanName, String alias, Object source) {
		Assert.notNull(beanName, "Bean name must not be null");
		Assert.notNull(alias, "Alias must not be null");
		this.beanName = beanName;
		this.alias = alias;
		this.source = source;
	}


	/**
	 * Return the canonical name of the bean.
	 */
	public final String getBeanName() {
		return this.beanName;
	}

	/**
	 * Return the alias registered for the bean.
	 */
	public final String getAlias() {
		return this.alias;
	}

	public final Object getSource() {
		return this.source;
	}

}
