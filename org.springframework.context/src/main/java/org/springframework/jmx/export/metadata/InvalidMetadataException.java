/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export.metadata;

import org.springframework.jmx.JmxException;

/**
 * Thrown by the <code>JmxAttributeSource</code> when it encounters
 * incorrect metadata on a managed resource or one of its methods.
 *
 * @author Rob Harrop
 * @since 1.2
 * @see JmxAttributeSource
 * @see org.springframework.jmx.export.assembler.MetadataMBeanInfoAssembler
 */
public class InvalidMetadataException extends JmxException {

	/**
	 * Create a new <code>InvalidMetadataException</code> with the supplied
	 * error message.
	 * @param msg the detail message
	 */
	public InvalidMetadataException(String msg) {
		super(msg);
	}

}
