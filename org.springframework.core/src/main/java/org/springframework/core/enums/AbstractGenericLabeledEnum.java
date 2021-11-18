/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.enums;

/**
 * Base class for labeled enum instances that aren't static.
 *
 * @author Keith Donald
 * @since 1.2.6
 * @deprecated as of Spring 3.0, in favor of Java 5 enums.
 */
public abstract class AbstractGenericLabeledEnum extends AbstractLabeledEnum {

	/**
	 * A descriptive label for the enum.
	 */
	private final String label;


	/**
	 * Create a new StaticLabeledEnum instance.
	 * @param label the label; if <code>null</code>), the enum's code
	 * will be used as label
	 */
	protected AbstractGenericLabeledEnum(String label) {
		this.label = label;
	}


	public String getLabel() {
		if (this.label != null) {
			return label;
		}
		else {
			return getCode().toString();
		}
	}	

}
