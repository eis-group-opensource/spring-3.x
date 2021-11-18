/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scripting.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.scripting.support.ScriptFactoryPostProcessor;

/**
 * @author Rob Harrop
 * @author Mark Fisher
 * @since 2.5
 */
public abstract class LangNamespaceUtils {

	/**
	 * The unique name under which the internally managed {@link ScriptFactoryPostProcessor} is
	 * registered in the {@link BeanDefinitionRegistry}.
	 */
	private static final String SCRIPT_FACTORY_POST_PROCESSOR_BEAN_NAME =
			"org.springframework.scripting.config.scriptFactoryPostProcessor";


	/**
	 * Register a {@link ScriptFactoryPostProcessor} bean definition in the supplied
	 * {@link BeanDefinitionRegistry} if the {@link ScriptFactoryPostProcessor} hasn't
	 * already been registered.
	 * @param registry the {@link BeanDefinitionRegistry} to register the script processor with
	 * @return the {@link ScriptFactoryPostProcessor} bean definition (new or already registered)
	 */
	public static BeanDefinition registerScriptFactoryPostProcessorIfNecessary(BeanDefinitionRegistry registry) {
		BeanDefinition beanDefinition = null;
		if (registry.containsBeanDefinition(SCRIPT_FACTORY_POST_PROCESSOR_BEAN_NAME)) {
			beanDefinition = registry.getBeanDefinition(SCRIPT_FACTORY_POST_PROCESSOR_BEAN_NAME);
		}
		else {
			beanDefinition = new RootBeanDefinition(ScriptFactoryPostProcessor.class);
			registry.registerBeanDefinition(SCRIPT_FACTORY_POST_PROCESSOR_BEAN_NAME, beanDefinition);
		}
		return beanDefinition;
	}

}
