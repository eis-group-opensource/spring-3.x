/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jca.cci.object;

import java.io.IOException;

import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.InteractionSpec;
import javax.resource.cci.Record;
import javax.resource.cci.RecordFactory;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jca.cci.core.support.CommAreaRecord;

/**
 * EIS operation object for access to COMMAREA records.
 * Subclass of the generic MappingRecordOperation class.
 *
 * @author Thierry Templier
 * @since 1.2
 */
public abstract class MappingCommAreaOperation extends MappingRecordOperation {

	/**
	 * Create a new MappingCommAreaQuery.
	 * @see #setConnectionFactory
	 * @see #setInteractionSpec
	 */
	public MappingCommAreaOperation() {
	}

	/**
	 * Create a new MappingCommAreaQuery.
	 * @param connectionFactory ConnectionFactory to use to obtain connections
	 * @param interactionSpec specification to configure the interaction
	 */
	public MappingCommAreaOperation(ConnectionFactory connectionFactory, InteractionSpec interactionSpec) {
		super(connectionFactory, interactionSpec);
	}


	@Override
	protected final Record createInputRecord(RecordFactory recordFactory, Object inObject) {
		try {
			return new CommAreaRecord(objectToBytes(inObject));
		}
		catch (IOException ex) {
			throw new DataRetrievalFailureException("I/O exception during bytes conversion", ex);
		}
	}

	@Override
	protected final Object extractOutputData(Record record) throws DataAccessException {
		CommAreaRecord commAreaRecord = (CommAreaRecord) record;
		try {
			return bytesToObject(commAreaRecord.toByteArray());
		}
		catch (IOException ex) {
			throw new DataRetrievalFailureException("I/O exception during bytes conversion", ex);
		}
	}


	/**
	 * Method used to convert an object into COMMAREA bytes.
	 * @param inObject the input data
	 * @return the COMMAREA's bytes
	 * @throws IOException if thrown by I/O methods
	 * @throws DataAccessException if conversion failed
	 */
	protected abstract byte[] objectToBytes(Object inObject) throws IOException, DataAccessException;

	/**
	 * Method used to convert the COMMAREA's bytes to an object.
	 * @param bytes the COMMAREA's bytes
	 * @return the output data
	 * @throws IOException if thrown by I/O methods
	 * @throws DataAccessException if conversion failed
	 */
	protected abstract Object bytesToObject(byte[] bytes) throws IOException, DataAccessException;

}
