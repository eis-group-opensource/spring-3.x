/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export.naming;

import java.util.Hashtable;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.springframework.jmx.support.ObjectNameManager;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

/**
 * An implementation of the <code>ObjectNamingStrategy</code> interface that
 * creates a name based on the the identity of a given instance.
 *
 * <p>The resulting <code>ObjectName</code> will be in the form
 * <i>package</i>:class=<i>class name</i>,hashCode=<i>identity hash (in hex)</i>
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 1.2
 */
public class IdentityNamingStrategy implements ObjectNamingStrategy {

	public static final String TYPE_KEY = "type";

	public static final String HASH_CODE_KEY = "hashCode";


	/**
	 * Returns an instance of <code>ObjectName</code> based on the identity
	 * of the managed resource.
	 */
	public ObjectName getObjectName(Object managedBean, String beanKey) throws MalformedObjectNameException {
		String domain = ClassUtils.getPackageName(managedBean.getClass());
		Hashtable keys = new Hashtable();
		keys.put(TYPE_KEY, ClassUtils.getShortName(managedBean.getClass()));
		keys.put(HASH_CODE_KEY, ObjectUtils.getIdentityHexString(managedBean));
		return ObjectNameManager.getInstance(domain, keys);
	}

}
