/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.format.datetime;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.format.datetime.DateFormatter;

/**
 * @author Keith Donald
 */
public class DateFormatterTests {

	private DateFormatter formatter = new DateFormatter("yyyy-MM-dd");
	
	@Test
	public void formatValue() {
		Calendar cal = Calendar.getInstance(Locale.US);
		cal.clear();
		cal.set(Calendar.YEAR, 2009);
		cal.set(Calendar.MONTH, Calendar.JUNE);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		assertEquals("2009-06-01", formatter.print(cal.getTime(), Locale.US));
	}
	
	@Test
	public void parseValue() throws ParseException {
		Calendar cal = Calendar.getInstance(Locale.US);
		cal.clear();
		cal.set(Calendar.YEAR, 2009);
		cal.set(Calendar.MONTH, Calendar.JUNE);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		assertEquals(cal.getTime(), formatter.parse("2009-06-01", Locale.US));
	}

}
