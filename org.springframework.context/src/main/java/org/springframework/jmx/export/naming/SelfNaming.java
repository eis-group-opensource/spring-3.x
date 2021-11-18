/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export.naming;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 * Interface that allows infrastructure components to provide their own
 * <code>ObjectName</code>s to the <code>MBeanExporter</code>.
 *
 * <p><b>Note:</b> This interface is mainly intended for internal usage.
 *
 * @author Rob Harrop
 * @since 1.2.2
 * @see org.springframework.jmx.export.MBeanExporter
 */
public interface SelfNaming {

	/**
	 * Return the <code>ObjectName</code> for the implementing object.
	 * @throws MalformedObjectNameException if thrown by the ObjectName constructor
	 * @see javax.management.ObjectName#ObjectName(String)
	 * @see javax.management.ObjectName#getInstance(String)
	 * @see org.springframework.jmx.support.ObjectNameManager#getInstance(String)
	 */
	ObjectName getObjectName() throws MalformedObjectNameException;

}
