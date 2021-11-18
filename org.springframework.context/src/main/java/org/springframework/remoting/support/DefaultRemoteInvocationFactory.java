/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.remoting.support;

import org.aopalliance.intercept.MethodInvocation;

/**
 * Default implementation of the {@link RemoteInvocationFactory} interface.
 * Simply creates a new standard {@link RemoteInvocation} object.
 *
 * @author Juergen Hoeller
 * @since 1.1
 */
public class DefaultRemoteInvocationFactory implements RemoteInvocationFactory {

	public RemoteInvocation createRemoteInvocation(MethodInvocation methodInvocation) {
		return new RemoteInvocation(methodInvocation);
	}

}
