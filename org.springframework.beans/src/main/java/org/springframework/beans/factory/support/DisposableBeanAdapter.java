/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.support;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * Adapter that implements the {@link DisposableBean} and {@link Runnable} interfaces
 * performing various destruction steps on a given bean instance:
 * <ul>
 * <li>DestructionAwareBeanPostProcessors;
 * <li>the bean implementing DisposableBean itself;
 * <li>a custom destroy method specified on the bean definition.
 * </ul>
 *
 * @author Juergen Hoeller
 * @author Costin Leau
 * @since 2.0
 * @see AbstractBeanFactory
 * @see org.springframework.beans.factory.DisposableBean
 * @see org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor
 * @see AbstractBeanDefinition#getDestroyMethodName()
 */
@SuppressWarnings("serial")
class DisposableBeanAdapter implements DisposableBean, Runnable, Serializable {

	private static final Log logger = LogFactory.getLog(DisposableBeanAdapter.class);

	private final Object bean;

	private final String beanName;

	private final boolean invokeDisposableBean;

	private final boolean nonPublicAccessAllowed;

	private String destroyMethodName;

	private transient Method destroyMethod;

	private List<DestructionAwareBeanPostProcessor> beanPostProcessors;

	private final AccessControlContext acc;


	/**
	 * Create a new DisposableBeanAdapter for the given bean.
	 * @param bean the bean instance (never <code>null</code>)
	 * @param beanName the name of the bean
	 * @param beanDefinition the merged bean definition
	 * @param postProcessors the List of BeanPostProcessors
	 * (potentially DestructionAwareBeanPostProcessor), if any
	 */
	public DisposableBeanAdapter(Object bean, String beanName, RootBeanDefinition beanDefinition,
			List<BeanPostProcessor> postProcessors, AccessControlContext acc) {

		Assert.notNull(bean, "Disposable bean must not be null");
		this.bean = bean;
		this.beanName = beanName;
		this.invokeDisposableBean =
				(this.bean instanceof DisposableBean && !beanDefinition.isExternallyManagedDestroyMethod("destroy"));
		this.nonPublicAccessAllowed = beanDefinition.isNonPublicAccessAllowed();
		this.acc = acc;
		inferDestroyMethodIfNecessary(beanDefinition);
		final String destroyMethodName = beanDefinition.getDestroyMethodName();
		if (destroyMethodName != null && !(this.invokeDisposableBean && "destroy".equals(destroyMethodName)) &&
				!beanDefinition.isExternallyManagedDestroyMethod(destroyMethodName)) {
			this.destroyMethodName = destroyMethodName;
			this.destroyMethod = determineDestroyMethod();
			if (this.destroyMethod == null) {
				if (beanDefinition.isEnforceDestroyMethod()) {
					throw new BeanDefinitionValidationException("Couldn't find a destroy method named '" +
							destroyMethodName + "' on bean with name '" + beanName + "'");
				}
			}
			else {
				Class<?>[] paramTypes = this.destroyMethod.getParameterTypes();
				if (paramTypes.length > 1) {
					throw new BeanDefinitionValidationException("Method '" + destroyMethodName + "' of bean '" +
							beanName + "' has more than one parameter - not supported as destroy method");
				}
				else if (paramTypes.length == 1 && !paramTypes[0].equals(boolean.class)) {
					throw new BeanDefinitionValidationException("Method '" + destroyMethodName + "' of bean '" +
							beanName + "' has a non-boolean parameter - not supported as destroy method");
				}
			}
		}
		this.beanPostProcessors = filterPostProcessors(postProcessors);
	}

	/**
	 * If the current value of the given beanDefinition's destroyMethodName property is
	 * {@link AbstractBeanDefinition#INFER_METHOD}, then attempt to infer a destroy method.
	 * Candidate methods are currently limited to public, no-arg methods named 'close'
	 * (whether declared locally or inherited). The given beanDefinition's
	 * destroyMethodName is updated to be null if no such method is found, otherwise set
	 * to the name of the inferred method. This constant serves as the default for the
	 * {@code @Bean#destroyMethod} attribute and the value of the constant may also be
	 * used in XML within the {@code <bean destroy-method="">} or {@code
	 * <beans default-destroy-method="">} attributes.
	 */
	private void inferDestroyMethodIfNecessary(RootBeanDefinition beanDefinition) {
		if ("(inferred)".equals(beanDefinition.getDestroyMethodName())) {
			try {
				Method candidate = bean.getClass().getMethod("close");
				if (Modifier.isPublic(candidate.getModifiers())) {
					beanDefinition.setDestroyMethodName(candidate.getName());
				}
			} catch (NoSuchMethodException ex) {
				// no candidate destroy method found
				beanDefinition.setDestroyMethodName(null);
			}
		}
	}

	/**
	 * Create a new DisposableBeanAdapter for the given bean.
	 */
	private DisposableBeanAdapter(Object bean, String beanName, boolean invokeDisposableBean,
			boolean nonPublicAccessAllowed, String destroyMethodName,
			List<DestructionAwareBeanPostProcessor> postProcessors) {

		this.bean = bean;
		this.beanName = beanName;
		this.invokeDisposableBean = invokeDisposableBean;
		this.nonPublicAccessAllowed = nonPublicAccessAllowed;
		this.destroyMethodName = destroyMethodName;
		this.beanPostProcessors = postProcessors;
		this.acc = null;
	}


	/**
	 * Search for all DestructionAwareBeanPostProcessors in the List.
	 * @param postProcessors the List to search
	 * @return the filtered List of DestructionAwareBeanPostProcessors
	 */
	private List<DestructionAwareBeanPostProcessor> filterPostProcessors(List<BeanPostProcessor> postProcessors) {
		List<DestructionAwareBeanPostProcessor> filteredPostProcessors = null;
		if (postProcessors != null && !postProcessors.isEmpty()) {
			filteredPostProcessors = new ArrayList<DestructionAwareBeanPostProcessor>(postProcessors.size());
			for (BeanPostProcessor postProcessor : postProcessors) {
				if (postProcessor instanceof DestructionAwareBeanPostProcessor) {
					filteredPostProcessors.add((DestructionAwareBeanPostProcessor) postProcessor);
				}
			}
		}
		return filteredPostProcessors;
	}


	public void run() {
		destroy();
	}

	public void destroy() {
		if (this.beanPostProcessors != null && !this.beanPostProcessors.isEmpty()) {
			for (DestructionAwareBeanPostProcessor processor : this.beanPostProcessors) {
				processor.postProcessBeforeDestruction(this.bean, this.beanName);
			}
		}

		if (this.invokeDisposableBean) {
			if (logger.isDebugEnabled()) {
				logger.debug("Invoking destroy() on bean with name '" + this.beanName + "'");
			}
			try {
				if (System.getSecurityManager() != null) {
					AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
						public Object run() throws Exception {
							((DisposableBean) bean).destroy();
							return null;
						}
					}, acc);
				}
				else {
					((DisposableBean) bean).destroy();
				}
			}
			catch (Throwable ex) {
				String msg = "Invocation of destroy method failed on bean with name '" + this.beanName + "'";
				if (logger.isDebugEnabled()) {
					logger.warn(msg, ex);
				}
				else {
					logger.warn(msg + ": " + ex);
				}
			}
		}

		if (this.destroyMethod != null) {
			invokeCustomDestroyMethod(this.destroyMethod);
		}
		else if (this.destroyMethodName != null) {
			Method methodToCall = determineDestroyMethod();
			if (methodToCall != null) {
				invokeCustomDestroyMethod(methodToCall);
			}
		}
	}


	private Method determineDestroyMethod() {
		try {
			if (System.getSecurityManager() != null) {
				return AccessController.doPrivileged(new PrivilegedAction<Method>() {
					public Method run() {
						return findDestroyMethod();
					}
				});
			}
			else {
				return findDestroyMethod();
			}
		}
		catch (IllegalArgumentException ex) {
			throw new BeanDefinitionValidationException("Couldn't find a unique destroy method on bean with name '" +
					this.beanName + ": " + ex.getMessage());
		}
	}

	private Method findDestroyMethod() {
		return (this.nonPublicAccessAllowed ?
				BeanUtils.findMethodWithMinimalParameters(this.bean.getClass(), this.destroyMethodName) :
				BeanUtils.findMethodWithMinimalParameters(this.bean.getClass().getMethods(), this.destroyMethodName));
	}

	/**
	 * Invoke the specified custom destroy method on the given bean.
	 * <p>This implementation invokes a no-arg method if found, else checking
	 * for a method with a single boolean argument (passing in "true",
	 * assuming a "force" parameter), else logging an error.
	 */
	private void invokeCustomDestroyMethod(final Method destroyMethod) {
		Class<?>[] paramTypes = destroyMethod.getParameterTypes();
		final Object[] args = new Object[paramTypes.length];
		if (paramTypes.length == 1) {
			args[0] = Boolean.TRUE;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Invoking destroy method '" + this.destroyMethodName +
					"' on bean with name '" + this.beanName + "'");
		}
		try {
			if (System.getSecurityManager() != null) {
				AccessController.doPrivileged(new PrivilegedAction<Object>() {
					public Object run() {
						ReflectionUtils.makeAccessible(destroyMethod);
						return null;
					}
				});
				try {
					AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
						public Object run() throws Exception {
							destroyMethod.invoke(bean, args);
							return null;
						}
					}, acc);
				}
				catch (PrivilegedActionException pax) {
					throw (InvocationTargetException) pax.getException();
				}
			}
			else {
				ReflectionUtils.makeAccessible(destroyMethod);
				destroyMethod.invoke(bean, args);
			}
		}
		catch (InvocationTargetException ex) {
			String msg = "Invocation of destroy method '" + this.destroyMethodName +
					"' failed on bean with name '" + this.beanName + "'";
			if (logger.isDebugEnabled()) {
				logger.warn(msg, ex.getTargetException());
			}
			else {
				logger.warn(msg + ": " + ex.getTargetException());
			}
		}
		catch (Throwable ex) {
			logger.error("Couldn't invoke destroy method '" + this.destroyMethodName +
					"' on bean with name '" + this.beanName + "'", ex);
		}
	}


	/**
	 * Serializes a copy of the state of this class,
	 * filtering out non-serializable BeanPostProcessors.
	 */
	protected Object writeReplace() {
		List<DestructionAwareBeanPostProcessor> serializablePostProcessors = null;
		if (this.beanPostProcessors != null) {
			serializablePostProcessors = new ArrayList<DestructionAwareBeanPostProcessor>();
			for (DestructionAwareBeanPostProcessor postProcessor : this.beanPostProcessors) {
				if (postProcessor instanceof Serializable) {
					serializablePostProcessors.add(postProcessor);
				}
			}
		}
		return new DisposableBeanAdapter(this.bean, this.beanName, this.invokeDisposableBean,
				this.nonPublicAccessAllowed, this.destroyMethodName, serializablePostProcessors);
	}

}
