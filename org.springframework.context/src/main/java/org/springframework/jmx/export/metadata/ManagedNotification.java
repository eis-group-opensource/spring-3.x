/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export.metadata;

import org.springframework.util.StringUtils;

/**
 * Metadata that indicates a JMX notification emitted by a bean.
 *
 * @author Rob Harrop
 * @since 2.0
 */
public class ManagedNotification {

	private String[] notificationTypes;

	private String name;

	private String description;


	/**
	 * Set a single notification type, or a list of notification types
	 * as comma-delimited String.
	 */
	public void setNotificationType(String notificationType) {
		this.notificationTypes = StringUtils.commaDelimitedListToStringArray(notificationType);
	}

	/**
	 * Set a list of notification types.
	 */
	public void setNotificationTypes(String[] notificationTypes) {
		this.notificationTypes = notificationTypes;
	}

	/**
	 * Return the list of notification types.
	 */
	public String[] getNotificationTypes() {
		return this.notificationTypes;
	}

	/**
	 * Set the name of this notification.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Return the name of this notification.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Set a description for this notification.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Return a description for this notification.
	 */
	public String getDescription() {
		return this.description;
	}

}
