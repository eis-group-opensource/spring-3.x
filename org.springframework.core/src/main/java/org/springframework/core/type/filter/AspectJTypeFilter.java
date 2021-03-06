/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.type.filter;

import java.io.IOException;

import org.aspectj.bridge.IMessageHandler;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.World;
import org.aspectj.weaver.bcel.BcelWorld;
import org.aspectj.weaver.patterns.Bindings;
import org.aspectj.weaver.patterns.FormalBinding;
import org.aspectj.weaver.patterns.IScope;
import org.aspectj.weaver.patterns.PatternParser;
import org.aspectj.weaver.patterns.SimpleScope;
import org.aspectj.weaver.patterns.TypePattern;

import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;

/**
 * Type filter that uses AspectJ type pattern for matching.
 *
 * <p>A critical implementation details of this type filter is that it does not
 * load the class being examined to match with a type pattern.
 *
 * @author Ramnivas Laddad
 * @author Juergen Hoeller
 * @since 2.5
 */
public class AspectJTypeFilter implements TypeFilter {

	private final World world;

	private final TypePattern typePattern;


	public AspectJTypeFilter(String typePatternExpression, ClassLoader classLoader) {
		this.world = new BcelWorld(classLoader, IMessageHandler.THROW, null);
		this.world.setBehaveInJava5Way(true);
		PatternParser patternParser = new PatternParser(typePatternExpression);
		TypePattern typePattern = patternParser.parseTypePattern();
		typePattern.resolve(this.world);
		IScope scope = new SimpleScope(this.world, new FormalBinding[0]);
		this.typePattern = typePattern.resolveBindings(scope, Bindings.NONE, false, false);
	}


	public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
			throws IOException {

		String className = metadataReader.getClassMetadata().getClassName();
		ResolvedType resolvedType = this.world.resolve(className);
		return this.typePattern.matchesStatically(resolvedType);
	}

}
