/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.util.xml;

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.ProcessingInstruction;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.XMLEvent;

/**
 * Implementation of the {@link javax.xml.stream.XMLStreamReader} interface that wraps a {@link XMLEventReader}. Useful,
 * because the StAX {@link javax.xml.stream.XMLInputFactory} allows one to create a event reader from a stream reader,
 * but not vice-versa.
 *
 * @author Arjen Poutsma
 * @since 3.0
 * @see StaxUtils#createEventStreamReader(javax.xml.stream.XMLEventReader)
 */
class XMLEventStreamReader extends AbstractXMLStreamReader {

	private XMLEvent event;

	private final XMLEventReader eventReader;

	XMLEventStreamReader(XMLEventReader eventReader) throws XMLStreamException {
		this.eventReader = eventReader;
		event = eventReader.nextEvent();
	}

	public boolean isStandalone() {
		if (event.isStartDocument()) {
			return ((StartDocument) event).isStandalone();
		}
		else {
			throw new IllegalStateException();
		}
	}

	public String getVersion() {
		if (event.isStartDocument()) {
			return ((StartDocument) event).getVersion();
		}
		else {
			return null;
		}
	}

	public int getTextStart() {
		return 0;
	}

	public String getText() {
		if (event.isCharacters()) {
			return event.asCharacters().getData();
		}
		else if (event.getEventType() == XMLEvent.COMMENT) {
			return ((Comment) event).getText();
		}
		else {
			throw new IllegalStateException();
		}
	}

	public String getPITarget() {
		if (event.isProcessingInstruction()) {
			return ((ProcessingInstruction) event).getTarget();
		}
		else {
			throw new IllegalStateException();
		}
	}

	public String getPIData() {
		if (event.isProcessingInstruction()) {
			return ((ProcessingInstruction) event).getData();
		}
		else {
			throw new IllegalStateException();
		}
	}

	public int getNamespaceCount() {
		Iterator namespaces;
		if (event.isStartElement()) {
			namespaces = event.asStartElement().getNamespaces();
		}
		else if (event.isEndElement()) {
			namespaces = event.asEndElement().getNamespaces();
		}
		else {
			throw new IllegalStateException();
		}
		return countIterator(namespaces);
	}

	public NamespaceContext getNamespaceContext() {
		if (event.isStartElement()) {
			return event.asStartElement().getNamespaceContext();
		}
		else {
			throw new IllegalStateException();
		}
	}

	public QName getName() {
		if (event.isStartElement()) {
			return event.asStartElement().getName();
		}
		else if (event.isEndElement()) {
			return event.asEndElement().getName();
		}
		else {
			throw new IllegalStateException();
		}
	}

	public Location getLocation() {
		return event.getLocation();
	}

	public int getEventType() {
		return event.getEventType();
	}

	public String getEncoding() {
		return null;
	}

	public String getCharacterEncodingScheme() {
		return null;
	}

	public int getAttributeCount() {
		if (!event.isStartElement()) {
			throw new IllegalStateException();
		}
		Iterator attributes = event.asStartElement().getAttributes();
		return countIterator(attributes);
	}

	public void close() throws XMLStreamException {
		eventReader.close();
	}

	public QName getAttributeName(int index) {
		return getAttribute(index).getName();
	}

	public String getAttributeType(int index) {
		return getAttribute(index).getDTDType();
	}

	public String getAttributeValue(int index) {
		return getAttribute(index).getValue();
	}

	public String getNamespacePrefix(int index) {
		return getNamespace(index).getPrefix();
	}

	public String getNamespaceURI(int index) {
		return getNamespace(index).getNamespaceURI();
	}

	public Object getProperty(String name) throws IllegalArgumentException {
		return eventReader.getProperty(name);
	}

	public boolean isAttributeSpecified(int index) {
		return getAttribute(index).isSpecified();
	}

	public int next() throws XMLStreamException {
		event = eventReader.nextEvent();
		return event.getEventType();
	}

	public boolean standaloneSet() {
		if (event.isStartDocument()) {
			return ((StartDocument) event).standaloneSet();
		}
		else {
			throw new IllegalStateException();
		}
	}

	private int countIterator(Iterator iterator) {
		int count = 0;
		while (iterator.hasNext()) {
			iterator.next();
			count++;
		}
		return count;
	}

	private Attribute getAttribute(int index) {
		if (!event.isStartElement()) {
			throw new IllegalStateException();
		}
		int count = 0;
		Iterator attributes = event.asStartElement().getAttributes();
		while (attributes.hasNext()) {
			Attribute attribute = (Attribute) attributes.next();
			if (count == index) {
				return attribute;
			}
			else {
				count++;
			}
		}
		throw new IllegalArgumentException();
	}

	private Namespace getNamespace(int index) {
		Iterator namespaces;
		if (event.isStartElement()) {
			namespaces = event.asStartElement().getNamespaces();
		}
		else if (event.isEndElement()) {
			namespaces = event.asEndElement().getNamespaces();
		}
		else {
			throw new IllegalStateException();
		}
		int count = 0;
		while (namespaces.hasNext()) {
			Namespace namespace = (Namespace) namespaces.next();
			if (count == index) {
				return namespace;
			}
			else {
				count++;
			}
		}
		throw new IllegalArgumentException();
	}
}
