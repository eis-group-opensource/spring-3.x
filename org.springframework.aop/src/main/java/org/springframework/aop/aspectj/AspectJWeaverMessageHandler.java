/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.aspectj;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.IMessage.Kind;
import org.aspectj.bridge.IMessageHandler;

/**
 * Implementation of AspectJ's {@link IMessageHandler} interface that
 * routes AspectJ weaving messages through the same logging system as the
 * regular Spring messages.
 * 
 * <p>Pass the option...
 *
 * <p><code class="code">-XmessageHandlerClass:org.springframework.aop.aspectj.AspectJWeaverMessageHandler</code>
 *
 * <p>to the weaver; for example, specifying the following in a
 * "<code>META-INF/aop.xml</code> file:
 *
 * <p><code class="code">&lt;weaver options="..."/&gt;</code>
 *
 * @author Adrian Colyer
 * @author Juergen Hoeller
 * @since 2.0
 */
public class AspectJWeaverMessageHandler implements IMessageHandler {

	private static final String AJ_ID = "[AspectJ] ";
	
	private static final Log LOGGER = LogFactory.getLog("AspectJ Weaver");
	

	public boolean handleMessage(IMessage message) throws AbortException {
		Kind messageKind = message.getKind();

		if (LOGGER.isDebugEnabled() || LOGGER.isTraceEnabled()) {
			if (messageKind == IMessage.DEBUG) {
				LOGGER.debug(makeMessageFor(message));
				return true;
			}
		} 
		
		if (LOGGER.isInfoEnabled()) {
			if ((messageKind == IMessage.INFO) || (messageKind == IMessage.WEAVEINFO)) {
				LOGGER.info(makeMessageFor(message));
				return true;
			}
		} 
		
		if (LOGGER.isWarnEnabled()) {
			if (messageKind == IMessage.WARNING) {
				LOGGER.warn(makeMessageFor(message));
				return true;
			}
		}
		
		if (LOGGER.isErrorEnabled()) {
			if (messageKind == IMessage.ERROR) {
				LOGGER.error(makeMessageFor(message));
				return true;
			}
		}
		
		if (LOGGER.isFatalEnabled()) {
			if (messageKind == IMessage.ABORT) {
				LOGGER.fatal(makeMessageFor(message));
				return true;
			}
		}
		
		return false;
	}
	
	private String makeMessageFor(IMessage aMessage) {
		return AJ_ID + aMessage.getMessage();
	}

	public boolean isIgnoring(Kind messageKind) {
		// We want to see everything, and allow configuration of log levels dynamically.
		return false;
	}

	public void dontIgnore(Kind messageKind) {
		// We weren't ignoring anything anyway...
	}

	public void ignore(Kind kind) {
		// We weren't ignoring anything anyway...
	}

}
