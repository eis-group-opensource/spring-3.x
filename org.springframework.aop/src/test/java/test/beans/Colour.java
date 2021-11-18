/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package test.beans;

import org.springframework.core.enums.ShortCodedLabeledEnum;

/**
 * @author Rob Harrop
 */
@SuppressWarnings("serial")
public class Colour extends ShortCodedLabeledEnum {

	public static final Colour RED = new Colour(0, "RED");
	public static final Colour BLUE = new Colour(1, "BLUE");
	public static final Colour GREEN = new Colour(2, "GREEN");
	public static final Colour PURPLE = new Colour(3, "PURPLE");

	private Colour(int code, String label) {
		super(code, label);
	}

}