/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.xml;

import java.util.Collection;

/**
 * Bean that exposes a simple property that can be set
 * to a mix of references and individual values.
 *
 * @author Rod Johnson
 * @since 27.05.2003
 */
public class MixedCollectionBean {

	private Collection jumble;


	public void setJumble(Collection jumble) {
		this.jumble = jumble;
	}

	public Collection getJumble() {
		return jumble;
	}

}
