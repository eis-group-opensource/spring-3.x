/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export;

import javax.management.ObjectName;

/**
 * A listener that allows application code to be notified when an MBean is
 * registered and unregistered via an {@link MBeanExporter}.
 *
 * @author Rob Harrop
 * @since 1.2.2
 * @see org.springframework.jmx.export.MBeanExporter#setListeners
 */
public interface MBeanExporterListener {

	/**
	 * Called by {@link MBeanExporter} after an MBean has been <i>successfully</i>
	 * registered with an {@link javax.management.MBeanServer}.
	 * @param objectName the <code>ObjectName</code> of the registered MBean
	 */
	void mbeanRegistered(ObjectName objectName);

	/**
	 * Called by {@link MBeanExporter} after an MBean has been <i>successfully</i>
	 * unregistered from an {@link javax.management.MBeanServer}.
	 * @param objectName the <code>ObjectName</code> of the unregistered MBean
	 */
	void mbeanUnregistered(ObjectName objectName);

}
