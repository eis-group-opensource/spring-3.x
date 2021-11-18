/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export.naming;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 * Strategy interface that encapsulates the creation of <code>ObjectName</code> instances.
 *
 * <p>Used by the <code>MBeanExporter</code> to obtain <code>ObjectName</code>s
 * when registering beans.
 *
 * @author Rob Harrop
 * @since 1.2
 * @see org.springframework.jmx.export.MBeanExporter
 * @see javax.management.ObjectName
 */
public interface ObjectNamingStrategy {

	/**
	 * Obtain an <code>ObjectName</code> for the supplied bean.
	 * @param managedBean the bean that will be exposed under the
	 * returned <code>ObjectName</code>
	 * @param beanKey the key associated with this bean in the beans map
	 * passed to the <code>MBeanExporter</code>
	 * @return the <code>ObjectName</code> instance
	 * @throws MalformedObjectNameException if the resulting <code>ObjectName</code> is invalid
	 */
	ObjectName getObjectName(Object managedBean, String beanKey) throws MalformedObjectNameException;

}
