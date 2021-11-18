/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation.configuration.spr8955;

import org.springframework.stereotype.Component;

/**
 * @author Chris Beams
 * @author Willem Dekker
 */
abstract class Spr8955Parent {

	@Component
	static class Spr8955Child extends Spr8955Parent {

	}

}
