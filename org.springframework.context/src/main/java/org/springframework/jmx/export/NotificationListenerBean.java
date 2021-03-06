/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export;

import javax.management.NotificationListener;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jmx.support.NotificationListenerHolder;
import org.springframework.util.Assert;

/**
 * Helper class that aggregates a {@link javax.management.NotificationListener},
 * a {@link javax.management.NotificationFilter}, and an arbitrary handback
 * object.
 *
 * <p>Also provides support for associating the encapsulated
 * {@link javax.management.NotificationListener} with any number of
 * MBeans from which it wishes to receive
 * {@link javax.management.Notification Notifications} via the
 * {@link #setMappedObjectNames mappedObjectNames} property.
 *
 * <p>Note: This class supports Spring bean names as
 * {@link #setMappedObjectNames "mappedObjectNames"} as well, as alternative
 * to specifying JMX object names. Note that only beans exported by the
 * same {@link MBeanExporter} are supported for such bean names.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 2.0
 * @see MBeanExporter#setNotificationListeners
 */
public class NotificationListenerBean extends NotificationListenerHolder implements InitializingBean {

	/**
	 * Create a new instance of the {@link NotificationListenerBean} class.
	 */
	public NotificationListenerBean() {
	}

	/**
	 * Create a new instance of the {@link NotificationListenerBean} class.
	 * @param notificationListener the encapsulated listener
	 */
	public NotificationListenerBean(NotificationListener notificationListener) {
		Assert.notNull(notificationListener, "NotificationListener must not be null");
		setNotificationListener(notificationListener);
	}


	public void afterPropertiesSet() {
		if (getNotificationListener() == null) {
			throw new IllegalArgumentException("Property 'notificationListener' is required");
		}
	}

	void replaceObjectName(Object originalName, Object newName) {
		if (this.mappedObjectNames != null && this.mappedObjectNames.contains(originalName)) {
			this.mappedObjectNames.remove(originalName);
			this.mappedObjectNames.add(newName);
		}
	}

}
