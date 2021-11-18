/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.enums;

import org.springframework.util.Assert;

/**
 * Implementation of LabeledEnum which uses a String as the code type.
 *
 * <p>Should almost always be subclassed, but for some simple situations it may be
 * used directly. Note that you will not be able to use unique type-based
 * functionality like <code>LabeledEnumResolver.getLabeledEnumSet(type) in this case.
 *
 * @author Keith Donald
 * @author Juergen Hoeller
 * @since 1.2.2
 * @see org.springframework.core.enums.LabeledEnumResolver#getLabeledEnumSet(Class)
 * @deprecated as of Spring 3.0, in favor of Java 5 enums.
 */
@Deprecated
public class StringCodedLabeledEnum extends AbstractGenericLabeledEnum {

	/**
	 * The unique code of this enum.
	 */
	private final String code;


	/**
	 * Create a new StringCodedLabeledEnum instance.
	 * @param code the String code
	 * @param label the label (can be <code>null</code>)
	 */
	public StringCodedLabeledEnum(String code, String label) {
		super(label);
		Assert.notNull(code, "'code' must not be null");
		this.code = code;
	}


	public Comparable getCode() {
		return this.code;
	}

	/**
	 * Return the String code of this LabeledEnum instance.
	 */
	public String getStringCode() {
		return (String) getCode();
	}

}
