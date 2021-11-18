/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.validation;

import java.util.Map;

import org.springframework.util.Assert;

/**
 * Convenience methods for looking up BindingResults in a model Map.
 *
 * @author Juergen Hoeller
 * @since 2.0
 * @see BindingResult#MODEL_KEY_PREFIX
 */
public abstract class BindingResultUtils {

	/**
	 * Find the BindingResult for the given name in the given model.
	 * @param model the model to search
	 * @param name the name of the target object to find a BindingResult for
	 * @return the BindingResult, or <code>null</code> if none found
	 * @throws IllegalStateException if the attribute found is not of type BindingResult
	 */
	public static BindingResult getBindingResult(Map<?, ?> model, String name) {
		Assert.notNull(model, "Model map must not be null");
		Assert.notNull(name, "Name must not be null");
		Object attr = model.get(BindingResult.MODEL_KEY_PREFIX + name);
		if (attr != null && !(attr instanceof BindingResult)) {
			throw new IllegalStateException("BindingResult attribute is not of type BindingResult: " + attr);
		}
		return (BindingResult) attr;
	}

	/**
	 * Find a required BindingResult for the given name in the given model.
	 * @param model the model to search
	 * @param name the name of the target object to find a BindingResult for
	 * @return the BindingResult (never <code>null</code>)
	 * @throws IllegalStateException if no BindingResult found
	 */
	public static BindingResult getRequiredBindingResult(Map<?, ?> model, String name) {
		BindingResult bindingResult = getBindingResult(model, name);
		if (bindingResult == null) {
			throw new IllegalStateException("No BindingResult attribute found for name '" + name +
					"'- have you exposed the correct model?");
		}
		return bindingResult;
	}

}
