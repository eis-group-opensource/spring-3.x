/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.propertyeditors;

import java.beans.PropertyEditorSupport;

import org.springframework.util.StringUtils;

/**
 * Editor for a {@link java.lang.Character}, to populate a property
 * of type <code>Character</code> or <code>char</code> from a String value.
 *
 * <p>Note that the JDK does not contain a default
 * {@link java.beans.PropertyEditor property editor} for <code>char</code>!
 * {@link org.springframework.beans.BeanWrapperImpl} will register this
 * editor by default.
 * 
 * <p>Also supports conversion from a Unicode character sequence; e.g.
 * <code>u0041</code> ('A').
 *
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @author Rick Evans
 * @since 1.2
 * @see java.lang.Character
 * @see org.springframework.beans.BeanWrapperImpl
 */
public class CharacterEditor extends PropertyEditorSupport {

	/**
	 * The prefix that identifies a string as being a Unicode character sequence.
	 */
	private static final String UNICODE_PREFIX = "\\u";

	/**
	 * The length of a Unicode character sequence.
	 */
	private static final int UNICODE_LENGTH = 6;


	private final boolean allowEmpty;


	/**
	 * Create a new CharacterEditor instance.
	 * <p>The "allowEmpty" parameter controls whether an empty String is
	 * to be allowed in parsing, i.e. be interpreted as the <code>null</code>
	 * value when {@link #setAsText(String) text is being converted}. If
	 * <code>false</code>, an {@link IllegalArgumentException} will be thrown
	 * at that time.
	 * @param allowEmpty if empty strings are to be allowed
	 */
	public CharacterEditor(boolean allowEmpty) {
		this.allowEmpty = allowEmpty;
	}


	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (this.allowEmpty && !StringUtils.hasLength(text)) {
			// Treat empty String as null value.
			setValue(null);
		}
		else if (text == null) {
			throw new IllegalArgumentException("null String cannot be converted to char type");
		}
		else if (isUnicodeCharacterSequence(text)) {
			setAsUnicode(text);
		}
		else if (text.length() != 1) {
			throw new IllegalArgumentException("String [" + text + "] with length " +
					text.length() + " cannot be converted to char type");
		}
		else {
			setValue(new Character(text.charAt(0)));
		}
	}

	@Override
	public String getAsText() {
		Object value = getValue();
		return (value != null ? value.toString() : "");
	}


	private boolean isUnicodeCharacterSequence(String sequence) {
		return (sequence.startsWith(UNICODE_PREFIX) && sequence.length() == UNICODE_LENGTH);
	}

	private void setAsUnicode(String text) {
		int code = Integer.parseInt(text.substring(UNICODE_PREFIX.length()), 16);
		setValue(new Character((char) code));
	}

}
