/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Extended interface for a resource that supports writing to it.
 * Provides an {@link #getOutputStream() OutputStream accessor}.
 *
 * @author Juergen Hoeller
 * @since 3.1
 * @see java.io.OutputStream
 */
public interface WritableResource extends Resource {

	/**
	 * Return whether the contents of this resource can be modified,
	 * e.g. via {@link #getOutputStream()} or {@link #getFile()}.
	 * <p>Will be <code>true</code> for typical resource descriptors;
	 * note that actual content writing may still fail when attempted.
	 * However, a value of <code>false</code> is a definitive indication
	 * that the resource content cannot be modified.
	 * @see #getOutputStream()
	 * @see #isReadable()
	 */
	boolean isWritable();

	/**
	 * Return an {@link OutputStream} for the underlying resource,
	 * allowing to (over-)write its content.
	 * @throws IOException if the stream could not be opened
	 * @see #getInputStream()
	 */
	OutputStream getOutputStream() throws IOException;

}
