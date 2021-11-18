/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.enums;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.util.Assert;

/**
 * {@link LabeledEnumResolver} that resolves statically defined enumerations.
 * Static implies all enum instances were defined within Java code,
 * implementing the type-safe enum pattern.
 *
 * @author Keith Donald
 * @author Juergen Hoeller
 * @since 1.2.2
 * @deprecated as of Spring 3.0, in favor of Java 5 enums.
 */
@Deprecated
public class StaticLabeledEnumResolver extends AbstractCachingLabeledEnumResolver {

	/**
	 * Shared <code>StaticLabeledEnumResolver</code> singleton instance.
	 */
	private static final StaticLabeledEnumResolver INSTANCE = new StaticLabeledEnumResolver();


	/**
	 * Return the shared <code>StaticLabeledEnumResolver</code> singleton instance.
	 * Mainly for resolving unique StaticLabeledEnum references on deserialization.
	 * @see StaticLabeledEnum
	 */
	public static StaticLabeledEnumResolver instance() {
		return INSTANCE;
	}


	@Override
	protected Set<LabeledEnum> findLabeledEnums(Class type) {
		Set<LabeledEnum> typeEnums = new TreeSet<LabeledEnum>();
		for (Field field : type.getFields()) {
			if (Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers())) {
				if (type.isAssignableFrom(field.getType())) {
					try {
						Object value = field.get(null);
						Assert.isTrue(value instanceof LabeledEnum, "Field value must be a LabeledEnum instance");
						typeEnums.add((LabeledEnum) value);
					}
					catch (IllegalAccessException ex) {
						logger.warn("Unable to access field value: " + field, ex);
					}
				}
			}
		}
		return typeEnums;
	}

}
