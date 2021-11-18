/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.format.datetime.joda;

import java.util.Locale;

import org.joda.time.ReadablePartial;
import org.joda.time.format.DateTimeFormatter;

import org.springframework.format.Printer;

/**
 * Prints JodaTime {@link ReadablePartial} instances using a {@link DateTimeFormatter}.
 *
 * @author Keith Donald
 * @since 3.0
 */
public final class ReadablePartialPrinter implements Printer<ReadablePartial> {

	private final DateTimeFormatter formatter;

	/**
	 * Create a new ReadableInstantPrinter.
	 * @param formatter the Joda DateTimeFormatter instance
	 */
	public ReadablePartialPrinter(DateTimeFormatter formatter) {
		this.formatter = formatter;
	}

	public String print(ReadablePartial partial, Locale locale) {
		return JodaTimeContextHolder.getFormatter(this.formatter, locale).print(partial);
	}

}
