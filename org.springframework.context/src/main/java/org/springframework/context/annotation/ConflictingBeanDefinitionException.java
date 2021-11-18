/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation;

/**
 * Marker subclass of {@link IllegalStateException}, allowing for explicit
 * catch clauses in calling code.
 *
 * @author Chris Beams
 * @since 3.1
 */
@SuppressWarnings("serial")
class ConflictingBeanDefinitionException extends IllegalStateException {

	public ConflictingBeanDefinitionException(String message) {
		super(message);
	}

}
