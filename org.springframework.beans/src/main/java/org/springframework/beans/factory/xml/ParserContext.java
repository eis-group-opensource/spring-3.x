/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.xml;

import java.util.Stack;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.parsing.ComponentDefinition;
import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * Context that gets passed along a bean definition parsing process,
 * encapsulating all relevant configuration as well as state.
 * Nested inside an {@link XmlReaderContext}.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 2.0
 * @see XmlReaderContext
 * @see BeanDefinitionParserDelegate
 */
public final class ParserContext {

	private final XmlReaderContext readerContext;

	private final BeanDefinitionParserDelegate delegate;

	private BeanDefinition containingBeanDefinition;

	private final Stack<ComponentDefinition> containingComponents = new Stack<ComponentDefinition>();


	public ParserContext(XmlReaderContext readerContext, BeanDefinitionParserDelegate delegate) {
		this.readerContext = readerContext;
		this.delegate = delegate;
	}

	public ParserContext(XmlReaderContext readerContext, BeanDefinitionParserDelegate delegate,
			BeanDefinition containingBeanDefinition) {

		this.readerContext = readerContext;
		this.delegate = delegate;
		this.containingBeanDefinition = containingBeanDefinition;
	}


	public final XmlReaderContext getReaderContext() {
		return this.readerContext;
	}

	public final BeanDefinitionRegistry getRegistry() {
		return this.readerContext.getRegistry();
	}

	public final BeanDefinitionParserDelegate getDelegate() {
		return this.delegate;
	}

	public final BeanDefinition getContainingBeanDefinition() {
		return this.containingBeanDefinition;
	}

	public final boolean isNested() {
		return (this.containingBeanDefinition != null);
	}

	public boolean isDefaultLazyInit() {
		return BeanDefinitionParserDelegate.TRUE_VALUE.equals(this.delegate.getDefaults().getLazyInit());
	}

	public Object extractSource(Object sourceCandidate) {
		return this.readerContext.extractSource(sourceCandidate);
	}

	public CompositeComponentDefinition getContainingComponent() {
		return (!this.containingComponents.isEmpty() ?
				(CompositeComponentDefinition) this.containingComponents.lastElement() : null);
	}

	public void pushContainingComponent(CompositeComponentDefinition containingComponent) {
		this.containingComponents.push(containingComponent);
	}

	public CompositeComponentDefinition popContainingComponent() {
		return (CompositeComponentDefinition) this.containingComponents.pop();
	}

	public void popAndRegisterContainingComponent() {
		registerComponent(popContainingComponent());
	}

	public void registerComponent(ComponentDefinition component) {
		CompositeComponentDefinition containingComponent = getContainingComponent();
		if (containingComponent != null) {
			containingComponent.addNestedComponent(component);
		}
		else {
			this.readerContext.fireComponentRegistered(component);
		}
	}

	public void registerBeanComponent(BeanComponentDefinition component) {
		BeanDefinitionReaderUtils.registerBeanDefinition(component, getRegistry());
		registerComponent(component);
	}

}
