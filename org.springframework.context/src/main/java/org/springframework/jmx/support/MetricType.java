/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.support;

/**
 * Represents how the measurement values of a {@code ManagedMetric} will change over time.
 * @author Jennifer Hickey
 * @since 3.0
 */
public enum MetricType {

	/**
	 * The measurement values may go up or down over time
	 */
	GAUGE,

	/**
	 * The measurement values will always increase
	 */
	COUNTER

}
