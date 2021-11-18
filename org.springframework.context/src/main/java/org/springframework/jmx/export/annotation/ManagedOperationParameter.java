/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export.annotation;

/**
 * JDK 1.5+ method-level annotation used to provide metadata about operation
 * parameters, corresponding to a <code>ManagedOperationParameter</code> attribute.
 * Used as part of a <code>ManagedOperationParameters</code> annotation.
 *
 * @author Rob Harrop
 * @since 1.2
 * @see ManagedOperationParameters#value
 * @see org.springframework.jmx.export.metadata.ManagedOperationParameter
 */
public @interface ManagedOperationParameter {

	String name();

	String description();

}
