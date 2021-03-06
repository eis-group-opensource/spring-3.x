/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.validation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

/**
 * Default implementation of the {@link MessageCodesResolver} interface.
 *
 * <p>Will create two message codes for an object error, in the following order:
 * <ul>
 * <li>1.: code + "." + object name
 * <li>2.: code
 * </ul>
 *
 * <p>Will create four message codes for a field specification, in the following order:
 * <ul>
 * <li>1.: code + "." + object name + "." + field
 * <li>2.: code + "." + field
 * <li>3.: code + "." + field type
 * <li>4.: code
 * </ul>
 *
 * <p>For example, in case of code "typeMismatch", object name "user", field "age":
 * <ul>
 * <li>1. try "typeMismatch.user.age"
 * <li>2. try "typeMismatch.age"
 * <li>3. try "typeMismatch.int"
 * <li>4. try "typeMismatch"
 * </ul>
 *
 * <p>This resolution algorithm thus can be leveraged for example to show
 * specific messages for binding errors like "required" and "typeMismatch":
 * <ul>
 * <li>at the object + field level ("age" field, but only on "user");
 * <li>at the field level (all "age" fields, no matter which object name);
 * <li>or at the general level (all fields, on any object).
 * </ul>
 *
 * <p>In case of array, {@link List} or {@link java.util.Map} properties,
 * both codes for specific elements and for the whole collection are
 * generated. Assuming a field "name" of an array "groups" in object "user":
 * <ul>
 * <li>1. try "typeMismatch.user.groups[0].name"
 * <li>2. try "typeMismatch.user.groups.name"
 * <li>3. try "typeMismatch.groups[0].name"
 * <li>4. try "typeMismatch.groups.name"
 * <li>5. try "typeMismatch.name"
 * <li>6. try "typeMismatch.java.lang.String"
 * <li>7. try "typeMismatch"
 * </ul>
 *
 * <p>In order to group all codes into a specific category within your resource bundles,
 * e.g. "validation.typeMismatch.name" instead of the default "typeMismatch.name",
 * consider specifying a {@link #setPrefix prefix} to be applied.
 *
 * @author Juergen Hoeller
 * @since 1.0.1
 */
@SuppressWarnings("serial")
public class DefaultMessageCodesResolver implements MessageCodesResolver, Serializable {

	/**
	 * The separator that this implementation uses when resolving message codes.
	 */
	public static final String CODE_SEPARATOR = ".";


	private String prefix = "";


	/**
	 * Specify a prefix to be applied to any code built by this resolver.
	 * <p>Default is none. Specify, for example, "validation." to get
	 * error codes like "validation.typeMismatch.name".
	 */
	public void setPrefix(String prefix) {
		this.prefix = (prefix != null ? prefix : "");
	}

	/**
	 * Return the prefix to be applied to any code built by this resolver.
	 * <p>Returns an empty String in case of no prefix.
	 */
	protected String getPrefix() {
		return this.prefix;
	}


	public String[] resolveMessageCodes(String errorCode, String objectName) {
		return new String[] {
				postProcessMessageCode(errorCode + CODE_SEPARATOR + objectName),
				postProcessMessageCode(errorCode)};
	}

	/**
	 * Build the code list for the given code and field: an
	 * object/field-specific code, a field-specific code, a plain error code.
	 * <p>Arrays, Lists and Maps are resolved both for specific elements and
	 * the whole collection.
	 * <p>See the {@link DefaultMessageCodesResolver class level Javadoc} for
	 * details on the generated codes.
	 * @return the list of codes
	 */
	public String[] resolveMessageCodes(String errorCode, String objectName, String field, Class<?> fieldType) {
		List<String> codeList = new ArrayList<String>();
		List<String> fieldList = new ArrayList<String>();
		buildFieldList(field, fieldList);
		for (String fieldInList : fieldList) {
			codeList.add(postProcessMessageCode(errorCode + CODE_SEPARATOR + objectName + CODE_SEPARATOR + fieldInList));
		}
		int dotIndex = field.lastIndexOf('.');
		if (dotIndex != -1) {
			buildFieldList(field.substring(dotIndex + 1), fieldList);
		}
		for (String fieldInList : fieldList) {
			codeList.add(postProcessMessageCode(errorCode + CODE_SEPARATOR + fieldInList));
		}
		if (fieldType != null) {
			codeList.add(postProcessMessageCode(errorCode + CODE_SEPARATOR + fieldType.getName()));
		}
		codeList.add(postProcessMessageCode(errorCode));
		return StringUtils.toStringArray(codeList);
	}

	/**
	 * Add both keyed and non-keyed entries for the supplied <code>field</code>
	 * to the supplied field list.
	 */
	protected void buildFieldList(String field, List<String> fieldList) {
		fieldList.add(field);
		String plainField = field;
		int keyIndex = plainField.lastIndexOf('[');
		while (keyIndex != -1) {
			int endKeyIndex = plainField.indexOf(']', keyIndex);
			if (endKeyIndex != -1) {
				plainField = plainField.substring(0, keyIndex) + plainField.substring(endKeyIndex + 1);
				fieldList.add(plainField);
				keyIndex = plainField.lastIndexOf('[');
			}
			else {
				keyIndex = -1;
			}
		}
	}

	/**
	 * Post-process the given message code, built by this resolver.
	 * <p>The default implementation applies the specified prefix, if any.
	 * @param code the message code as built by this resolver
	 * @return the final message code to be returned
	 * @see #setPrefix
	 */
	protected String postProcessMessageCode(String code) {
		return getPrefix() + code;
	}

}
