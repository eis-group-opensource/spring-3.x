/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation;

import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;

/**
 * Convenience methods adapting {@link AnnotationMetadata} and {@link MethodMetadata}
 * annotation attribute maps to the {@link AnnotationAttributes} API. As of Spring 3.1.1,
 * both the reflection- and ASM-based implementations of these SPIs return
 * {@link AnnotationAttributes} instances anyway, but for backward-compatibility, their
 * signatures still return Maps. Therefore, for the usual case, these methods perform
 * little more than a cast from Map to AnnotationAttributes.
 *
 * @author Chris Beams
 * @since 3.1.1
 * @see AnnotationAttributes#fromMap(java.util.Map)
 */
class MetadataUtils {

	public static AnnotationAttributes attributesFor(AnnotationMetadata metadata, Class<?> annoClass) {
		return attributesFor(metadata, annoClass.getName());
	}

	public static AnnotationAttributes attributesFor(AnnotationMetadata metadata, String annoClassName) {
		return AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(annoClassName, false));
	}

	public static AnnotationAttributes attributesFor(MethodMetadata metadata, Class<?> targetAnno) {
		return AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(targetAnno.getName()));
	}

}
