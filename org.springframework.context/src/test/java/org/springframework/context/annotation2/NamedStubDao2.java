/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation2;

import org.springframework.stereotype.Repository;

/**
 * @author Juergen Hoeller
 */
@Repository("myNamedDao")
public class NamedStubDao2 {

	public String find(int id) {
		return "bar";
	}

}
