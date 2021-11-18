/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.util.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

import org.springframework.util.Assert;

/**
 * Simple <code>javax.xml.namespace.NamespaceContext</code> implementation. Follows the standard
 * <code>NamespaceContext</code> contract, and is loadable via a <code>java.util.Map</code> or
 * <code>java.util.Properties</code> object
 *
 * @author Arjen Poutsma
 * @since 3.0
 */
public class SimpleNamespaceContext implements NamespaceContext {

	private Map<String, String> prefixToNamespaceUri = new HashMap<String, String>();

	private Map<String, List<String>> namespaceUriToPrefixes = new HashMap<String, List<String>>();

	private String defaultNamespaceUri = "";

	public String getNamespaceURI(String prefix) {
		Assert.notNull(prefix, "prefix is null");
		if (XMLConstants.XML_NS_PREFIX.equals(prefix)) {
			return XMLConstants.XML_NS_URI;
		}
		else if (XMLConstants.XMLNS_ATTRIBUTE.equals(prefix)) {
			return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
		}
		else if (XMLConstants.DEFAULT_NS_PREFIX.equals(prefix)) {
			return defaultNamespaceUri;
		}
		else if (prefixToNamespaceUri.containsKey(prefix)) {
			return prefixToNamespaceUri.get(prefix);
		}
		return "";
	}

	public String getPrefix(String namespaceUri) {
		List prefixes = getPrefixesInternal(namespaceUri);
		return prefixes.isEmpty() ? null : (String) prefixes.get(0);
	}

	public Iterator getPrefixes(String namespaceUri) {
		return getPrefixesInternal(namespaceUri).iterator();
	}

	/**
	 * Sets the bindings for this namespace context. The supplied map must consist of string key value pairs.
	 *
	 * @param bindings the bindings
	 */
	public void setBindings(Map<String, String> bindings) {
		for (Map.Entry<String, String> entry : bindings.entrySet()) {
			bindNamespaceUri(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Binds the given namespace as default namespace.
	 *
	 * @param namespaceUri the namespace uri
	 */
	public void bindDefaultNamespaceUri(String namespaceUri) {
		bindNamespaceUri(XMLConstants.DEFAULT_NS_PREFIX, namespaceUri);
	}

	/**
	 * Binds the given prefix to the given namespace.
	 *
	 * @param prefix	   the namespace prefix
	 * @param namespaceUri the namespace uri
	 */
	public void bindNamespaceUri(String prefix, String namespaceUri) {
		Assert.notNull(prefix, "No prefix given");
		Assert.notNull(namespaceUri, "No namespaceUri given");
		if (XMLConstants.DEFAULT_NS_PREFIX.equals(prefix)) {
			defaultNamespaceUri = namespaceUri;
		}
		else {
			prefixToNamespaceUri.put(prefix, namespaceUri);
			getPrefixesInternal(namespaceUri).add(prefix);
		}
	}

	/** Removes all declared prefixes. */
	public void clear() {
		prefixToNamespaceUri.clear();
	}

	/**
	 * Returns all declared prefixes.
	 *
	 * @return the declared prefixes
	 */
	public Iterator<String> getBoundPrefixes() {
		return prefixToNamespaceUri.keySet().iterator();
	}

	private List<String> getPrefixesInternal(String namespaceUri) {
		if (defaultNamespaceUri.equals(namespaceUri)) {
			return Collections.singletonList(XMLConstants.DEFAULT_NS_PREFIX);
		}
		else if (XMLConstants.XML_NS_URI.equals(namespaceUri)) {
			return Collections.singletonList(XMLConstants.XML_NS_PREFIX);
		}
		else if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(namespaceUri)) {
			return Collections.singletonList(XMLConstants.XMLNS_ATTRIBUTE);
		}
		else {
			List<String> list = namespaceUriToPrefixes.get(namespaceUri);
			if (list == null) {
				list = new ArrayList<String>();
				namespaceUriToPrefixes.put(namespaceUri, list);
			}
			return list;
		}
	}

	/**
	 * Removes the given prefix from this context.
	 *
	 * @param prefix the prefix to be removed
	 */
	public void removeBinding(String prefix) {
		if (XMLConstants.DEFAULT_NS_PREFIX.equals(prefix)) {
			defaultNamespaceUri = "";
		}
		else {
			String namespaceUri = prefixToNamespaceUri.remove(prefix);
			List prefixes = getPrefixesInternal(namespaceUri);
			prefixes.remove(prefix);
		}
	}
}
