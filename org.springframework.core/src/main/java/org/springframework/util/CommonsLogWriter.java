/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.util;

import java.io.Writer;

import org.apache.commons.logging.Log;

/**
 * <code>java.io.Writer</code> adapter for a Commons Logging <code>Log</code>.
 *
 * @author Juergen Hoeller
 * @since 2.5.1
 */
public class CommonsLogWriter extends Writer {

	private final Log logger;

	private final StringBuilder buffer = new StringBuilder();


	/**
	 * Create a new CommonsLogWriter for the given Commons Logging logger.
	 * @param logger the Commons Logging logger to write to
	 */
	public CommonsLogWriter(Log logger) {
		Assert.notNull(logger, "Logger must not be null");
		this.logger = logger;
	}


	public void write(char ch) {
		if (ch == '\n' && this.buffer.length() > 0) {
			this.logger.debug(this.buffer.toString());
			this.buffer.setLength(0);
		}
		else {
			this.buffer.append(ch);
		}
	}

	@Override
	public void write(char[] buffer, int offset, int length) {
		for (int i = 0; i < length; i++) {
			char ch = buffer[offset + i];
			if (ch == '\n' && this.buffer.length() > 0) {
				this.logger.debug(this.buffer.toString());
				this.buffer.setLength(0);
			}
			else {
				this.buffer.append(ch);
			}
		}
	}

	@Override
	public void flush() {
	}

	@Override
	public void close() {
	}

}
