/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jca.cci.core;

import java.sql.SQLException;

import javax.resource.ResourceException;
import javax.resource.cci.Record;

import org.springframework.dao.DataAccessException;

/** 
 * Callback interface for extracting a result object from a CCI Record instance.
 *
 * <p>Used for output object creation in CciTemplate. Alternatively, output
 * Records can also be returned to client code as-is. In case of a CCI ResultSet
 * as execution result, you will almost always want to implement a RecordExtractor,
 * to be able to read the ResultSet in a managed fashion, with the CCI Connection
 * still open while reading the ResultSet.
 *
 * <p>Implementations of this interface perform the actual work of extracting
 * results, but don't need to worry about exception handling. ResourceExceptions
 * will be caught and handled correctly by the CciTemplate class.
 *
 * @author Thierry Templier
 * @author Juergen Hoeller
 * @since 1.2
 * @see CciTemplate#execute(javax.resource.cci.InteractionSpec, Record, RecordExtractor)
 * @see CciTemplate#execute(javax.resource.cci.InteractionSpec, RecordCreator, RecordExtractor)
 * @see javax.resource.cci.ResultSet
 */
public interface RecordExtractor<T> {
	
	/** 
	 * Process the data in the given Record, creating a corresponding result object.
	 * @param record the Record to extract data from
	 * (possibly a CCI ResultSet)
	 * @return an arbitrary result object, or <code>null</code> if none
	 * (the extractor will typically be stateful in the latter case)
	 * @throws ResourceException if thrown by a CCI method, to be auto-converted
	 * to a DataAccessException
	 * @throws SQLException if thrown by a ResultSet method, to be auto-converted
	 * to a DataAccessException
	 * @throws DataAccessException in case of custom exceptions
	 * @see javax.resource.cci.ResultSet
	 */
	T extractData(Record record) throws ResourceException, SQLException, DataAccessException;

}
