/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Factory bean for
 * <a href="http://jakarta.apache.org/commons/logging.html">commons-logging</a>
 * {@link org.apache.commons.logging.Log} instances.
 *
 * <p>Useful for sharing Log instances among multiple beans instead of using
 * one Log instance per class name, e.g. for common log topics.
 *
 * @author Juergen Hoeller
 * @since 16.11.2003
 * @see org.apache.commons.logging.Log
 */
public class CommonsLogFactoryBean implements FactoryBean<Log>, InitializingBean {

	private Log log;


	/**
	 * The name of the log.
	 * <p>This property is required.
	 * @param logName the name of the log
	 */
	public void setLogName(String logName) {
		this.log = LogFactory.getLog(logName);
	}


	public void afterPropertiesSet() {
		if (this.log == null) {
			throw new IllegalArgumentException("'logName' is required");
		}
	}

	public Log getObject() {
		return this.log;
	}

	public Class<? extends Log> getObjectType() {
		return (this.log != null ? this.log.getClass() : Log.class);
	}

	public boolean isSingleton() {
		return true;
	}

}
