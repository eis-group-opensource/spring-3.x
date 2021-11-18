/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.ejb.support;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Convenient base class for EJB 2.x stateless session beans (SLSBs),
 * minimizing the work involved in implementing an SLSB and preventing
 * common errors. <b>Note that SLSBs are the most useful kind of EJB.</b>
 *
 * <p>As the ejbActivate() and ejbPassivate() methods cannot be invoked
 * on SLSBs, these methods are implemented to throw an exception and should
 * not be overriden by subclasses. (Unfortunately the EJB specification
 * forbids enforcing this by making EJB lifecycle methods final.)
 *
 * <p>There should be no need to override the <code>setSessionContext()</code>
 * or <code>ejbCreate()</code> lifecycle methods.
 *
 * <p>Subclasses are left to implement the <code>onEjbCreate()</code> method
 * to do whatever initialization they wish to do after their BeanFactory has
 * already been loaded, and is available from the <code>getBeanFactory()</code>
 * method.
 *
 * <p>This class provides the no-arg <code>ejbCreate()</code> method required
 * by the EJB specification, but not the SessionBean interface, eliminating
 * a common cause of EJB deployment failure.
 *
 * @author Rod Johnson
 */
public abstract class AbstractStatelessSessionBean extends AbstractSessionBean {

	/** Logger available to subclasses */
	protected final Log logger = LogFactory.getLog(getClass());


	/**
	 * This implementation loads the BeanFactory. A BeansException thrown by
	 * loadBeanFactory will simply get propagated, as it is a runtime exception.
	 * <p>Don't override it (although it can't be made final): code your own
	 * initialization in onEjbCreate(), which is called when the BeanFactory
	 * is available.
	 * <p>Unfortunately we can't load the BeanFactory in setSessionContext(),
	 * as resource manager access isn't permitted there - but the BeanFactory
	 * may require it.
	 */
	public void ejbCreate() throws CreateException {
		loadBeanFactory();
		onEjbCreate();
	}

	/**
	 * Subclasses must implement this method to do any initialization
	 * they would otherwise have done in an <code>ejbCreate()</code> method.
	 * In contrast to <code>ejbCreate</code>, the BeanFactory will have been loaded here.
	 * <p>The same restrictions apply to the work of this method as
	 * to an <code>ejbCreate()</code> method.
	 * @throws CreateException
	 */
	protected abstract void onEjbCreate() throws CreateException;


	/**
	 * @see javax.ejb.SessionBean#ejbActivate(). This method always throws an exception, as
	 * it should not be invoked by the EJB container.
	 */
	public void ejbActivate() throws EJBException {
		throw new IllegalStateException("ejbActivate must not be invoked on a stateless session bean");
	}

	/**
	 * @see javax.ejb.SessionBean#ejbPassivate(). This method always throws an exception, as
	 * it should not be invoked by the EJB container.
	 */
	public void ejbPassivate() throws EJBException {
		throw new IllegalStateException("ejbPassivate must not be invoked on a stateless session bean");
	}

}
