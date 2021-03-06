/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.management.ObjectName;

import org.springframework.jmx.AbstractJmxTests;

/**
 * @author Rob Harrop
 */
public class CustomEditorConfigurerTests extends AbstractJmxTests {

	private final SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

	protected String getApplicationContextPath() {
		return "org/springframework/jmx/export/customConfigurer.xml";
	}

	public void testDatesInJmx() throws Exception {
		// System.out.println(getServer().getClass().getName());
		ObjectName oname = new ObjectName("bean:name=dateRange");

		Date startJmx = (Date) getServer().getAttribute(oname, "StartDate");
		Date endJmx = (Date) getServer().getAttribute(oname, "EndDate");

		assertEquals("startDate ", getStartDate(), startJmx);
		assertEquals("endDate ", getEndDate(), endJmx);
	}

	public void testGetDates() throws Exception {
		DateRange dr = (DateRange) getContext().getBean("dateRange");

		assertEquals("startDate ", getStartDate(), dr.getStartDate());
		assertEquals("endDate ", getEndDate(), dr.getEndDate());
	}

	private Date getStartDate() throws ParseException {
		Date start = df.parse("2004/10/12");
		return start;
	}

	private Date getEndDate() throws ParseException {
		Date end = df.parse("2004/11/13");
		return end;
	}

}
