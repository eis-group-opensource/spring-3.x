/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.format.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares that a field should be formatted as a number.
 * Supports formatting by style or custom pattern string.
 * Can be applied to any JDK <code>java.lang.Number</code> type.
 * <p>
 * For style-based formatting, set the {@link #style()} attribute to be the desired {@link Style}.  
 * For custom formatting, set the {@link #pattern()} attribute to be the number pattern, such as <code>#,###.##</code>.
 * <p>
 * Each attribute is mutually exclusive, so only set one attribute per annotation instance (the one most convenient one for your formatting needs).
 * When the pattern attribute is specified, it takes precedence over the style attribute.
 * When no annotation attributes are specified, the default format applied is style-based with a style of {@link Style#NUMBER}.
 * 
 * @author Keith Donald
 * @since 3.0
 * @see java.text.NumberFormat
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NumberFormat {

	/**
	 * The style pattern to use to format the field.
	 * Defaults to {@link Style#NUMBER} for general-purpose number formatter.
	 * Set this attribute when you wish to format your field in accordance with a common style other than the default style.
	 */
	Style style() default Style.NUMBER;

	/**
	 * The custom pattern to use to format the field.
	 * Defaults to empty String, indicating no custom pattern String has been specified.
	 * Set this attribute when you wish to format your field in accordance with a custom number pattern not represented by a style.
	 */
	String pattern() default "";


	/**
	 * Common number format styles.
	 * @author Keith Donald
	 * @since 3.0
	 */
	public enum Style {

		/**
		 * The general-purpose number format for the current locale.
		 */
		NUMBER,
		
		/**
		 * The currency format for the current locale.
		 */
		CURRENCY,

		/**
		 * The percent format for the current locale.
		 */
		PERCENT
	}

}
