/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.enums;

import org.springframework.util.Assert;

/**
 * Implementation of LabeledEnum which uses a letter as the code type.
 *
 * <p>Should almost always be subclassed, but for some simple situations it may be
 * used directly. Note that you will not be able to use unique type-based functionality
 * like <code>LabeledEnumResolver.getLabeledEnumSet(type)</code> in this case.
 *
 * @author Keith Donald
 * @since 1.2.2
 * @deprecated as of Spring 3.0, in favor of Java 5 enums.
 */
@Deprecated
public class LetterCodedLabeledEnum extends AbstractGenericLabeledEnum {

	/**
	 * The unique code of this enum.
	 */
	private final Character code;


	/**
	 * Create a new LetterCodedLabeledEnum instance.
	 * @param code the letter code
	 * @param label the label (can be <code>null</code>)
	 */
	public LetterCodedLabeledEnum(char code, String label) {
		super(label);
		Assert.isTrue(Character.isLetter(code),
				"The code '" + code + "' is invalid: it must be a letter");
		this.code = new Character(code);
	}

	
	public Comparable getCode() {
		return code;
	}

	/**
	 * Return the letter code of this LabeledEnum instance.
	 */
	public char getLetterCode() {
		return ((Character) getCode()).charValue();
	}

}
