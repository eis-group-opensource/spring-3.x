/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.format.number;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * A general-purpose Number formatter.
 *
 * <p>Delegates to {@link NumberFormat#getInstance(Locale)}.
 * Configures BigDecimal parsing so there is no loss in precision.
 * Allows configuration over the decimal number pattern.
 * The {@link #parse(String, Locale)} routine always returns a BigDecimal.
 *
 * @author Keith Donald
 * @author Juergen Hoeller
 * @since 3.0
 * @see #setPattern
 * @see #setLenient
 */
public class NumberFormatter extends AbstractNumberFormatter {

	private String pattern;


	/**
	 * Create a new NumberFormatter without a pattern.
	 */
	public NumberFormatter() {
	}

	/**
	 * Create a new NumberFormatter with the specified pattern.
	 * @param pattern the format pattern
	 * @see #setPattern
	 */
	public NumberFormatter(String pattern) {
		this.pattern = pattern;
	}


	/**
	 * Sets the pattern to use to format number values.
	 * If not specified, the default DecimalFormat pattern is used.
	 * @see DecimalFormat#applyPattern(String)
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}


	public NumberFormat getNumberFormat(Locale locale) {
		NumberFormat format = NumberFormat.getInstance(locale);
		if (!(format instanceof DecimalFormat)) {
			if (this.pattern != null) {
				throw new IllegalStateException("Cannot support pattern for non-DecimalFormat: " + format);
			}
			return format;
		}
		DecimalFormat decimalFormat = (DecimalFormat) format;
		decimalFormat.setParseBigDecimal(true);
		if (this.pattern != null) {
			decimalFormat.applyPattern(this.pattern);
		}
		return decimalFormat;
	}

}
