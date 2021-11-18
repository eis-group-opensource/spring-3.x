/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.config;

import org.springframework.beans.factory.ObjectFactory;

/**
 * Shared test types for this package.
 * 
 * @author Chris Beams
 */
final class TestTypes {}

/**
 * @author Juergen Hoeller
 */
class NoOpScope implements Scope {

	public Object get(String name, ObjectFactory<?> objectFactory) {
		throw new UnsupportedOperationException();
	}

	public Object remove(String name) {
		throw new UnsupportedOperationException();
	}

	public void registerDestructionCallback(String name, Runnable callback) {
	}

	public Object resolveContextualObject(String key) {
		return null;
	}

	public String getConversationId() {
		return null;
	}

}
