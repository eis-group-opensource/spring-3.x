/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package test.util;

/**
 * This interface can be implemented by cacheable objects or cache entries,
 * to enable the freshness of objects to be checked.
 *
 * @author Rod Johnson
 */
public interface TimeStamped {
	
	/**
	 * Return the timestamp for this object.
	 * @return long the timestamp for this object,
	 * as returned by System.currentTimeMillis()
	 */
	long getTimeStamp();

}
