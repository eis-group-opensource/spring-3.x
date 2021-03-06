/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;

/**
 * Simple {@link ScopeMetadataResolver} implementation that follows JSR-330 scoping rules:
 * defaulting to prototype scope unless {@link javax.inject.Singleton} is present.
 *
 * <p>This scope resolver can be used with {@link ClassPathBeanDefinitionScanner} and
 * {@link AnnotatedBeanDefinitionReader} for standard JSR-330 compliance. However,
 * in practice, you will typically use Spring's rich default scoping instead - or extend
 * this resolver with custom scoping annotations that point to extended Spring scopes.
 *
 * @author Juergen Hoeller
 * @since 3.0
 * @see #registerScope
 * @see #resolveScopeName
 * @see ClassPathBeanDefinitionScanner#setScopeMetadataResolver
 * @see AnnotatedBeanDefinitionReader#setScopeMetadataResolver
 */
public class Jsr330ScopeMetadataResolver implements ScopeMetadataResolver {

	private final Map<String, String> scopeMap = new HashMap<String, String>();


	public Jsr330ScopeMetadataResolver() {
		registerScope("javax.inject.Singleton", BeanDefinition.SCOPE_SINGLETON);
	}


	/**
	 * Register an extended JSR-330 scope annotation, mapping it onto a
	 * specific Spring scope by name.
	 * @param annotationType the JSR-330 annotation type as a Class
	 * @param scopeName the Spring scope name
	 */
	public final void registerScope(Class annotationType, String scopeName) {
		this.scopeMap.put(annotationType.getName(), scopeName);
	}

	/**
	 * Register an extended JSR-330 scope annotation, mapping it onto a
	 * specific Spring scope by name.
	 * @param annotationType the JSR-330 annotation type by name
	 * @param scopeName the Spring scope name
	 */
	public final void registerScope(String annotationType, String scopeName) {
		this.scopeMap.put(annotationType, scopeName);
	}

	/**
	 * Resolve the given annotation type into a named Spring scope.
	 * <p>The default implementation simply checks against registered scopes.
	 * Can be overridden for custom mapping rules, e.g. naming conventions.
	 * @param annotationType the JSR-330 annotation type
	 * @return the Spring scope name
	 */
	protected String resolveScopeName(String annotationType) {
		return this.scopeMap.get(annotationType);
	}


	public ScopeMetadata resolveScopeMetadata(BeanDefinition definition) {
		ScopeMetadata metadata = new ScopeMetadata();
		metadata.setScopeName(BeanDefinition.SCOPE_PROTOTYPE);
		if (definition instanceof AnnotatedBeanDefinition) {
			AnnotatedBeanDefinition annDef = (AnnotatedBeanDefinition) definition;
			Set<String> annTypes = annDef.getMetadata().getAnnotationTypes();
			String found = null;
			for (String annType : annTypes) {
				Set<String> metaAnns = annDef.getMetadata().getMetaAnnotationTypes(annType);
				if (metaAnns.contains("javax.inject.Scope")) {
					if (found != null) {
						throw new IllegalStateException("Found ambiguous scope annotations on bean class [" +
								definition.getBeanClassName() + "]: " + found + ", " + annType);
					}
					found = annType;
					String scopeName = resolveScopeName(annType);
					if (scopeName == null) {
						throw new IllegalStateException(
								"Unsupported scope annotation - not mapped onto Spring scope name: " + annType);
					}
					metadata.setScopeName(scopeName);
				}
			}
		}
		return metadata;
	}

}
