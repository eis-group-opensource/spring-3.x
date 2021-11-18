/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.BeansException;
import org.springframework.util.StringUtils;

/**
 * Bean factory post processor that logs a warning for {@link Deprecated @Deprecated} beans.
 *
 * @author Arjen Poutsma
 * @since 3.0.3
 */
public class DeprecatedBeanWarner implements BeanFactoryPostProcessor {

	/**
	 * Logger available to subclasses.
	 */
	protected transient Log logger = LogFactory.getLog(getClass());

	/**
	 * Set the name of the logger to use. The name will be passed to the underlying logger implementation through
	 * Commons Logging, getting interpreted as log category according to the logger's configuration.
	 * <p>This can be specified to not log into the category of this warner class but rather into a specific named category.
	 * @see org.apache.commons.logging.LogFactory#getLog(String)
	 * @see org.apache.log4j.Logger#getLogger(String)
	 * @see java.util.logging.Logger#getLogger(String)
	 */
	public void setLoggerName(String loggerName) {
		this.logger = LogFactory.getLog(loggerName);
	}


	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		if (isLogEnabled()) {
			String[] beanNames = beanFactory.getBeanDefinitionNames();
			for (String beanName : beanNames) {
				Class<?> beanType = beanFactory.getType(beanName);
				if (beanType != null && beanType.isAnnotationPresent(Deprecated.class)) {
					BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
					logDeprecatedBean(beanName, beanDefinition);
				}
			}
		}
	}

	/**
	 * Logs a warning for a bean annotated with {@link Deprecated @Deprecated}.
	 *
	 * @param beanName the name of the deprecated bean
	 * @param beanDefinition the definition of the deprecated bean
	 */
	protected void logDeprecatedBean(String beanName, BeanDefinition beanDefinition) {
		StringBuilder builder = new StringBuilder();
		builder.append(beanDefinition.getBeanClassName());
		builder.append(" ['");
		builder.append(beanName);
		builder.append('\'');
		String resourceDescription = beanDefinition.getResourceDescription();
		if (StringUtils.hasLength(resourceDescription)) {
			builder.append(" in ");
			builder.append(resourceDescription);
		}
		builder.append(" ] has been deprecated");
		logger.warn(builder.toString());
	}

	/**
	 * Determine whether the {@link #logger} field is enabled.
	 * <p>Default is {@code true} when the "warn" level is enabled. Subclasses can override this to change the level
	 * under which logging occurs.
	 */
	protected boolean isLogEnabled() {
		return logger.isWarnEnabled();
	}

}
