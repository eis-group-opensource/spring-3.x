/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.ejb.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * {@link org.springframework.beans.factory.xml.NamespaceHandler}
 * for the '<code>jee</code>' namespace.
 *
 * @author Rob Harrop
 * @since 2.0
 */
public class JeeNamespaceHandler extends NamespaceHandlerSupport {

	public void init() {
		registerBeanDefinitionParser("jndi-lookup", new JndiLookupBeanDefinitionParser());
		registerBeanDefinitionParser("local-slsb", new LocalStatelessSessionBeanDefinitionParser());
		registerBeanDefinitionParser("remote-slsb", new RemoteStatelessSessionBeanDefinitionParser());
	}

}
