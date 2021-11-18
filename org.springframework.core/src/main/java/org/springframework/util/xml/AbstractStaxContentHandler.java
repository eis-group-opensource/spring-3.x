/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.util.xml;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * Abstract base class for SAX <code>ContentHandler</code> implementations that use StAX as a basis. All methods
 * delegate to internal template methods, capable of throwing a <code>XMLStreamException</code>. Additionally, an
 * namespace context is used to keep track of declared namespaces.
 *
 * @author Arjen Poutsma
 * @since 3.0
 */
abstract class AbstractStaxContentHandler implements ContentHandler {

	private SimpleNamespaceContext namespaceContext = new SimpleNamespaceContext();

	private boolean namespaceContextChanged = false;

	public final void startDocument() throws SAXException {
		namespaceContext.clear();
		namespaceContextChanged = false;
		try {
			startDocumentInternal();
		}
		catch (XMLStreamException ex) {
			throw new SAXException("Could not handle startDocument: " + ex.getMessage(), ex);
		}
	}

	protected abstract void startDocumentInternal() throws XMLStreamException;

	public final void endDocument() throws SAXException {
		namespaceContext.clear();
		namespaceContextChanged = false;
		try {
			endDocumentInternal();
		}
		catch (XMLStreamException ex) {
			throw new SAXException("Could not handle startDocument: " + ex.getMessage(), ex);
		}
	}

	protected abstract void endDocumentInternal() throws XMLStreamException;

	/**
	 * Binds the given prefix to the given namespaces.
	 *
	 * @see SimpleNamespaceContext#bindNamespaceUri(String,String)
	 */
	public final void startPrefixMapping(String prefix, String uri) {
		namespaceContext.bindNamespaceUri(prefix, uri);
		namespaceContextChanged = true;
	}

	/**
	 * Removes the binding for the given prefix.
	 *
	 * @see SimpleNamespaceContext#removeBinding(String)
	 */
	public final void endPrefixMapping(String prefix) {
		namespaceContext.removeBinding(prefix);
		namespaceContextChanged = true;
	}

	public final void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		try {
			startElementInternal(toQName(uri, qName), atts, namespaceContextChanged ? namespaceContext : null);
			namespaceContextChanged = false;
		}
		catch (XMLStreamException ex) {
			throw new SAXException("Could not handle startElement: " + ex.getMessage(), ex);
		}
	}

	protected abstract void startElementInternal(QName name, Attributes atts, SimpleNamespaceContext namespaceContext)
			throws XMLStreamException;

	public final void endElement(String uri, String localName, String qName) throws SAXException {
		try {
			endElementInternal(toQName(uri, qName), namespaceContextChanged ? namespaceContext : null);
			namespaceContextChanged = false;
		}
		catch (XMLStreamException ex) {
			throw new SAXException("Could not handle endElement: " + ex.getMessage(), ex);
		}
	}

	protected abstract void endElementInternal(QName name, SimpleNamespaceContext namespaceContext)
			throws XMLStreamException;

	public final void characters(char ch[], int start, int length) throws SAXException {
		try {
			charactersInternal(ch, start, length);
		}
		catch (XMLStreamException ex) {
			throw new SAXException("Could not handle characters: " + ex.getMessage(), ex);
		}
	}

	protected abstract void charactersInternal(char[] ch, int start, int length) throws XMLStreamException;

	public final void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		try {
			ignorableWhitespaceInternal(ch, start, length);
		}
		catch (XMLStreamException ex) {
			throw new SAXException("Could not handle ignorableWhitespace:" + ex.getMessage(), ex);
		}
	}

	protected abstract void ignorableWhitespaceInternal(char[] ch, int start, int length) throws XMLStreamException;

	public final void processingInstruction(String target, String data) throws SAXException {
		try {
			processingInstructionInternal(target, data);
		}
		catch (XMLStreamException ex) {
			throw new SAXException("Could not handle processingInstruction: " + ex.getMessage(), ex);
		}
	}

	protected abstract void processingInstructionInternal(String target, String data) throws XMLStreamException;

	public final void skippedEntity(String name) throws SAXException {
		try {
			skippedEntityInternal(name);
		}
		catch (XMLStreamException ex) {
			throw new SAXException("Could not handle skippedEntity: " + ex.getMessage(), ex);
		}
	}

	/**
	 * Convert a namespace URI and DOM or SAX qualified name to a <code>QName</code>. The qualified name can have the form
	 * <code>prefix:localname</code> or <code>localName</code>.
	 *
	 * @param namespaceUri  the namespace URI
	 * @param qualifiedName the qualified name
	 * @return a QName
	 */
	protected QName toQName(String namespaceUri, String qualifiedName) {
		int idx = qualifiedName.indexOf(':');
		if (idx == -1) {
			return new QName(namespaceUri, qualifiedName);
		}
		else {
			String prefix = qualifiedName.substring(0, idx);
			String localPart = qualifiedName.substring(idx + 1);
			return new QName(namespaceUri, localPart, prefix);
		}
	}

	protected abstract void skippedEntityInternal(String name) throws XMLStreamException;
}
