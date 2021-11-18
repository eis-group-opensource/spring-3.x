/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.propertyeditors;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * {@link java.beans.PropertyEditor} implementation for
 * {@link java.util.ResourceBundle ResourceBundles}.
 * 
 * <p>Only supports conversion <i>from</i> a String, but not
 * <i>to</i> a String.
 * 
 * Find below some examples of using this class in a 
 * (properly configured) Spring container using XML-based metadata:
 * 
 * <pre class="code"> &lt;bean id="errorDialog" class="..."&gt;
 *    &lt;!--
 *        the 'messages' property is of type java.util.ResourceBundle.
 *        the 'DialogMessages.properties' file exists at the root of the CLASSPATH
 *    --&gt;
 *    &lt;property name="messages" value="DialogMessages"/&gt;
 * &lt;/bean&gt;</pre>
 * 
 * <pre class="code"> &lt;bean id="errorDialog" class="..."&gt;
 *    &lt;!--
 *        the 'DialogMessages.properties' file exists in the 'com/messages' package
 *    --&gt;
 *    &lt;property name="messages" value="com/messages/DialogMessages"/&gt;
 * &lt;/bean&gt;</pre>
 * 
 * <p>A 'properly configured' Spring {@link org.springframework.context.ApplicationContext container}
 * might contain a {@link org.springframework.beans.factory.config.CustomEditorConfigurer}
 * definition such that the conversion can be effected transparently:
 * 
 * <pre class="code"> &lt;bean class="org.springframework.beans.factory.config.CustomEditorConfigurer"&gt;
 *    &lt;property name="customEditors"&gt;
 *        &lt;map&gt;
 *            &lt;entry key="java.util.ResourceBundle"&gt;
 *                &lt;bean class="org.springframework.beans.propertyeditors.ResourceBundleEditor"/&gt;
 *            &lt;/entry&gt;
 *        &lt;/map&gt;
 *    &lt;/property&gt;
 * &lt;/bean&gt;</pre>
 * 
 * <p>Please note that this {@link java.beans.PropertyEditor} is
 * <b>not</b> registered by default with any of the Spring infrastructure.
 * 
 * <p>Thanks to David Leal Valmana for the suggestion and initial prototype.
 *
 * @author Rick Evans
 * @since 2.0
 */
public class ResourceBundleEditor extends PropertyEditorSupport {

	/**
	 * The separator used to distinguish between the base name and the
	 * locale (if any) when {@link #setAsText(String) converting from a String}.
	 */
	public static final String BASE_NAME_SEPARATOR = "_";


	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		Assert.hasText(text, "'text' must not be empty");
		ResourceBundle bundle;
		String rawBaseName = text.trim();
		int indexOfBaseNameSeparator = rawBaseName.indexOf(BASE_NAME_SEPARATOR);
		if (indexOfBaseNameSeparator == -1) {
			bundle = ResourceBundle.getBundle(rawBaseName);
		} else {
			// it potentially has locale information
			String baseName = rawBaseName.substring(0, indexOfBaseNameSeparator);
			if (!StringUtils.hasText(baseName)) {
				throw new IllegalArgumentException("Bad ResourceBundle name : received '" + text + "' as argument to 'setAsText(String value)'.");
			}
			String localeString = rawBaseName.substring(indexOfBaseNameSeparator + 1);
			Locale locale = StringUtils.parseLocaleString(localeString);
			bundle = (StringUtils.hasText(localeString))
					? ResourceBundle.getBundle(baseName, locale)
					: ResourceBundle.getBundle(baseName);
		}
		setValue(bundle);
	}

}
