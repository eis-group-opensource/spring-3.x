/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.util.TimeZone;

/**
 * Editor for <code>java.util.TimeZone</code>, translating timezone IDs into
 * TimeZone objects. Does not expose a text representation for TimeZone objects.
 *
 * @author Juergen Hoeller
 * @since 3.0
 * @see java.util.TimeZone
 */
public class TimeZoneEditor extends PropertyEditorSupport {

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(TimeZone.getTimeZone(text));
	}

	/**
	 * This implementation returns <code>null</code> to indicate that
	 * there is no appropriate text representation.
	 */
	@Override
	public String getAsText() {
		return null;
	}

}
