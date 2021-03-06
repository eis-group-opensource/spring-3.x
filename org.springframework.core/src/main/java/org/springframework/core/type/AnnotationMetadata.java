/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.type;

import java.util.Map;
import java.util.Set;

/**
 * Interface that defines abstract access to the annotations of a specific
 * class, in a form that does not require that class to be loaded yet.
 *
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @since 2.5
 * @see StandardAnnotationMetadata
 * @see org.springframework.core.type.classreading.MetadataReader#getAnnotationMetadata()
 */
public interface AnnotationMetadata extends ClassMetadata {

	/**
	 * Return the names of all annotation types defined on the underlying class.
	 * @return the annotation type names
	 */
	Set<String> getAnnotationTypes();

	/**
	 * Return the names of all meta-annotation types defined on the
	 * given annotation type of the underlying class.
	 * @param annotationType the meta-annotation type to look for
	 * @return the meta-annotation type names
	 */
	Set<String> getMetaAnnotationTypes(String annotationType);

	/**
	 * Determine whether the underlying class has an annotation of the given
	 * type defined.
	 * @param annotationType the annotation type to look for
	 * @return whether a matching annotation is defined
	 */
	boolean hasAnnotation(String annotationType);

	/**
	 * Determine whether the underlying class has an annotation that
	 * is itself annotated with the meta-annotation of the given type.
	 * @param metaAnnotationType the meta-annotation type to look for
	 * @return whether a matching meta-annotation is defined
	 */
	boolean hasMetaAnnotation(String metaAnnotationType);

	/**
	 * Determine whether the underlying class has an annotation or
	 * meta-annotation of the given type defined.
	 * <p>This is equivalent to a "hasAnnotation || hasMetaAnnotation"
	 * check. If this method returns <code>true</code>, then
	 * {@link #getAnnotationAttributes} will return a non-null Map.
	 * @param annotationType the annotation type to look for
	 * @return whether a matching annotation is defined
	 */
	boolean isAnnotated(String annotationType);

	/**
	 * Retrieve the attributes of the annotation of the given type,
	 * if any (i.e. if defined on the underlying class, as direct
	 * annotation or as meta-annotation).
	 * @param annotationType the annotation type to look for
	 * @return a Map of attributes, with the attribute name as key (e.g. "value")
	 * and the defined attribute value as Map value. This return value will be
	 * <code>null</code> if no matching annotation is defined.
	 */
	Map<String, Object> getAnnotationAttributes(String annotationType);

	/**
	 * Retrieve the attributes of the annotation of the given type,
	 * if any (i.e. if defined on the underlying class, as direct
	 * annotation or as meta-annotation).
	 * @param annotationType the annotation type to look for
	 * @param classValuesAsString whether to convert class references to String
	 * class names for exposure as values in the returned Map, instead of Class
	 * references which might potentially have to be loaded first
	 * @return a Map of attributes, with the attribute name as key (e.g. "value")
	 * and the defined attribute value as Map value. This return value will be
	 * <code>null</code> if no matching annotation is defined.
	 */
	Map<String, Object> getAnnotationAttributes(String annotationType, boolean classValuesAsString);

	/**
	 * Determine whether the underlying class has any methods that are
	 * annotated (or meta-annotated) with the given annotation type.
	 */
	boolean hasAnnotatedMethods(String annotationType);

	/**
	 * Retrieve the method metadata for all methods that are annotated
	 * (or meta-annotated) with the given annotation type.
	 * <p>For any returned method, {@link MethodMetadata#isAnnotated} will
	 * return <code>true</code> for the given annotation type.
	 * @param annotationType the annotation type to look for
	 * @return a Set of {@link MethodMetadata} for methods that have a matching
	 * annotation. The return value will be an empty set if no methods match
	 * the annotation type.
	 */
	Set<MethodMetadata> getAnnotatedMethods(String annotationType);

}
