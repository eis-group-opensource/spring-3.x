/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.style;

/**
 * Simple utility class to allow for convenient access to value 
 * styling logic, mainly to support descriptive logging messages.
 *
 * <p>For more sophisticated needs, use the {@link ValueStyler} abstraction
 * directly. This class simply uses a shared {@link DefaultValueStyler}
 * instance underneath.
 *
 * @author Keith Donald
 * @since 1.2.2
 * @see ValueStyler
 * @see DefaultValueStyler
 */
public abstract class StylerUtils {
	
	/**
	 * Default ValueStyler instance used by the <code>style</code> method.
	 * Also available for the {@link ToStringCreator} class in this package.
	 */
	static final ValueStyler DEFAULT_VALUE_STYLER = new DefaultValueStyler();

	/**
	 * Style the specified value according to default conventions.
	 * @param value the Object value to style
	 * @return the styled String
	 * @see DefaultValueStyler
	 */
	public static String style(Object value) {
		return DEFAULT_VALUE_STYLER.style(value);
	}

}
