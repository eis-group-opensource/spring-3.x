/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.type;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * {@link AnnotationMetadata} implementation that uses standard reflection
 * to introspect a given {@link Class}.
 *
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Chris Beams
 * @since 2.5
 */
public class StandardAnnotationMetadata extends StandardClassMetadata implements AnnotationMetadata {

	private final boolean nestedAnnotationsAsMap;


	/**
	 * Create a new {@code StandardAnnotationMetadata} wrapper for the given Class.
	 * @param introspectedClass the Class to introspect
	 * @see #StandardAnnotationMetadata(Class, boolean)
	 */
	public StandardAnnotationMetadata(Class<?> introspectedClass) {
		this(introspectedClass, false);
	}

	/**
	 * Create a new {@link StandardAnnotationMetadata} wrapper for the given Class,
	 * providing the option to return any nested annotations or annotation arrays in the
	 * form of {@link AnnotationAttributes} instead of actual {@link Annotation} instances.
	 * @param introspectedClass the Class to instrospect
	 * @param nestedAnnotationsAsMap return nested annotations and annotation arrays as
	 * {@link AnnotationAttributes} for compatibility with ASM-based
	 * {@link AnnotationMetadata} implementations
	 * @since 3.1.1
	 */
	public StandardAnnotationMetadata(Class<?> introspectedClass, boolean nestedAnnotationsAsMap) {
		super(introspectedClass);
		this.nestedAnnotationsAsMap = nestedAnnotationsAsMap;
	}


	public Set<String> getAnnotationTypes() {
		Set<String> types = new LinkedHashSet<String>();
		Annotation[] anns = getIntrospectedClass().getAnnotations();
		for (Annotation ann : anns) {
			types.add(ann.annotationType().getName());
		}
		return types;
	}

	public Set<String> getMetaAnnotationTypes(String annotationType) {
		Annotation[] anns = getIntrospectedClass().getAnnotations();
		for (Annotation ann : anns) {
			if (ann.annotationType().getName().equals(annotationType)) {
				Set<String> types = new LinkedHashSet<String>();
				Annotation[] metaAnns = ann.annotationType().getAnnotations();
				for (Annotation metaAnn : metaAnns) {
					types.add(metaAnn.annotationType().getName());
					for (Annotation metaMetaAnn : metaAnn.annotationType().getAnnotations()) {
						types.add(metaMetaAnn.annotationType().getName());
					}
				}
				return types;
			}
		}
		return null;
	}

	public boolean hasAnnotation(String annotationType) {
		Annotation[] anns = getIntrospectedClass().getAnnotations();
		for (Annotation ann : anns) {
			if (ann.annotationType().getName().equals(annotationType)) {
				return true;
			}
		}
		return false;
	}

	public boolean hasMetaAnnotation(String annotationType) {
		Annotation[] anns = getIntrospectedClass().getAnnotations();
		for (Annotation ann : anns) {
			Annotation[] metaAnns = ann.annotationType().getAnnotations();
			for (Annotation metaAnn : metaAnns) {
				if (metaAnn.annotationType().getName().equals(annotationType)) {
					return true;
				}
				for (Annotation metaMetaAnn : metaAnn.annotationType().getAnnotations()) {
					if (metaMetaAnn.annotationType().getName().equals(annotationType)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean isAnnotated(String annotationType) {
		Annotation[] anns = getIntrospectedClass().getAnnotations();
		for (Annotation ann : anns) {
			if (ann.annotationType().getName().equals(annotationType)) {
				return true;
			}
			for (Annotation metaAnn : ann.annotationType().getAnnotations()) {
				if (metaAnn.annotationType().getName().equals(annotationType)) {
					return true;
				}
			}
		}
		return false;
	}

	public Map<String, Object> getAnnotationAttributes(String annotationType) {
		return this.getAnnotationAttributes(annotationType, false);
	}

	public Map<String, Object> getAnnotationAttributes(String annotationType, boolean classValuesAsString) {
		Annotation[] anns = getIntrospectedClass().getAnnotations();
		for (Annotation ann : anns) {
			if (ann.annotationType().getName().equals(annotationType)) {
				return AnnotationUtils.getAnnotationAttributes(
						ann, classValuesAsString, this.nestedAnnotationsAsMap);
			}
			for (Annotation metaAnn : ann.annotationType().getAnnotations()) {
				if (metaAnn.annotationType().getName().equals(annotationType)) {
					return AnnotationUtils.getAnnotationAttributes(
							metaAnn, classValuesAsString, this.nestedAnnotationsAsMap);
				}
			}
		}
		return null;
	}

	public boolean hasAnnotatedMethods(String annotationType) {
		Method[] methods = getIntrospectedClass().getDeclaredMethods();
		for (Method method : methods) {
			for (Annotation ann : method.getAnnotations()) {
				if (ann.annotationType().getName().equals(annotationType)) {
					return true;
				}
				else {
					for (Annotation metaAnn : ann.annotationType().getAnnotations()) {
						if (metaAnn.annotationType().getName().equals(annotationType)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public Set<MethodMetadata> getAnnotatedMethods(String annotationType) {
		Method[] methods = getIntrospectedClass().getDeclaredMethods();
		Set<MethodMetadata> annotatedMethods = new LinkedHashSet<MethodMetadata>();
		for (Method method : methods) {
			for (Annotation ann : method.getAnnotations()) {
				if (ann.annotationType().getName().equals(annotationType)) {
					annotatedMethods.add(new StandardMethodMetadata(method, this.nestedAnnotationsAsMap));
					break;
				}
				else {
					for (Annotation metaAnn : ann.annotationType().getAnnotations()) {
						if (metaAnn.annotationType().getName().equals(annotationType)) {
							annotatedMethods.add(new StandardMethodMetadata(method, this.nestedAnnotationsAsMap));
							break;
						}
					}
				}
			}
		}
		return annotatedMethods;
	}

}
