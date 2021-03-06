/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jca.endpoint;

import javax.resource.ResourceException;
import javax.resource.spi.UnavailableException;
import javax.resource.spi.endpoint.MessageEndpoint;
import javax.transaction.xa.XAResource;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DelegatingIntroductionInterceptor;
import org.springframework.util.ReflectionUtils;

/**
 * Generic implementation of the JCA 1.5
 * {@link javax.resource.spi.endpoint.MessageEndpointFactory} interface,
 * providing transaction management capabilities for any kind of message
 * listener object (e.g. {@link javax.jms.MessageListener} objects or
 * {@link javax.resource.cci.MessageListener} objects.
 *
 * <p>Uses AOP proxies for concrete endpoint instances, simply wrapping
 * the specified message listener object and exposing all of its implemented
 * interfaces on the endpoint instance.
 *
 * <p>Typically used with Spring's {@link GenericMessageEndpointManager},
 * but not tied to it. As a consequence, this endpoint factory could
 * also be used with programmatic endpoint management on a native
 * {@link javax.resource.spi.ResourceAdapter} instance.
 *
 * @author Juergen Hoeller
 * @since 2.5
 * @see #setMessageListener
 * @see #setTransactionManager
 * @see GenericMessageEndpointManager
 */
public class GenericMessageEndpointFactory extends AbstractMessageEndpointFactory {

	private Object messageListener;


	/**
	 * Specify the message listener object that the endpoint should expose
	 * (e.g. a {@link javax.jms.MessageListener} objects or
	 * {@link javax.resource.cci.MessageListener} implementation).
	 */
	public void setMessageListener(Object messageListener) {
		this.messageListener = messageListener;
	}

	/**
	 * Wrap each concrete endpoint instance with an AOP proxy,
	 * exposing the message listener's interfaces as well as the
	 * endpoint SPI through an AOP introduction.
	 */
	@Override
	public MessageEndpoint createEndpoint(XAResource xaResource) throws UnavailableException {
		GenericMessageEndpoint endpoint = (GenericMessageEndpoint) super.createEndpoint(xaResource);
		ProxyFactory proxyFactory = new ProxyFactory(this.messageListener);
		DelegatingIntroductionInterceptor introduction = new DelegatingIntroductionInterceptor(endpoint);
		introduction.suppressInterface(MethodInterceptor.class);
		proxyFactory.addAdvice(introduction);
		return (MessageEndpoint) proxyFactory.getProxy();
	}

	/**
	 * Creates a concrete generic message endpoint, internal to this factory.
	 */
	@Override
	protected AbstractMessageEndpoint createEndpointInternal() throws UnavailableException {
		return new GenericMessageEndpoint();
	}


	/**
	 * Private inner class that implements the concrete generic message endpoint,
	 * as an AOP Alliance MethodInterceptor that will be invoked by a proxy.
	 */
	private class GenericMessageEndpoint extends AbstractMessageEndpoint implements MethodInterceptor {

		public Object invoke(MethodInvocation methodInvocation) throws Throwable {
			boolean applyDeliveryCalls = !hasBeforeDeliveryBeenCalled();
			if (applyDeliveryCalls) {
				try {
					beforeDelivery(null);
				}
				catch (ResourceException ex) {
					if (ReflectionUtils.declaresException(methodInvocation.getMethod(), ex.getClass())) {
						throw ex;
					}
					else {
						throw new InternalResourceException(ex);
					}
				}
			}
			try {
				return methodInvocation.proceed();
			}
			catch (Throwable ex) {
				onEndpointException(ex);
				throw ex;
			}
			finally {
				if (applyDeliveryCalls) {
					try {
						afterDelivery();
					}
					catch (ResourceException ex) {
						if (ReflectionUtils.declaresException(methodInvocation.getMethod(), ex.getClass())) {
							throw ex;
						}
						else {
							throw new InternalResourceException(ex);
						}
					}
				}
			}
		}

		@Override
		protected ClassLoader getEndpointClassLoader() {
			return messageListener.getClass().getClassLoader();
		}
	}


	/**
	 * Internal exception thrown when a ResourceExeption has been encountered
	 * during the endpoint invocation.
	 * <p>Will only be used if the ResourceAdapter does not invoke the
	 * endpoint's <code>beforeDelivery</code> and <code>afterDelivery</code>
	 * directly, leavng it up to the concrete endpoint to apply those -
	 * and to handle any ResourceExceptions thrown from them.
	 */
	public static class InternalResourceException extends RuntimeException {

		protected InternalResourceException(ResourceException cause) {
			super(cause);
		}
	}

}
