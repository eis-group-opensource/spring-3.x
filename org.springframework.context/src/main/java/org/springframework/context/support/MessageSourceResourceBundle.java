/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.support;

import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.util.Assert;

/**
 * Helper class that allows for accessing a Spring
 * {@link org.springframework.context.MessageSource} as a {@link java.util.ResourceBundle}.
 * Used for example to expose a Spring MessageSource to JSTL web views.
 *
 * @author Juergen Hoeller
 * @since 27.02.2003
 * @see org.springframework.context.MessageSource
 * @see java.util.ResourceBundle
 * @see org.springframework.web.servlet.support.JstlUtils#exposeLocalizationContext
 */
public class MessageSourceResourceBundle extends ResourceBundle {

	private final MessageSource messageSource;

	private final Locale locale;


	/**
	 * Create a new MessageSourceResourceBundle for the given MessageSource and Locale.
	 * @param source the MessageSource to retrieve messages from
	 * @param locale the Locale to retrieve messages for
	 */
	public MessageSourceResourceBundle(MessageSource source, Locale locale) {
		Assert.notNull(source, "MessageSource must not be null");
		this.messageSource = source;
		this.locale = locale;
	}

	/**
	 * Create a new MessageSourceResourceBundle for the given MessageSource and Locale.
	 * @param source the MessageSource to retrieve messages from
	 * @param locale the Locale to retrieve messages for
	 * @param parent the parent ResourceBundle to delegate to if no local message found
	 */
	public MessageSourceResourceBundle(MessageSource source, Locale locale, ResourceBundle parent) {
		this(source, locale);
		setParent(parent);
	}


	/**
	 * This implementation resolves the code in the MessageSource.
	 * Returns <code>null</code> if the message could not be resolved.
	 */
	@Override
	protected Object handleGetObject(String code) {
		try {
			return this.messageSource.getMessage(code, null, this.locale);
		}
		catch (NoSuchMessageException ex) {
			return null;
		}
	}

	/**
	 * This implementation returns <code>null</code>, as a MessageSource does
	 * not allow for enumerating the defined message codes.
	 */
	@Override
	public Enumeration<String> getKeys() {
		return null;
	}

	/**
	 * This implementation exposes the specified Locale for introspection
	 * through the standard <code>ResourceBundle.getLocale()</code> method.
	 */
	@Override
	public Locale getLocale() {
		return this.locale;
	}

}
