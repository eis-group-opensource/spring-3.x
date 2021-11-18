/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.util.xml;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.sax.SAXResult;

import org.xml.sax.ContentHandler;

/**
 * Implementation of the <code>Result</code> tagging interface for StAX writers. Can be constructed with a
 * <code>XMLEventConsumer</code> or a <code>XMLStreamWriter</code>.
 *
 * <p>This class is necessary because there is no implementation of <code>Source</code> for StaxReaders in JAXP 1.3.
 * There is a <code>StAXResult</code> in JAXP 1.4 (JDK 1.6), but this class is kept around for back-ward compatibility
 * reasons.
 *
 * <p>Even though <code>StaxResult</code> extends from <code>SAXResult</code>, calling the methods of
 * <code>SAXResult</code> is <strong>not supported</strong>. In general, the only supported operation on this class is
 * to use the <code>ContentHandler</code> obtained via {@link #getHandler()} to parse an input source using an
 * <code>XMLReader</code>. Calling {@link #setHandler(org.xml.sax.ContentHandler)} will result in
 * <code>UnsupportedOperationException</code>s.
 *
 * @author Arjen Poutsma
 * @see XMLEventWriter
 * @see XMLStreamWriter
 * @see javax.xml.transform.Transformer
 * @since 3.0
 */
class StaxResult extends SAXResult {

	private XMLEventWriter eventWriter;

	private XMLStreamWriter streamWriter;

	/**
	 * Constructs a new instance of the <code>StaxResult</code> with the specified <code>XMLStreamWriter</code>.
	 *
	 * @param streamWriter the <code>XMLStreamWriter</code> to write to
	 */
	StaxResult(XMLStreamWriter streamWriter) {
		super.setHandler(new StaxStreamContentHandler(streamWriter));
		this.streamWriter = streamWriter;
	}

	/**
	 * Constructs a new instance of the <code>StaxResult</code> with the specified <code>XMLEventWriter</code>.
	 *
	 * @param eventWriter the <code>XMLEventWriter</code> to write to
	 */
	StaxResult(XMLEventWriter eventWriter) {
		super.setHandler(new StaxEventContentHandler(eventWriter));
		this.eventWriter = eventWriter;
	}

	/**
	 * Constructs a new instance of the <code>StaxResult</code> with the specified <code>XMLEventWriter</code> and
	 * <code>XMLEventFactory</code>.
	 *
	 * @param eventWriter  the <code>XMLEventWriter</code> to write to
	 * @param eventFactory the <code>XMLEventFactory</code> to use for creating events
	 */
	StaxResult(XMLEventWriter eventWriter, XMLEventFactory eventFactory) {
		super.setHandler(new StaxEventContentHandler(eventWriter, eventFactory));
		this.eventWriter = eventWriter;
	}

	/**
	 * Returns the <code>XMLEventWriter</code> used by this <code>StaxResult</code>. If this <code>StaxResult</code> was
	 * created with an <code>XMLStreamWriter</code>, the result will be <code>null</code>.
	 *
	 * @return the StAX event writer used by this result
	 * @see #StaxResult(javax.xml.stream.XMLEventWriter)
	 */
	XMLEventWriter getXMLEventWriter() {
		return eventWriter;
	}

	/**
	 * Returns the <code>XMLStreamWriter</code> used by this <code>StaxResult</code>. If this <code>StaxResult</code> was
	 * created with an <code>XMLEventConsumer</code>, the result will be <code>null</code>.
	 *
	 * @return the StAX stream writer used by this result
	 * @see #StaxResult(javax.xml.stream.XMLStreamWriter)
	 */
	XMLStreamWriter getXMLStreamWriter() {
		return streamWriter;
	}

	/**
	 * Throws a <code>UnsupportedOperationException</code>.
	 *
	 * @throws UnsupportedOperationException always
	 */
	@Override
	public void setHandler(ContentHandler handler) {
		throw new UnsupportedOperationException("setHandler is not supported");
	}
}
