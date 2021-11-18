/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.format.number;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.Locale;

import org.junit.Test;
import org.springframework.format.number.CurrencyFormatter;

/**
 * @author Keith Donald
 */
public class CurrencyFormatterTests {

	private CurrencyFormatter formatter = new CurrencyFormatter();
	
	@Test
	public void formatValue() {
		assertEquals("$23.00", formatter.print(new BigDecimal("23"), Locale.US));
	}

	@Test
	public void parseValue() throws ParseException {
		assertEquals(new BigDecimal("23.56"), formatter.parse("$23.56", Locale.US));
	}

	@Test(expected = ParseException.class)
	public void parseBogusValue() throws ParseException {
		formatter.parse("bogus", Locale.US);
	}

	@Test
	public void parseValueDefaultRoundDown() throws ParseException {
		this.formatter.setRoundingMode(RoundingMode.DOWN);
		assertEquals(new BigDecimal("23.56"), formatter.parse("$23.567", Locale.US));
	}

	@Test
	public void parseWholeValue() throws ParseException {
		assertEquals(new BigDecimal("23.00"), formatter.parse("$23", Locale.US));
	}

	@Test(expected=ParseException.class)
	public void parseValueNotLenientFailure() throws ParseException {
		formatter.parse("$23.56bogus", Locale.US);
	}

}
