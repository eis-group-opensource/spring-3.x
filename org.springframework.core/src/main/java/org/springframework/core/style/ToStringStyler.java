/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.style;

/**
 * A strategy interface for pretty-printing <code>toString()</code> methods.
 * Encapsulates the print algorithms; some other object such as a builder
 * should provide the workflow.
 *
 * @author Keith Donald
 * @since 1.2.2
 */
public interface ToStringStyler {

	/**
	 * Style a <code>toString()</code>'ed object before its fields are styled.
	 * @param buffer the buffer to print to
	 * @param obj the object to style
	 */
	void styleStart(StringBuilder buffer, Object obj);

	/**
	 * Style a <code>toString()</code>'ed object after it's fields are styled.
	 * @param buffer the buffer to print to
	 * @param obj the object to style
	 */
	void styleEnd(StringBuilder buffer, Object obj);

	/**
	 * Style a field value as a string.
	 * @param buffer the buffer to print to
	 * @param fieldName the he name of the field
	 * @param value the field value
	 */
	void styleField(StringBuilder buffer, String fieldName, Object value);

	/**
	 * Style the given value.
	 * @param buffer the buffer to print to
	 * @param value the field value
	 */
	void styleValue(StringBuilder buffer, Object value);

	/**
	 * Style the field separator.
	 * @param buffer buffer to print to
	 */
	void styleFieldSeparator(StringBuilder buffer);

}
