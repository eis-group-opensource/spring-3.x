/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.parsing;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.BeanReference;

/**
 * ComponentDefinition based on a standard BeanDefinition, exposing the given bean
 * definition as well as inner bean definitions and bean references for the given bean.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 2.0
 */
public class BeanComponentDefinition extends BeanDefinitionHolder implements ComponentDefinition {

	private BeanDefinition[] innerBeanDefinitions;

	private BeanReference[] beanReferences;


	/**
	 * Create a new BeanComponentDefinition for the given bean.
	 * @param beanDefinition the BeanDefinition
	 * @param beanName the name of the bean
	 */
	public BeanComponentDefinition(BeanDefinition beanDefinition, String beanName) {
		super(beanDefinition, beanName);
		findInnerBeanDefinitionsAndBeanReferences(beanDefinition);
	}

	/**
	 * Create a new BeanComponentDefinition for the given bean.
	 * @param beanDefinition the BeanDefinition
	 * @param beanName the name of the bean
	 * @param aliases alias names for the bean, or <code>null</code> if none
	 */
	public BeanComponentDefinition(BeanDefinition beanDefinition, String beanName, String[] aliases) {
		super(beanDefinition, beanName, aliases);
		findInnerBeanDefinitionsAndBeanReferences(beanDefinition);
	}

	/**
	 * Create a new BeanComponentDefinition for the given bean.
	 * @param holder the BeanDefinitionHolder encapsulating the
	 * bean definition as well as the name of the bean
	 */
	public BeanComponentDefinition(BeanDefinitionHolder holder) {
		super(holder);
		findInnerBeanDefinitionsAndBeanReferences(holder.getBeanDefinition());
	}


	private void findInnerBeanDefinitionsAndBeanReferences(BeanDefinition beanDefinition) {
		List<BeanDefinition> innerBeans = new ArrayList<BeanDefinition>();
		List<BeanReference> references = new ArrayList<BeanReference>();
		PropertyValues propertyValues = beanDefinition.getPropertyValues();
		for (int i = 0; i < propertyValues.getPropertyValues().length; i++) {
			PropertyValue propertyValue = propertyValues.getPropertyValues()[i];
			Object value = propertyValue.getValue();
			if (value instanceof BeanDefinitionHolder) {
				innerBeans.add(((BeanDefinitionHolder) value).getBeanDefinition());
			}
			else if (value instanceof BeanDefinition) {
				innerBeans.add((BeanDefinition) value);
			}
			else if (value instanceof BeanReference) {
				references.add((BeanReference) value);
			}
		}
		this.innerBeanDefinitions = innerBeans.toArray(new BeanDefinition[innerBeans.size()]);
		this.beanReferences = references.toArray(new BeanReference[references.size()]);
	}


	public String getName() {
		return getBeanName();
	}

	public String getDescription() {
		return getShortDescription();
	}

	public BeanDefinition[] getBeanDefinitions() {
		return new BeanDefinition[] {getBeanDefinition()};
	}

	public BeanDefinition[] getInnerBeanDefinitions() {
		return this.innerBeanDefinitions;
	}

	public BeanReference[] getBeanReferences() {
		return this.beanReferences;
	}


	/**
	 * This implementation returns this ComponentDefinition's description.
	 * @see #getDescription()
	 */
	@Override
	public String toString() {
		return getDescription();
	}

	/**
	 * This implementations expects the other object to be of type BeanComponentDefinition
	 * as well, in addition to the superclass's equality requirements.
	 */
	@Override
	public boolean equals(Object other) {
		return (this == other || (other instanceof BeanComponentDefinition && super.equals(other)));
	}

}
