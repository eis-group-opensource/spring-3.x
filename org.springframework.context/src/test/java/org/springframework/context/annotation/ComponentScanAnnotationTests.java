/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation;

import org.junit.Test;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.core.type.filter.TypeFilter;

/**
 * Unit tests for the @ComponentScan annotation.
 *
 * @author Chris Beams
 * @since 3.1
 * @see ComponentScanAnnotationIntegrationTests
 */
public class ComponentScanAnnotationTests {
	@Test
	public void noop() {
		// no-op; the @ComponentScan-annotated MyConfig class below simply excercises
		// available attributes of the annotation.
	}
}

@interface MyAnnotation { }

@Configuration
@ComponentScan(
	basePackageClasses={TestBean.class},
	nameGenerator = DefaultBeanNameGenerator.class,
	scopedProxy = ScopedProxyMode.NO,
	scopeResolver = AnnotationScopeMetadataResolver.class,
	useDefaultFilters = false,
	resourcePattern = "**/*custom.class",
	includeFilters = {
		@Filter(type = FilterType.ANNOTATION, value = MyAnnotation.class)
	},
	excludeFilters = {
		@Filter(type = FilterType.CUSTOM, value = TypeFilter.class)
	}
)
class MyConfig {

}

@ComponentScan(basePackageClasses=example.scannable.NamedComponent.class)
class SimpleConfig { }