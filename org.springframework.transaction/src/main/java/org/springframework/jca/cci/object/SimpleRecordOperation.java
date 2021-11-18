/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jca.cci.object;

import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.InteractionSpec;
import javax.resource.cci.Record;

import org.springframework.dao.DataAccessException;

/**
 * EIS operation object that accepts a passed-in CCI input Record
 * and returns a corresponding CCI output Record.
 *
 * @author Juergen Hoeller
 * @since 1.2
 */
public class SimpleRecordOperation extends EisOperation {

	/**
	 * Constructor that allows use as a JavaBean.
	 */
	public SimpleRecordOperation() {
	}

	/**
	 * Convenient constructor with ConnectionFactory and specifications
	 * (connection and interaction).
	 * @param connectionFactory ConnectionFactory to use to obtain connections
	 */
	public SimpleRecordOperation(ConnectionFactory connectionFactory, InteractionSpec interactionSpec) {
		getCciTemplate().setConnectionFactory(connectionFactory);
		setInteractionSpec(interactionSpec);
	}

	/**
	 * Execute the CCI interaction encapsulated by this operation object.
	 * <p>This method will call CCI's <code>Interaction.execute</code> variant
	 * that returns an output Record.
	 * @param inputRecord the input record
	 * @return the output record
	 * @throws DataAccessException if there is any problem
	 * @see javax.resource.cci.Interaction#execute(javax.resource.cci.InteractionSpec, Record)
	 */
	public Record execute(Record inputRecord) throws DataAccessException {
		return getCciTemplate().execute(getInteractionSpec(), inputRecord);
	}

	/**
	 * Execute the CCI interaction encapsulated by this operation object.
	 * <p>This method will call CCI's <code>Interaction.execute</code> variant
	 * with a passed-in output Record.
	 * @param inputRecord the input record
	 * @param outputRecord the output record
	 * @throws DataAccessException if there is any problem
	 * @see javax.resource.cci.Interaction#execute(javax.resource.cci.InteractionSpec, Record, Record)
	 */
	public void execute(Record inputRecord, Record outputRecord) throws DataAccessException {
		getCciTemplate().execute(getInteractionSpec(), inputRecord, outputRecord);
	}

}
