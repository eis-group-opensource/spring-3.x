/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.enums;

/**
 * Implementation of LabeledEnum which uses Short as the code type.
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
public class ShortCodedLabeledEnum extends AbstractGenericLabeledEnum {

	/**
	 * The unique code of this enum.
	 */
	private final Short code;


	/**
	 * Create a new ShortCodedLabeledEnum instance.
	 * @param code the short code
	 * @param label the label (can be <code>null</code>)
	 */
	public ShortCodedLabeledEnum(int code, String label) {
		super(label);
		this.code = new Short((short) code);
	}

	
	public Comparable getCode() {
		return code;
	}

	/**
	 * Return the short code of this LabeledEnum instance.
	 */
	public short getShortCode() {
		return ((Short) getCode()).shortValue();
	}

}
