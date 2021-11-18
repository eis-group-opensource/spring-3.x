/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package test.util;

import static java.lang.String.format;

import org.springframework.core.io.ClassPathResource;

/**
 * Convenience utilities for common operations with test resources.
 *
 * @author Chris Beams
 */
public class TestResourceUtils {

	/**
	 * Loads a {@link ClassPathResource} qualified by the simple name of clazz,
	 * and relative to the package for clazz.
	 * 
	 * <p>Example: given a clazz 'com.foo.BarTests' and a resourceSuffix of 'context.xml',
	 * this method will return a ClassPathResource representing com/foo/BarTests-context.xml
	 * 
	 * <p>Intended for use loading context configuration XML files within JUnit tests.
	 * 
	 * @param clazz
	 * @param resourceSuffix
	 */
	public static ClassPathResource qualifiedResource(Class<?> clazz, String resourceSuffix) {
		return new ClassPathResource(format("%s-%s", clazz.getSimpleName(), resourceSuffix), clazz);
	}

}
