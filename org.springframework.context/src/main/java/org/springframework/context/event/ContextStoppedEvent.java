/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.event;

import org.springframework.context.ApplicationContext;

/**
 * Event raised when an <code>ApplicationContext</code> gets stopped.
 *
 * @author Mark Fisher
 * @author Juergen Hoeller
 * @since 2.5
 * @see ContextStartedEvent
 */
public class ContextStoppedEvent extends ApplicationContextEvent {

	/**
	 * Create a new ContextStoppedEvent.
	 * @param source the <code>ApplicationContext</code> that has been stopped
	 * (must not be <code>null</code>)
	 */
	public ContextStoppedEvent(ApplicationContext source) {
		super(source);
	}

}
