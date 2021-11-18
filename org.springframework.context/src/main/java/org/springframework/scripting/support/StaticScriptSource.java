/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scripting.support;

import org.springframework.scripting.ScriptSource;
import org.springframework.util.Assert;

/**
 * Static implementation of the
 * {@link org.springframework.scripting.ScriptSource} interface,
 * encapsulating a given String that contains the script source text.
 * Supports programmatic updates of the script String.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 2.0
 */
public class StaticScriptSource implements ScriptSource {

	private String script;

	private boolean modified;

	private String className;


	/**
	 * Create a new StaticScriptSource for the given script.
	 * @param script the script String
	 */
	public StaticScriptSource(String script) {
		setScript(script);
	}

	/**
	 * Create a new StaticScriptSource for the given script.
	 * @param script the script String
	 * @param className the suggested class name for the script
	 * (may be <code>null</code>)
	 */
	public StaticScriptSource(String script, String className) {
		setScript(script);
		this.className = className;
	}

	/**
	 * Set a fresh script String, overriding the previous script.
	 * @param script the script String
	 */
	public synchronized void setScript(String script) {
		Assert.hasText(script, "Script must not be empty");
		this.modified = !script.equals(this.script);
		this.script = script;
	}


	public synchronized String getScriptAsString() {
		this.modified = false;
		return this.script;
	}

	public synchronized boolean isModified() {
		return this.modified;
	}

	public String suggestedClassName() {
		return this.className;
	}


	@Override
	public String toString() {
		return "static script" + (this.className != null ? " [" + this.className + "]" : "");
	}

}
