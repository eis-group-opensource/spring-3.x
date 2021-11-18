/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jca.cci.connection;

import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.cci.IndexedRecord;
import javax.resource.cci.MappedRecord;
import javax.resource.cci.RecordFactory;

/**
 * Implementation of the CCI RecordFactory interface that always throws
 * NotSupportedException.
 *
 * <p>Useful as a placeholder for a RecordFactory argument (for example as
 * defined by the RecordCreator callback), in particular when the connector's
 * <code>ConnectionFactory.getRecordFactory()</code> implementation happens to
 * throw NotSupportedException early rather than throwing the exception from
 * RecordFactory's methods.
 *
 * @author Juergen Hoeller
 * @since 1.2.4
 * @see org.springframework.jca.cci.core.RecordCreator#createRecord(javax.resource.cci.RecordFactory)
 * @see org.springframework.jca.cci.core.CciTemplate#getRecordFactory(javax.resource.cci.ConnectionFactory)
 * @see javax.resource.cci.ConnectionFactory#getRecordFactory()
 * @see javax.resource.NotSupportedException
 */
public class NotSupportedRecordFactory implements RecordFactory {

	public MappedRecord createMappedRecord(String name) throws ResourceException {
		throw new NotSupportedException("The RecordFactory facility is not supported by the connector");
	}

	public IndexedRecord createIndexedRecord(String name) throws ResourceException {
		throw new NotSupportedException("The RecordFactory facility is not supported by the connector");
	}

}
