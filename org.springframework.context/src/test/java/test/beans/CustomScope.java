/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package test.beans;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

/**
 * Simple scope implementation which creates object based on a flag.
 *
 * @author  Costin Leau
 * @author  Chris Beams
 */
public class CustomScope implements Scope {

	public boolean createNewScope = true;

	private Map<String, Object> beans = new HashMap<String, Object>();

	public Object get(String name, ObjectFactory<?> objectFactory) {
		if (createNewScope) {
			beans.clear();
			// reset the flag back
			createNewScope = false;
		}

		Object bean = beans.get(name);
		// if a new object is requested or none exists under the current
		// name, create one
		if (bean == null) {
			beans.put(name, objectFactory.getObject());
		}

		return beans.get(name);
	}

	public String getConversationId() {
		return null;
	}

	public void registerDestructionCallback(String name, Runnable callback) {
		// do nothing
	}

	public Object remove(String name) {
		return beans.remove(name);
	}

	public Object resolveContextualObject(String key) {
		return null;
	}

}
