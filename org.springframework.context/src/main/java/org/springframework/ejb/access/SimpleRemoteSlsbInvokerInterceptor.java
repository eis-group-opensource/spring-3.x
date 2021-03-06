/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.ejb.access;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBObject;
import javax.naming.NamingException;

import org.aopalliance.intercept.MethodInvocation;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.remoting.RemoteLookupFailureException;
import org.springframework.remoting.rmi.RmiClientInterceptorUtils;

/**
 * Basic invoker for a remote Stateless Session Bean.
 * Designed for EJB 2.x, but works for EJB 3 Session Beans as well.
 *
 * <p>"Creates" a new EJB instance for each invocation, or caches the session
 * bean instance for all invocations (see {@link #setCacheSessionBean}).
 * See {@link org.springframework.jndi.JndiObjectLocator} for info on
 * how to specify the JNDI location of the target EJB.
 *
 * <p>In a bean container, this class is normally best used as a singleton. However,
 * if that bean container pre-instantiates singletons (as do the XML ApplicationContext
 * variants) you may have a problem if the bean container is loaded before the EJB
 * container loads the target EJB. That is because by default the JNDI lookup will be
 * performed in the init method of this class and cached, but the EJB will not have been
 * bound at the target location yet. The best solution is to set the "lookupHomeOnStartup"
 * property to "false", in which case the home will be fetched on first access to the EJB.
 * (This flag is only true by default for backwards compatibility reasons).
 *
 * <p>This invoker is typically used with an RMI business interface, which serves
 * as super-interface of the EJB component interface. Alternatively, this invoker
 * can also proxy a remote SLSB with a matching non-RMI business interface, i.e. an
 * interface that mirrors the EJB business methods but does not declare RemoteExceptions.
 * In the latter case, RemoteExceptions thrown by the EJB stub will automatically get
 * converted to Spring's unchecked RemoteAccessException.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 09.05.2003
 * @see org.springframework.remoting.RemoteAccessException
 * @see AbstractSlsbInvokerInterceptor#setLookupHomeOnStartup
 * @see AbstractSlsbInvokerInterceptor#setCacheHome
 * @see AbstractRemoteSlsbInvokerInterceptor#setRefreshHomeOnConnectFailure
 */
public class SimpleRemoteSlsbInvokerInterceptor extends AbstractRemoteSlsbInvokerInterceptor
		implements DisposableBean {

	private boolean cacheSessionBean = false;

	private Object beanInstance;

	private final Object beanInstanceMonitor = new Object();


	/**
	 * Set whether to cache the actual session bean object.
	 * <p>Off by default for standard EJB compliance. Turn this flag
	 * on to optimize session bean access for servers that are
	 * known to allow for caching the actual session bean object.
	 * @see #setCacheHome
	 */
	public void setCacheSessionBean(boolean cacheSessionBean) {
		this.cacheSessionBean = cacheSessionBean;
	}


	/**
	 * This implementation "creates" a new EJB instance for each invocation.
	 * Can be overridden for custom invocation strategies.
	 * <p>Alternatively, override {@link #getSessionBeanInstance} and
	 * {@link #releaseSessionBeanInstance} to change EJB instance creation,
	 * for example to hold a single shared EJB component instance.
	 */
	@Override
	protected Object doInvoke(MethodInvocation invocation) throws Throwable {
		Object ejb = null;
		try {
			ejb = getSessionBeanInstance();
			return RmiClientInterceptorUtils.invokeRemoteMethod(invocation, ejb);
		}
		catch (NamingException ex) {
			throw new RemoteLookupFailureException("Failed to locate remote EJB [" + getJndiName() + "]", ex);
		}
		catch (InvocationTargetException ex) {
			Throwable targetEx = ex.getTargetException();
			if (targetEx instanceof RemoteException) {
				RemoteException rex = (RemoteException) targetEx;
				throw RmiClientInterceptorUtils.convertRmiAccessException(
				    invocation.getMethod(), rex, isConnectFailure(rex), getJndiName());
			}
			else if (targetEx instanceof CreateException) {
				throw RmiClientInterceptorUtils.convertRmiAccessException(
				    invocation.getMethod(), targetEx, "Could not create remote EJB [" + getJndiName() + "]");
			}
			throw targetEx;
		}
		finally {
			if (ejb instanceof EJBObject) {
				releaseSessionBeanInstance((EJBObject) ejb);
			}
		}
	}

	/**
	 * Return an EJB component instance to delegate the call to.
	 * <p>The default implementation delegates to {@link #newSessionBeanInstance}.
	 * @return the EJB component instance
	 * @throws NamingException if thrown by JNDI
	 * @throws InvocationTargetException if thrown by the create method
	 * @see #newSessionBeanInstance
	 */
	protected Object getSessionBeanInstance() throws NamingException, InvocationTargetException {
		if (this.cacheSessionBean) {
			synchronized (this.beanInstanceMonitor) {
				if (this.beanInstance == null) {
					this.beanInstance = newSessionBeanInstance();
				}
				return this.beanInstance;
			}
		}
		else {
			return newSessionBeanInstance();
		}
	}

	/**
	 * Release the given EJB instance.
	 * <p>The default implementation delegates to {@link #removeSessionBeanInstance}.
	 * @param ejb the EJB component instance to release
	 * @see #removeSessionBeanInstance
	 */
	protected void releaseSessionBeanInstance(EJBObject ejb) {
		if (!this.cacheSessionBean) {
			removeSessionBeanInstance(ejb);
		}
	}

	/**
	 * Reset the cached session bean instance, if necessary.
	 */
	@Override
	protected void refreshHome() throws NamingException {
		super.refreshHome();
		if (this.cacheSessionBean) {
			synchronized (this.beanInstanceMonitor) {
				this.beanInstance = null;
			}
		}
	}

	/**
	 * Remove the cached session bean instance, if necessary.
	 */
	public void destroy() {
		if (this.cacheSessionBean) {
			synchronized (this.beanInstanceMonitor) {
				if (this.beanInstance instanceof EJBObject) {
					removeSessionBeanInstance((EJBObject) this.beanInstance);
				}
			}
		}
	}

}
