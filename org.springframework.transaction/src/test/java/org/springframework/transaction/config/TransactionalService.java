/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.transaction.config;

import java.io.Serializable;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author Rob Harrop
 * @author Juergen Hoeller
 */
public class TransactionalService implements Serializable {

	@Transactional("synch")
	public void setSomething(String name) {
	}

	@Transactional("noSynch")
	public void doSomething() {
	}

}
