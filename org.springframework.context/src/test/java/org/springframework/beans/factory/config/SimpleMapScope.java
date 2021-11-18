/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.ObjectFactory;

/**
 * @author Juergen Hoeller
 */
public class SimpleMapScope implements Scope, Serializable {

	private final Map map = new HashMap();

	private final List callbacks = new LinkedList();


	public SimpleMapScope() {
	}

	public final Map getMap() {
		return this.map;
	}


	public Object get(String name, ObjectFactory objectFactory) {
		synchronized (this.map) {
			Object scopedObject = this.map.get(name);
			if (scopedObject == null) {
				scopedObject = objectFactory.getObject();
				this.map.put(name, scopedObject);
			}
			return scopedObject;
		}
	}

	public Object remove(String name) {
		synchronized (this.map) {
			return this.map.remove(name);
		}
	}

	public void registerDestructionCallback(String name, Runnable callback) {
		this.callbacks.add(callback);
	}

	public Object resolveContextualObject(String key) {
		return null;
	}

	public void close() {
		for (Iterator it = this.callbacks.iterator(); it.hasNext();) {
			Runnable runnable = (Runnable) it.next();
			runnable.run();
		}
	}

	public String getConversationId() {
		return null;
	}

}
