/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.config;

import java.util.Properties;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.InitializingBean;

/**
 * Subclass of PropertyPlaceholderConfigurer that supports JDK 1.4's
 * Preferences API (<code>java.util.prefs</code>).
 *
 * <p>Tries to resolve placeholders as keys first in the user preferences,
 * then in the system preferences, then in this configurer's properties.
 * Thus, behaves like PropertyPlaceholderConfigurer if no corresponding
 * preferences defined.
 *
 * <p>Supports custom paths for the system and user preferences trees. Also
 * supports custom paths specified in placeholders ("myPath/myPlaceholderKey").
 * Uses the respective root node if not specified.
 *
 * @author Juergen Hoeller
 * @since 16.02.2004
 * @see #setSystemTreePath
 * @see #setUserTreePath
 * @see java.util.prefs.Preferences
 */
public class PreferencesPlaceholderConfigurer extends PropertyPlaceholderConfigurer implements InitializingBean {

	private String systemTreePath;

	private String userTreePath;

	private Preferences systemPrefs;

	private Preferences userPrefs;


	/**
	 * Set the path in the system preferences tree to use for resolving
	 * placeholders. Default is the root node.
	 */
	public void setSystemTreePath(String systemTreePath) {
		this.systemTreePath = systemTreePath;
	}

	/**
	 * Set the path in the system preferences tree to use for resolving
	 * placeholders. Default is the root node.
	 */
	public void setUserTreePath(String userTreePath) {
		this.userTreePath = userTreePath;
	}


	/**
	 * This implementation eagerly fetches the Preferences instances
	 * for the required system and user tree nodes.
	 */
	public void afterPropertiesSet() {
		this.systemPrefs = (this.systemTreePath != null) ?
		    Preferences.systemRoot().node(this.systemTreePath) : Preferences.systemRoot();
		this.userPrefs = (this.userTreePath != null) ?
		    Preferences.userRoot().node(this.userTreePath) : Preferences.userRoot();
	}

	/**
	 * This implementation tries to resolve placeholders as keys first
	 * in the user preferences, then in the system preferences, then in
	 * the passed-in properties.
	 */
	@Override
	protected String resolvePlaceholder(String placeholder, Properties props) {
		String path = null;
		String key = placeholder;
		int endOfPath = placeholder.lastIndexOf('/');
		if (endOfPath != -1) {
			path = placeholder.substring(0, endOfPath);
			key = placeholder.substring(endOfPath + 1);
		}
		String value = resolvePlaceholder(path, key, this.userPrefs);
		if (value == null) {
			value = resolvePlaceholder(path, key, this.systemPrefs);
			if (value == null) {
				value = props.getProperty(placeholder);
			}
		}
		return value;
	}

	/**
	 * Resolve the given path and key against the given Preferences.
	 * @param path the preferences path (placeholder part before '/')
	 * @param key the preferences key (placeholder part after '/')
	 * @param preferences the Preferences to resolve against
	 * @return the value for the placeholder, or <code>null</code> if none found
	 */
	protected String resolvePlaceholder(String path, String key, Preferences preferences) {
		if (path != null) {
			 // Do not create the node if it does not exist...
			try {
				if (preferences.nodeExists(path)) {
					return preferences.node(path).get(key, null);
				}
				else {
					return null;
				}
			}
			catch (BackingStoreException ex) {
				throw new BeanDefinitionStoreException("Cannot access specified node path [" + path + "]", ex);
			}
		}
		else {
			return preferences.get(key, null);
		}
	}

}
