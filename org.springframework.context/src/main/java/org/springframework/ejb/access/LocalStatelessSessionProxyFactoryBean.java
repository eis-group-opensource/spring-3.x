/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.ejb.access;

import javax.naming.NamingException;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.ClassUtils;

/**
 * Convenient {@link FactoryBean} for local Stateless Session Bean (SLSB) proxies.
 * Designed for EJB 2.x, but works for EJB 3 Session Beans as well.
 *
 * <p>See {@link org.springframework.jndi.JndiObjectLocator} for info on
 * how to specify the JNDI location of the target EJB.
 * 
 * <p>If you want control over interceptor chaining, use an AOP ProxyFactoryBean
 * with LocalSlsbInvokerInterceptor rather than rely on this class.
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
 * @author Rod Johnson
 * @author Colin Sampaleanu
 * @since 09.05.2003
 * @see AbstractSlsbInvokerInterceptor#setLookupHomeOnStartup
 * @see AbstractSlsbInvokerInterceptor#setCacheHome
 */
public class LocalStatelessSessionProxyFactoryBean extends LocalSlsbInvokerInterceptor
		implements FactoryBean<Object>, BeanClassLoaderAware {

	/** The business interface of the EJB we're proxying */
	private Class businessInterface;

	private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

	/** EJBLocalObject */
	private Object proxy;


	/**
	 * Set the business interface of the EJB we're proxying.
	 * This will normally be a super-interface of the EJB local component interface.
	 * Using a business methods interface is a best practice when implementing EJBs.
	 * @param businessInterface set the business interface of the EJB
	 */
	public void setBusinessInterface(Class businessInterface) {
		this.businessInterface = businessInterface;
	}

	/**
	 * Return the business interface of the EJB we're proxying.
	 */
	public Class getBusinessInterface() {
		return this.businessInterface;
	}

	public void setBeanClassLoader(ClassLoader classLoader) {
		this.beanClassLoader = classLoader;
	}

	@Override
	public void afterPropertiesSet() throws NamingException {
		super.afterPropertiesSet();
		if (this.businessInterface == null) {
			throw new IllegalArgumentException("businessInterface is required");
		}
		this.proxy = new ProxyFactory(this.businessInterface, this).getProxy(this.beanClassLoader);
	}


	public Object getObject() {
		return this.proxy;
	}

	public Class<?> getObjectType() {
		return this.businessInterface;
	}

	public boolean isSingleton() {
		return true;
	}

}
