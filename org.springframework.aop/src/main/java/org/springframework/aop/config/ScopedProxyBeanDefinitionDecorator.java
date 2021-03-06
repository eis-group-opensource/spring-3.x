/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.config;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionDecorator;
import org.springframework.beans.factory.xml.ParserContext;

/**
 * {@link BeanDefinitionDecorator} responsible for parsing the
 * <code>&lt;aop:scoped-proxy/&gt;</code> tag.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @since 2.0
 */
class ScopedProxyBeanDefinitionDecorator implements BeanDefinitionDecorator {

	private static final String PROXY_TARGET_CLASS = "proxy-target-class";


	public BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder definition, ParserContext parserContext) {
		boolean proxyTargetClass = true;
		if (node instanceof Element) {
			Element ele = (Element) node;
			if (ele.hasAttribute(PROXY_TARGET_CLASS)) {
				proxyTargetClass = Boolean.valueOf(ele.getAttribute(PROXY_TARGET_CLASS));
			}
		}
		
		// Register the original bean definition as it will be referenced by the scoped proxy
		// and is relevant for tooling (validation, navigation).
		BeanDefinitionHolder holder =
				ScopedProxyUtils.createScopedProxy(definition, parserContext.getRegistry(), proxyTargetClass);
		String targetBeanName = ScopedProxyUtils.getTargetBeanName(definition.getBeanName());
		parserContext.getReaderContext().fireComponentRegistered(
				new BeanComponentDefinition(definition.getBeanDefinition(), targetBeanName));
		return holder;
	}

}
