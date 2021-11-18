/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.format.number;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;
import org.springframework.util.StringUtils;
import org.springframework.util.StringValueResolver;

/**
 * Formats fields annotated with the {@link NumberFormat} annotation.
 *
 * @author Keith Donald
 * @since 3.0
 * @see NumberFormat
 */
public class NumberFormatAnnotationFormatterFactory
		implements AnnotationFormatterFactory<NumberFormat>, EmbeddedValueResolverAware {

	private final Set<Class<?>> fieldTypes;

	private StringValueResolver embeddedValueResolver;


	public NumberFormatAnnotationFormatterFactory() {
		Set<Class<?>> rawFieldTypes = new HashSet<Class<?>>(7);
		rawFieldTypes.add(Short.class);
		rawFieldTypes.add(Integer.class);
		rawFieldTypes.add(Long.class);
		rawFieldTypes.add(Float.class);
		rawFieldTypes.add(Double.class);
		rawFieldTypes.add(BigDecimal.class);
		rawFieldTypes.add(BigInteger.class);
		this.fieldTypes = Collections.unmodifiableSet(rawFieldTypes);
	}

	public final Set<Class<?>> getFieldTypes() {
		return this.fieldTypes;
	}


	public void setEmbeddedValueResolver(StringValueResolver resolver) {
		this.embeddedValueResolver = resolver;
	}

	protected String resolveEmbeddedValue(String value) {
		return (this.embeddedValueResolver != null ? this.embeddedValueResolver.resolveStringValue(value) : value);
	}


	public Printer<Number> getPrinter(NumberFormat annotation, Class<?> fieldType) {
		return configureFormatterFrom(annotation);
	}
	
	public Parser<Number> getParser(NumberFormat annotation, Class<?> fieldType) {
		return configureFormatterFrom(annotation);
	}


	private Formatter<Number> configureFormatterFrom(NumberFormat annotation) {
		if (StringUtils.hasLength(annotation.pattern())) {
			return new NumberFormatter(resolveEmbeddedValue(annotation.pattern()));
		}
		else {
			Style style = annotation.style();
			if (style == Style.PERCENT) {
				return new PercentFormatter();
			}
			else if (style == Style.CURRENCY) {
				return new CurrencyFormatter();
			}
			else {
				return new NumberFormatter();
			}
		}
	}

}
