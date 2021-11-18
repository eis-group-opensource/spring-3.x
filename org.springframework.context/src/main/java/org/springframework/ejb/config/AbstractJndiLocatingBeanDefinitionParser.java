/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.ejb.config;

import org.w3c.dom.Element;
import static org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.*;

import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;

/**
 * Abstract base class for BeanDefinitionParsers which build
 * JNDI-locating beans, supporting an optional "jndiEnvironment"
 * bean property, populated from an "environment" XML sub-element.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @author Oliver Gierke
 * @since 2.0
 */
abstract class AbstractJndiLocatingBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {

	public static final String ENVIRONMENT = "environment";

	public static final String ENVIRONMENT_REF = "environment-ref";

	public static final String JNDI_ENVIRONMENT = "jndiEnvironment";


	@Override
	protected boolean isEligibleAttribute(String attributeName) {
		return (super.isEligibleAttribute(attributeName) && !ENVIRONMENT_REF.equals(attributeName) && !LAZY_INIT_ATTRIBUTE
				.equals(attributeName));
	}

	@Override
	protected void postProcess(BeanDefinitionBuilder definitionBuilder, Element element) {
		Object envValue = DomUtils.getChildElementValueByTagName(element, ENVIRONMENT);
		if (envValue != null) {
			// Specific environment settings defined, overriding any shared properties.
			definitionBuilder.addPropertyValue(JNDI_ENVIRONMENT, envValue);
		}
		else {
			// Check whether there is a reference to shared environment properties...
			String envRef = element.getAttribute(ENVIRONMENT_REF);
			if (StringUtils.hasLength(envRef)) {
				definitionBuilder.addPropertyValue(JNDI_ENVIRONMENT, new RuntimeBeanReference(envRef));
			}
		}

		String lazyInit = element.getAttribute(LAZY_INIT_ATTRIBUTE);
		if (StringUtils.hasText(lazyInit) && !DEFAULT_VALUE.equals(lazyInit)) {
			definitionBuilder.setLazyInit(TRUE_VALUE.equals(lazyInit));
		}
	}
}
