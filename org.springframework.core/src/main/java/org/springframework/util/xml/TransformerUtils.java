/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.util.xml;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;

import org.springframework.util.Assert;

/**
 * Contains common behavior relating to {@link javax.xml.transform.Transformer Transformers}, and the
 * <code>javax.xml.transform</code> package in general.
 *
 * @author Rick Evans
 * @author Juergen Hoeller
 * @since 2.5.5
 */
public abstract class TransformerUtils {

	/**
	 * The indent amount of characters if {@link #enableIndenting(javax.xml.transform.Transformer) indenting is enabled}.
	 * <p>Defaults to "2".
	 */
	public static final int DEFAULT_INDENT_AMOUNT = 2;

	/**
	 * Enable indenting for the supplied {@link javax.xml.transform.Transformer}. <p>If the underlying XSLT engine is
	 * Xalan, then the special output key <code>indent-amount</code> will be also be set to a value of {@link
	 * #DEFAULT_INDENT_AMOUNT} characters.
	 *
	 * @param transformer the target transformer
	 * @see javax.xml.transform.Transformer#setOutputProperty(String, String)
	 * @see javax.xml.transform.OutputKeys#INDENT
	 */
	public static void enableIndenting(Transformer transformer) {
		enableIndenting(transformer, DEFAULT_INDENT_AMOUNT);
	}

	/**
	 * Enable indenting for the supplied {@link javax.xml.transform.Transformer}. <p>If the underlying XSLT engine is
	 * Xalan, then the special output key <code>indent-amount</code> will be also be set to a value of {@link
	 * #DEFAULT_INDENT_AMOUNT} characters.
	 *
	 * @param transformer  the target transformer
	 * @param indentAmount the size of the indent (2 characters, 3 characters, etc.)
	 * @see javax.xml.transform.Transformer#setOutputProperty(String, String)
	 * @see javax.xml.transform.OutputKeys#INDENT
	 */
	public static void enableIndenting(Transformer transformer, int indentAmount) {
		Assert.notNull(transformer, "Transformer must not be null");
		Assert.isTrue(indentAmount > -1, "The indent amount cannot be less than zero : got " + indentAmount);
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		try {
			// Xalan-specific, but this is the most common XSLT engine in any case
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(indentAmount));
		}
		catch (IllegalArgumentException ignored) {
		}
	}

	/**
	 * Disable indenting for the supplied {@link javax.xml.transform.Transformer}.
	 *
	 * @param transformer the target transformer
	 * @see javax.xml.transform.OutputKeys#INDENT
	 */
	public static void disableIndenting(Transformer transformer) {
		Assert.notNull(transformer, "Transformer must not be null");
		transformer.setOutputProperty(OutputKeys.INDENT, "no");
	}

}
