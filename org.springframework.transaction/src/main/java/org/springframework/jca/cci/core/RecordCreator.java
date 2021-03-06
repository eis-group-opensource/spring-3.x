/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jca.cci.core;

import javax.resource.ResourceException;
import javax.resource.cci.Record;
import javax.resource.cci.RecordFactory;

import org.springframework.dao.DataAccessException;

/**
 * Callback interface for creating a CCI Record instance,
 * usually based on the passed-in CCI RecordFactory.
 *
 * <p>Used for input Record creation in CciTemplate. Alternatively,
 * Record instances can be passed into CciTemplate's corresponding
 * <code>execute</code> methods directly, either instantiated manually
 * or created through CciTemplate's Record factory methods.
 *
 * <P>Also used for creating default output Records in CciTemplate.
 * This is useful when the JCA connector needs an explicit output Record
 * instance, but no output Records should be passed into CciTemplate's
 * <code>execute</code> methods.
 *
 * @author Thierry Templier
 * @author Juergen Hoeller
 * @since 1.2
 * @see CciTemplate#execute(javax.resource.cci.InteractionSpec, RecordCreator)
 * @see CciTemplate#execute(javax.resource.cci.InteractionSpec, RecordCreator, RecordExtractor)
 * @see CciTemplate#createIndexedRecord(String)
 * @see CciTemplate#createMappedRecord(String)
 * @see CciTemplate#setOutputRecordCreator(RecordCreator)
 */
public interface RecordCreator {

	/**
	 * Create a CCI Record instance, usually based on the passed-in CCI RecordFactory.
	 * <p>For use as <i>input</i> creator with CciTemplate's <code>execute</code> methods,
	 * this method should create a <i>populated</i> Record instance. For use as
	 * <i>output</i> Record creator, it should return an <i>empty</i> Record instance.
	 * @param recordFactory the CCI RecordFactory (never <code>null</code>, but not guaranteed to be
	 * supported by the connector: its create methods might throw NotSupportedException)
	 * @return the Record instance
	 * @throws ResourceException if thrown by a CCI method, to be auto-converted
	 * to a DataAccessException
	 * @throws DataAccessException in case of custom exceptions
	 */
	Record createRecord(RecordFactory recordFactory) throws ResourceException, DataAccessException;

}
