/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.format;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * A factory that creates formatters to format values of fields annotated with a particular {@link Annotation}.
 *
 * <p>For example, a <code>DateTimeFormatAnnotationFormatterFactory</code> might create a formatter
 * that formats <code>Date</code> values set on fields annotated with <code>@DateTimeFormat</code>.
 *
 * @author Keith Donald
 * @since 3.0 
 * @param <A> the annotation type that should trigger formatting
 */
public interface AnnotationFormatterFactory<A extends Annotation> {

	/**
	 * The types of fields that may be annotated with the &lt;A&gt; annotation.
	 */
	Set<Class<?>> getFieldTypes();

	/**
	 * Get the Printer to print the value of a field of <code>fieldType</code> annotated with <code>annotation</code>.
	 * If the type &lt;T&gt; the printer accepts is not assignable to <code>fieldType</code>, a coersion from <code>fieldType</code> to &lt;T&gt; will be attempted before the Printer is invoked.
	 * @param annotation the annotation instance
	 * @param fieldType the type of field that was annotated
	 * @return the printer
	 */
	Printer<?> getPrinter(A annotation, Class<?> fieldType);

	/**
	 * Get the Parser to parse a submitted value for a field of <code>fieldType</code> annotated with <code>annotation</code>.
	 * If the object the parser returns is not assignable to <code>fieldType</code>, a coersion to <code>fieldType</code> will be attempted before the field is set.
	 * @param annotation the annotation instance
	 * @param fieldType the type of field that was annotated
	 * @return the parser
	 */
	Parser<?> getParser(A annotation, Class<?> fieldType);
	
}
