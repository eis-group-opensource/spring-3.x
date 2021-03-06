/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jca.cci.core;

import javax.resource.cci.InteractionSpec;
import javax.resource.cci.Record;

import org.springframework.dao.DataAccessException;

/**
 * Interface that specifies a basic set of CCI operations on an EIS.
 * Implemented by CciTemplate. Not often used, but a useful option
 * to enhance testability, as it can easily be mocked or stubbed.
 *
 * <p>Alternatively, the standard CCI infrastructure can be mocked.
 * However, mocking this interface constitutes significantly less work.
 *
 * @author Juergen Hoeller
 * @since 1.2
 * @see CciTemplate
 */
public interface CciOperations {

	/**
	 * Execute a request on an EIS with CCI, implemented as callback action
	 * working on a CCI Connection. This allows for implementing arbitrary
	 * data access operations, within Spring's managed CCI environment:
	 * that is, participating in Spring-managed transactions and converting
	 * JCA ResourceExceptions into Spring's DataAccessException hierarchy.
	 * <p>The callback action can return a result object, for example a
	 * domain object or a collection of domain objects.
	 * @param action the callback object that specifies the action
	 * @return the result object returned by the action, if any
	 * @throws DataAccessException if there is any problem
	 */
	<T> T execute(ConnectionCallback<T> action) throws DataAccessException;

	/**
	 * Execute a request on an EIS with CCI, implemented as callback action
	 * working on a CCI Interaction. This allows for implementing arbitrary
	 * data access operations on a single Interaction, within Spring's managed
	 * CCI environment: that is, participating in Spring-managed transactions
	 * and converting JCA ResourceExceptions into Spring's DataAccessException
	 * hierarchy.
	 * <p>The callback action can return a result object, for example a
	 * domain object or a collection of domain objects.
	 * @param action the callback object that specifies the action
	 * @return the result object returned by the action, if any
	 * @throws DataAccessException if there is any problem
	 */
	<T> T execute(InteractionCallback<T> action) throws DataAccessException;

	/**
	 * Execute the specified interaction on an EIS with CCI.
	 * @param spec the CCI InteractionSpec instance that defines
	 * the interaction (connector-specific)
	 * @param inputRecord the input record
	 * @return the output record
	 * @throws DataAccessException if there is any problem
	 */
	Record execute(InteractionSpec spec, Record inputRecord) throws DataAccessException;

	/**
	 * Execute the specified interaction on an EIS with CCI.
	 * @param spec the CCI InteractionSpec instance that defines
	 * the interaction (connector-specific)
	 * @param inputRecord the input record
	 * @param outputRecord the output record
	 * @throws DataAccessException if there is any problem
	 */
	void execute(InteractionSpec spec, Record inputRecord, Record outputRecord) throws DataAccessException;

	/**
	 * Execute the specified interaction on an EIS with CCI.
	 * @param spec the CCI InteractionSpec instance that defines
	 * the interaction (connector-specific)
	 * @param inputCreator object that creates the input record to use
	 * @return the output record
	 * @throws DataAccessException if there is any problem
	 */
	Record execute(InteractionSpec spec, RecordCreator inputCreator) throws DataAccessException;

	/**
	 * Execute the specified interaction on an EIS with CCI.
	 * @param spec the CCI InteractionSpec instance that defines
	 * the interaction (connector-specific)
	 * @param inputRecord the input record
	 * @param outputExtractor object to convert the output record to a result object
	 * @return the output data extracted with the RecordExtractor object
	 * @throws DataAccessException if there is any problem
	 */
	<T> T execute(InteractionSpec spec, Record inputRecord, RecordExtractor<T> outputExtractor)
			throws DataAccessException;

	/**
	 * Execute the specified interaction on an EIS with CCI.
	 * @param spec the CCI InteractionSpec instance that defines
	 * the interaction (connector-specific)
	 * @param inputCreator object that creates the input record to use
	 * @param outputExtractor object to convert the output record to a result object
	 * @return the output data extracted with the RecordExtractor object
	 * @throws DataAccessException if there is any problem
	 */
	<T> T execute(InteractionSpec spec, RecordCreator inputCreator, RecordExtractor<T> outputExtractor)
			throws DataAccessException;

}
