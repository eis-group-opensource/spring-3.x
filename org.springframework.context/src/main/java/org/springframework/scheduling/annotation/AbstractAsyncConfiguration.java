/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scheduling.annotation;

import java.util.Collection;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

/**
 * Abstract base {@code Configuration} class providing common structure for enabling
 * Spring's asynchronous method execution capability.
 *
 * @author Chris Beams
 * @since 3.1
 * @see EnableAsync
 */
@Configuration
public abstract class AbstractAsyncConfiguration implements ImportAware {

	protected AnnotationAttributes enableAsync;
	protected Executor executor;

	public void setImportMetadata(AnnotationMetadata importMetadata) {
		this.enableAsync = AnnotationAttributes.fromMap(
				importMetadata.getAnnotationAttributes(EnableAsync.class.getName(), false));
		Assert.notNull(this.enableAsync,
				"@EnableAsync is not present on importing class " +
				importMetadata.getClassName());
	}

	/**
	 * The component that will apply async execution advice to beans annotated with
	 * the async annotation. Subclasses will provide either a BeanPostProcessor in
	 * the case of proxy-based advice, or an AspectJ aspect if weaving is preferred.
	 */
	public abstract Object asyncAdvisor();

	/**
	 * Collect any {@link AsyncConfigurer} beans through autowiring.
	 */
	@Autowired(required = false)
	void setConfigurers(Collection<AsyncConfigurer> configurers) {
		if (configurers == null || configurers.isEmpty()) {
			return;
		}

		if (configurers.size() > 1) {
			throw new IllegalStateException("only one AsyncConfigurer may exist");
		}

		AsyncConfigurer configurer = configurers.iterator().next();
		this.executor = configurer.getAsyncExecutor();
	}
}
