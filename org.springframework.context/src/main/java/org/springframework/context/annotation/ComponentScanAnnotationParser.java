/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * Parser for the @{@link ComponentScan} annotation.
 *
 * @author Chris Beams
 * @since 3.1
 * @see ClassPathBeanDefinitionScanner#scan(String...)
 * @see ComponentScanBeanDefinitionParser
 */
class ComponentScanAnnotationParser {

	private final ResourceLoader resourceLoader;

	private final Environment environment;

	private final BeanDefinitionRegistry registry;

	private final BeanNameGenerator beanNameGenerator;


	public ComponentScanAnnotationParser(
			ResourceLoader resourceLoader, Environment environment,
			BeanNameGenerator beanNameGenerator, BeanDefinitionRegistry registry) {

		this.resourceLoader = resourceLoader;
		this.environment = environment;
		this.beanNameGenerator = beanNameGenerator;
		this.registry = registry;
	}


	public Set<BeanDefinitionHolder> parse(AnnotationAttributes componentScan) {
		ClassPathBeanDefinitionScanner scanner =
			new ClassPathBeanDefinitionScanner(registry, componentScan.getBoolean("useDefaultFilters"));

		Assert.notNull(this.environment, "Environment must not be null");
		scanner.setEnvironment(this.environment);

		Assert.notNull(this.resourceLoader, "ResourceLoader must not be null");
		scanner.setResourceLoader(this.resourceLoader);

		Class<? extends BeanNameGenerator> generatorClass = componentScan.getClass("nameGenerator");
		boolean useInheritedGenerator = BeanNameGenerator.class.equals(generatorClass);
		scanner.setBeanNameGenerator(useInheritedGenerator
				? this.beanNameGenerator
				: BeanUtils.instantiateClass(generatorClass));

		ScopedProxyMode scopedProxyMode = componentScan.getEnum("scopedProxy");
		if (scopedProxyMode != ScopedProxyMode.DEFAULT) {
			scanner.setScopedProxyMode(scopedProxyMode);
		} else {
			Class<? extends ScopeMetadataResolver> resolverClass = componentScan.getClass("scopeResolver");
			scanner.setScopeMetadataResolver(BeanUtils.instantiateClass(resolverClass));
		}

		scanner.setResourcePattern(componentScan.getString("resourcePattern"));

		for (AnnotationAttributes filter : componentScan.getAnnotationArray("includeFilters")) {
			for (TypeFilter typeFilter : typeFiltersFor(filter)) {
				scanner.addIncludeFilter(typeFilter);
			}
		}
		for (AnnotationAttributes filter : componentScan.getAnnotationArray("excludeFilters")) {
			for (TypeFilter typeFilter : typeFiltersFor(filter)) {
				scanner.addExcludeFilter(typeFilter);
			}
		}

		List<String> basePackages = new ArrayList<String>();
		for (String pkg : componentScan.getStringArray("value")) {
			if (StringUtils.hasText(pkg)) {
				basePackages.add(pkg);
			}
		}
		for (String pkg : componentScan.getStringArray("basePackages")) {
			if (StringUtils.hasText(pkg)) {
				basePackages.add(pkg);
			}
		}
		for (Class<?> clazz : componentScan.getClassArray("basePackageClasses")) {
			basePackages.add(ClassUtils.getPackageName(clazz));
		}

		if (basePackages.isEmpty()) {
			throw new IllegalStateException("At least one base package must be specified");
		}

		return scanner.doScan(basePackages.toArray(new String[]{}));
	}

	private List<TypeFilter> typeFiltersFor(AnnotationAttributes filterAttributes) {
		List<TypeFilter> typeFilters = new ArrayList<TypeFilter>();
		FilterType filterType = filterAttributes.getEnum("type");

		for (Class<?> filterClass : filterAttributes.getClassArray("value")) {
			switch (filterType) {
				case ANNOTATION:
					Assert.isAssignable(Annotation.class, filterClass,
							"An error occured when processing a @ComponentScan " +
							"ANNOTATION type filter: ");
					@SuppressWarnings("unchecked")
					Class<Annotation> annoClass = (Class<Annotation>)filterClass;
					typeFilters.add(new AnnotationTypeFilter(annoClass));
					break;
				case ASSIGNABLE_TYPE:
					typeFilters.add(new AssignableTypeFilter(filterClass));
					break;
				case CUSTOM:
					Assert.isAssignable(TypeFilter.class, filterClass,
							"An error occured when processing a @ComponentScan " +
							"CUSTOM type filter: ");
					typeFilters.add(BeanUtils.instantiateClass(filterClass, TypeFilter.class));
					break;
				default:
					throw new IllegalArgumentException("unknown filter type " + filterType);
			}
		}
		return typeFilters;
	}
}
