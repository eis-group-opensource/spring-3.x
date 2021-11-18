/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scripting.support;

import org.springframework.scripting.ConfigurableMessenger;

/**
 * @author Rick Evans
 */
public final class StubMessenger implements ConfigurableMessenger {

	private String message = "I used to be smart... now I'm just stupid.";

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
