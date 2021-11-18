/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scheduling.config;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;

/**
 * Parser for the 'scheduled-tasks' element of the scheduling namespace.
 * 
 * @author Mark Fisher
 * @since 3.0
 */
public class ScheduledTasksBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	private static final String ELEMENT_SCHEDULED = "scheduled";

	@Override
	protected boolean shouldGenerateId() {
		return true;
	}

	@Override
	protected String getBeanClassName(Element element) {
		return "org.springframework.scheduling.config.ScheduledTaskRegistrar";
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		builder.setLazyInit(false); // lazy scheduled tasks are a contradiction in terms -> force to false
		ManagedMap<RuntimeBeanReference, String> cronTaskMap = new ManagedMap<RuntimeBeanReference, String>();
		ManagedMap<RuntimeBeanReference, String> fixedDelayTaskMap = new ManagedMap<RuntimeBeanReference, String>();
		ManagedMap<RuntimeBeanReference, String> fixedRateTaskMap = new ManagedMap<RuntimeBeanReference, String>();
		ManagedMap<RuntimeBeanReference, RuntimeBeanReference> triggerTaskMap = new ManagedMap<RuntimeBeanReference, RuntimeBeanReference>();
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (!isScheduledElement(child, parserContext)) {
				continue;
			}
			Element taskElement = (Element) child;
			String ref = taskElement.getAttribute("ref");
			String method = taskElement.getAttribute("method");
			
			// Check that 'ref' and 'method' are specified
			if (!StringUtils.hasText(ref) || !StringUtils.hasText(method)) {
				parserContext.getReaderContext().error("Both 'ref' and 'method' are required", taskElement);
				// Continue with the possible next task element
				continue;
			}
			
			RuntimeBeanReference runnableBeanRef = new RuntimeBeanReference(
					createRunnableBean(ref, method, taskElement, parserContext));

			String cronAttribute = taskElement.getAttribute("cron");
			String fixedDelayAttribute = taskElement.getAttribute("fixed-delay");
			String fixedRateAttribute = taskElement.getAttribute("fixed-rate");
			String triggerAttribute = taskElement.getAttribute("trigger");

			boolean hasCronAttribute = StringUtils.hasText(cronAttribute);
			boolean hasFixedDelayAttribute = StringUtils.hasText(fixedDelayAttribute);
			boolean hasFixedRateAttribute = StringUtils.hasText(fixedRateAttribute);
			boolean hasTriggerAttribute = StringUtils.hasText(triggerAttribute);

			if (!(hasCronAttribute | hasFixedDelayAttribute | hasFixedRateAttribute | hasTriggerAttribute)) {
				parserContext.getReaderContext().error(
						"exactly one of the 'cron', 'fixed-delay', 'fixed-rate', or 'trigger' attributes is required", taskElement);
				// Continue with the possible next task element
				continue;
			}

			if (hasCronAttribute) {
				cronTaskMap.put(runnableBeanRef, cronAttribute);
			}
			if (hasFixedDelayAttribute) {
				fixedDelayTaskMap.put(runnableBeanRef, fixedDelayAttribute);
			}
			if (hasFixedRateAttribute) {
				fixedRateTaskMap.put(runnableBeanRef, fixedRateAttribute);
			}
			if (hasTriggerAttribute) {
				triggerTaskMap.put(runnableBeanRef, new RuntimeBeanReference(triggerAttribute));
			}
		}
		String schedulerRef = element.getAttribute("scheduler");
		if (StringUtils.hasText(schedulerRef)) {
			builder.addPropertyReference("taskScheduler", schedulerRef);
		}
		builder.addPropertyValue("cronTasks", cronTaskMap);
		builder.addPropertyValue("fixedDelayTasks", fixedDelayTaskMap);
		builder.addPropertyValue("fixedRateTasks", fixedRateTaskMap);
		builder.addPropertyValue("triggerTasks", triggerTaskMap);
	}

	private boolean isScheduledElement(Node node, ParserContext parserContext) {
		return node.getNodeType() == Node.ELEMENT_NODE &&
				ELEMENT_SCHEDULED.equals(parserContext.getDelegate().getLocalName(node));
	}

	private String createRunnableBean(String ref, String method, Element taskElement, ParserContext parserContext) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(
				"org.springframework.scheduling.support.ScheduledMethodRunnable");
		builder.addConstructorArgReference(ref);
		builder.addConstructorArgValue(method);
		// Extract the source of the current task
		builder.getRawBeanDefinition().setSource(parserContext.extractSource(taskElement));
		String generatedName = parserContext.getReaderContext().generateBeanName(builder.getRawBeanDefinition());
		parserContext.registerBeanComponent(new BeanComponentDefinition(builder.getBeanDefinition(), generatedName));
		return generatedName;
	}

}
