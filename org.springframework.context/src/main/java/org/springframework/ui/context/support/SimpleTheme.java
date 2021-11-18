/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.ui.context.support;

import org.springframework.context.MessageSource;
import org.springframework.ui.context.Theme;
import org.springframework.util.Assert;

/**
 * Default {@link Theme} implementation, wrapping a name and an
 * underlying {@link org.springframework.context.MessageSource}.
 *
 * @author Juergen Hoeller
 * @since 17.06.2003
 */
public class SimpleTheme implements Theme {

	private final String name;

	private final MessageSource messageSource;


	/**
	 * Create a SimpleTheme.
	 * @param name the name of the theme
	 * @param messageSource the MessageSource that resolves theme messages
	 */
	public SimpleTheme(String name, MessageSource messageSource) {
		Assert.notNull(name, "Name must not be null");
		Assert.notNull(messageSource, "MessageSource must not be null");
		this.name = name;
		this.messageSource = messageSource;
	}


	public final String getName() {
		return this.name;
	}

	public final MessageSource getMessageSource() {
		return this.messageSource;
	}

}
