/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.foo;

import java.util.ArrayList;
import java.util.List;

public class Component {
	private String name;
	private List<Component> components = new ArrayList<Component>();

	// mmm, there is no setter method for the 'components'
	public void addComponent(Component component) {
		this.components.add(component);
	}

	public List<Component> getComponents() {
		return components;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
