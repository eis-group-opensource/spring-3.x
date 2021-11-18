/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scripting;

import java.io.IOException;

/**
 * Interface that defines the source of a script.
 * Tracks whether the underlying script has been modified.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 2.0
 */
public interface ScriptSource {

	/**
	 * Retrieve the current script source text as String.
	 * @return the script text
	 * @throws IOException if script retrieval failed
	 */
	String getScriptAsString() throws IOException;

	/**
	 * Indicate whether the underlying script data has been modified since
	 * the last time {@link #getScriptAsString()} was called.
	 * Returns <code>true</code> if the script has not been read yet.
	 * @return whether the script data has been modified
	 */
	boolean isModified();

	/**
	 * Determine a class name for the underlying script.
	 * @return the suggested class name, or <code>null</code> if none available
	 */
	String suggestedClassName();

}
