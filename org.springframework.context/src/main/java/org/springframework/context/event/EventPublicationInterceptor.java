/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.event;

import java.lang.reflect.Constructor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * {@link MethodInterceptor Interceptor} that publishes an
 * <code>ApplicationEvent</code> to all <code>ApplicationListeners</code>
 * registered with an <code>ApplicationEventPublisher</code> after each
 * <i>successful</i> method invocation.
 *
 * <p>Note that this interceptor is only capable of publishing <i>stateless</i>
 * events configured via the
 * {@link #setApplicationEventClass "applicationEventClass"} property.
 *
 * @author Dmitriy Kopylenko
 * @author Juergen Hoeller
 * @author Rick Evans
 * @see #setApplicationEventClass
 * @see org.springframework.context.ApplicationEvent
 * @see org.springframework.context.ApplicationListener
 * @see org.springframework.context.ApplicationEventPublisher
 * @see org.springframework.context.ApplicationContext
 */
public class EventPublicationInterceptor
		implements MethodInterceptor, ApplicationEventPublisherAware, InitializingBean {

	private Constructor applicationEventClassConstructor;

	private ApplicationEventPublisher applicationEventPublisher;


	/**
	 * Set the application event class to publish.
	 * <p>The event class <b>must</b> have a constructor with a single
	 * <code>Object</code> argument for the event source. The interceptor
	 * will pass in the invoked object.
	 * @throws IllegalArgumentException if the supplied <code>Class</code> is
	 * <code>null</code> or if it is not an <code>ApplicationEvent</code> subclass or
	 * if it does not expose a constructor that takes a single <code>Object</code> argument
	 */
	public void setApplicationEventClass(Class applicationEventClass) {
		if (ApplicationEvent.class.equals(applicationEventClass) ||
			!ApplicationEvent.class.isAssignableFrom(applicationEventClass)) {
			throw new IllegalArgumentException("applicationEventClass needs to extend ApplicationEvent");
		}
		try {
			this.applicationEventClassConstructor =
					applicationEventClass.getConstructor(new Class[] {Object.class});
		}
		catch (NoSuchMethodException ex) {
			throw new IllegalArgumentException("applicationEventClass [" +
					applicationEventClass.getName() + "] does not have the required Object constructor: " + ex);
		}
	}

	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	public void afterPropertiesSet() throws Exception {
		if (this.applicationEventClassConstructor == null) {
			throw new IllegalArgumentException("applicationEventClass is required");
		}
	}


	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object retVal = invocation.proceed();

		ApplicationEvent event = (ApplicationEvent)
				this.applicationEventClassConstructor.newInstance(new Object[] {invocation.getThis()});
		this.applicationEventPublisher.publishEvent(event);

		return retVal;
	}

}
