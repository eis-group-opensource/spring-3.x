/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.i18n;

import java.util.Locale;

import org.springframework.util.Assert;

/**
 * Simple implementation of the {@link LocaleContext} interface,
 * always returning a specified <code>Locale</code>.
 *
 * @author Juergen Hoeller
 * @since 1.2
 */
public class SimpleLocaleContext implements LocaleContext {

	private final Locale locale;


	/**
	 * Create a new SimpleLocaleContext that exposes the specified Locale.
	 * Every <code>getLocale()</code> will return this Locale.
	 * @param locale the Locale to expose
	 */
	public SimpleLocaleContext(Locale locale) {
		Assert.notNull(locale, "Locale must not be null");
		this.locale = locale;
	}

	public Locale getLocale() {
		return this.locale;
	}

	@Override
	public String toString() {
		return this.locale.toString();
	}

}
