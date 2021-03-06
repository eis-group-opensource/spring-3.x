/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.config;

import org.w3c.dom.Element;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.jmx.export.annotation.AnnotationMBeanExporter;
import org.springframework.jmx.support.MBeanRegistrationSupport;
import org.springframework.util.StringUtils;

/**
 * Parser for the &lt;context:mbean-export/&gt; element.
 *
 * <p>Registers an instance of
 * {@link org.springframework.jmx.export.annotation.AnnotationMBeanExporter}
 * within the context.
 *
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @since 2.5
 * @see org.springframework.jmx.export.annotation.AnnotationMBeanExporter
 */
class MBeanExportBeanDefinitionParser extends AbstractBeanDefinitionParser {

	private static final String MBEAN_EXPORTER_BEAN_NAME = "mbeanExporter";

	private static final String DEFAULT_DOMAIN_ATTRIBUTE = "default-domain";

	private static final String SERVER_ATTRIBUTE = "server";

	private static final String REGISTRATION_ATTRIBUTE = "registration";

	private static final String REGISTRATION_IGNORE_EXISTING = "ignoreExisting";

	private static final String REGISTRATION_REPLACE_EXISTING = "replaceExisting";


	@Override
	protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) {
		return MBEAN_EXPORTER_BEAN_NAME;
	}

	@Override
	protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(AnnotationMBeanExporter.class);
		
		// Mark as infrastructure bean and attach source location.
		builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		builder.getRawBeanDefinition().setSource(parserContext.extractSource(element));
		
		String defaultDomain = element.getAttribute(DEFAULT_DOMAIN_ATTRIBUTE);
		if (StringUtils.hasText(defaultDomain)) {
			builder.addPropertyValue("defaultDomain", defaultDomain);
		}

		String serverBeanName = element.getAttribute(SERVER_ATTRIBUTE);
		if (StringUtils.hasText(serverBeanName)) {
			builder.addPropertyReference("server", serverBeanName);
		}
		else {
			AbstractBeanDefinition specialServer = MBeanServerBeanDefinitionParser.findServerForSpecialEnvironment();
			if (specialServer != null) {
				builder.addPropertyValue("server", specialServer);
			}
		}

		String registration = element.getAttribute(REGISTRATION_ATTRIBUTE);
		int registrationBehavior = MBeanRegistrationSupport.REGISTRATION_FAIL_ON_EXISTING;
		if (REGISTRATION_IGNORE_EXISTING.equals(registration)) {
			registrationBehavior = MBeanRegistrationSupport.REGISTRATION_IGNORE_EXISTING;
		}
		else if (REGISTRATION_REPLACE_EXISTING.equals(registration)) {
			registrationBehavior = MBeanRegistrationSupport.REGISTRATION_REPLACE_EXISTING;
		}
		builder.addPropertyValue("registrationBehavior", registrationBehavior);

		return builder.getBeanDefinition();
	}

}
