/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Base JMX test class that pre-loads an ApplicationContext from a user-configurable file. Override the
 * {@link #getApplicationContextPath()} method to control the configuration file location.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 */
public abstract class AbstractJmxTests extends AbstractMBeanServerTests {

	private ConfigurableApplicationContext ctx;


	protected final void onSetUp() throws Exception {
		ctx = loadContext(getApplicationContextPath());
	}

	protected final void onTearDown() throws Exception {
		if (ctx != null) {
			ctx.close();
		}
	}

	protected String getApplicationContextPath() {
		return "org/springframework/jmx/applicationContext.xml";
	}

	protected ApplicationContext getContext() {
		return this.ctx;
	}
}
