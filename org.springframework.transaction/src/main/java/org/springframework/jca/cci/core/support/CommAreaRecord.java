/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jca.cci.core.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.resource.cci.Record;
import javax.resource.cci.Streamable;

import org.springframework.util.FileCopyUtils;

/**
 * CCI Record implementation for a COMMAREA, holding a byte array.
 *
 * @author Thierry Templier
 * @author Juergen Hoeller
 * @since 1.2
 * @see org.springframework.jca.cci.object.MappingCommAreaOperation
 */
public class CommAreaRecord implements Record, Streamable {

	private byte[] bytes;

	private String recordName;

	private String recordShortDescription;


	/**
	 * Create a new CommAreaRecord.
	 * @see #read(java.io.InputStream)
	 */
	public CommAreaRecord() {
	}

	/**
	 * Create a new CommAreaRecord.
	 * @param bytes the bytes to fill the record with
	 */
	public CommAreaRecord(byte[] bytes) {
		this.bytes = bytes;
	}


	public void setRecordName(String recordName) {
		this.recordName=recordName;
	}

	public String getRecordName() {
		return recordName;
	}

	public void setRecordShortDescription(String recordShortDescription) {
		this.recordShortDescription=recordShortDescription;
	}

	public String getRecordShortDescription() {
		return recordShortDescription;
	}


	public void read(InputStream in) throws IOException {
		this.bytes = FileCopyUtils.copyToByteArray(in);
	}

	public void write(OutputStream out) throws IOException {
		out.write(this.bytes);
		out.flush();
	}

	public byte[] toByteArray() {
		return this.bytes;
	}


	@Override
	public Object clone() {
		return new CommAreaRecord(this.bytes);
	}

}
