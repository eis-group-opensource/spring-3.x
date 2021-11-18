/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.type.classreading;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.asm.AnnotationVisitor;
import org.springframework.asm.MethodAdapter;
import org.springframework.asm.Opcodes;
import org.springframework.asm.Type;
import org.springframework.asm.commons.EmptyVisitor;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.MethodMetadata;
import org.springframework.util.MultiValueMap;

/**
 * ASM method visitor which looks for the annotations defined on the method,
 * exposing them through the {@link org.springframework.core.type.MethodMetadata}
 * interface.
 *
 * @author Juergen Hoeller
 * @author Mark Pollack
 * @author Costin Leau
 * @author Chris Beams
 * @since 3.0
 */
final class MethodMetadataReadingVisitor extends MethodAdapter implements MethodMetadata {

	private final String name;

	private final int access;

	private String declaringClassName;

	private final ClassLoader classLoader;

	private final MultiValueMap<String, MethodMetadata> methodMetadataMap;

	private final Map<String, AnnotationAttributes> attributeMap = new LinkedHashMap<String, AnnotationAttributes>(2);

	public MethodMetadataReadingVisitor(String name, int access, String declaringClassName, ClassLoader classLoader,
			MultiValueMap<String, MethodMetadata> methodMetadataMap) {
		super(new EmptyVisitor());
		this.name = name;
		this.access = access;
		this.declaringClassName = declaringClassName;
		this.classLoader = classLoader;
		this.methodMetadataMap = methodMetadataMap;
	}

	@Override
	public AnnotationVisitor visitAnnotation(final String desc, boolean visible) {
		String className = Type.getType(desc).getClassName();
		methodMetadataMap.add(className, this);
		return new AnnotationAttributesReadingVisitor(className, this.attributeMap, null, this.classLoader);
	}

	public String getMethodName() {
		return this.name;
	}

	public boolean isStatic() {
		return ((this.access & Opcodes.ACC_STATIC) != 0);
	}

	public boolean isFinal() {
		return ((this.access & Opcodes.ACC_FINAL) != 0);
	}

	public boolean isOverridable() {
		return (!isStatic() && !isFinal() && ((this.access & Opcodes.ACC_PRIVATE) == 0));
	}

	public boolean isAnnotated(String annotationType) {
		return this.attributeMap.containsKey(annotationType);
	}

	public AnnotationAttributes getAnnotationAttributes(String annotationType) {
		return this.attributeMap.get(annotationType);
	}

	public String getDeclaringClassName() {
		return this.declaringClassName;
	}
}