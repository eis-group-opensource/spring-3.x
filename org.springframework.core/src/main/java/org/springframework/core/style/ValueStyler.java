/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.style;

/**
 * Strategy that encapsulates value String styling algorithms
 * according to Spring conventions.
 *
 * @author Keith Donald
 * @since 1.2.2
 */
public interface ValueStyler {

	/**
	 * Style the given value, returning a String representation.
	 * @param value the Object value to style
	 * @return the styled String
	 */
	String style(Object value);

}
