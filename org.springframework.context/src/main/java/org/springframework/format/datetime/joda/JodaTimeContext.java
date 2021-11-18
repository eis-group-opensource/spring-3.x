/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.format.datetime.joda;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;

/**
 * A context that holds user-specific Joda Time settings such as the user's Chronology (calendar system) and time zone.
 * A <code>null</code> property value indicate the user has not specified a setting.
 *
 * @author Keith Donald
 * @since 3.0
 * @see JodaTimeContextHolder
 */
public class JodaTimeContext {

	private Chronology chronology;

	private DateTimeZone timeZone;


	/**
	 * Set the user's chronology.
	 */
	public void setChronology(Chronology chronology) {
		this.chronology = chronology;
	}

	/**
	 * The user's chronology (calendar system).
	 * Null if not specified.
	 */
	public Chronology getChronology() {
		return this.chronology;
	}

	/**
	 * Set the user's timezone.
	 */
	public void setTimeZone(DateTimeZone timeZone) {
		this.timeZone = timeZone;
	}

	/**
	 * The user's timezone.
	 * Null if not specified.
	 */
	public DateTimeZone getTimeZone() {
		return timeZone;
	}


	/**
	 * Gets the Formatter with the this context's settings applied to the base <code>formatter</code>.
	 * @param formatter the base formatter that establishes default formatting rules, generally context independent
	 * @return the context DateTimeFormatter
	 */
	public DateTimeFormatter getFormatter(DateTimeFormatter formatter) {
		if (this.chronology != null) {
			formatter = formatter.withChronology(this.chronology);
		}
		if (this.timeZone != null) {
			formatter = formatter.withZone(this.timeZone);
		}
		return formatter;
	}

}
