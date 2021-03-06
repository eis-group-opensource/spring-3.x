/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.validation;

import java.io.Serializable;
import java.util.Map;

import org.springframework.util.Assert;

/**
 * Map-based implementation of the BindingResult interface,
 * supporting registration and evaluation of binding errors on
 * Map attributes.
 *
 * <p>Can be used as errors holder for custom binding onto a
 * Map, for example when invoking a Validator for a Map object.
 *
 * @author Juergen Hoeller
 * @since 2.0
 * @see java.util.Map
 */
@SuppressWarnings("serial")
public class MapBindingResult extends AbstractBindingResult implements Serializable {

	private final Map<?, ?> target;


	/**
	 * Create a new MapBindingResult instance.
	 * @param target the target Map to bind onto
	 * @param objectName the name of the target object
	 */
	public MapBindingResult(Map<?, ?> target, String objectName) {
		super(objectName);
		Assert.notNull(target, "Target Map must not be null");
		this.target = target;
	}


	public final Map<?, ?> getTargetMap() {
		return this.target;
	}

	@Override
	public final Object getTarget() {
		return this.target;
	}

	@Override
	protected Object getActualFieldValue(String field) {
		return this.target.get(field);
	}

}
