/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.ejb.support;

import javax.ejb.SessionContext;

/**
 * Base class for Spring-based EJB 2.x session beans. Not intended for direct
 * subclassing: Extend {@link AbstractStatelessSessionBean} or
 * {@link AbstractStatefulSessionBean} instead.
 *
 * <p>This class saves the session context provided by the EJB container in an
 * instance variable and exposes it through the {@link SmartSessionBean} interface.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 */
public abstract class AbstractSessionBean extends AbstractEnterpriseBean implements SmartSessionBean {

	/** The SessionContext passed to this EJB */
	private SessionContext sessionContext;


	/**
	 * Set the session context for this EJB.
	 * <p><b>When overriding this method, be sure to invoke this form of it first.</b>
	 */
	public void setSessionContext(SessionContext sessionContext) {
		this.sessionContext = sessionContext;
	}

	/**
	 * Convenience method for subclasses, returning the EJB session context
	 * saved on initialization ({@link #setSessionContext}).
	 */
	public final SessionContext getSessionContext() {
		return this.sessionContext;
	}

}
