/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation4;

import org.springframework.beans.TestBean;
import org.springframework.context.annotation.Bean;

/**
 * Class to test that @FactoryMethods are detected only when inside a class with an @Component
 * class annotation.
 *
 * @author Mark Pollack
 */
public class SimpleBean {

	// This should *not* recognized as a bean since it does not reside inside an @Component
	@Bean
	public TestBean getPublicInstance() {
		return new TestBean("publicInstance");
	}

}
