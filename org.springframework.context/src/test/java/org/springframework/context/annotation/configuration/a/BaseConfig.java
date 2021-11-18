/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation.configuration.a;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.configuration.PackagePrivateBeanMethodInheritanceTests.Bar;

public abstract class BaseConfig {

	// ---- reproduce ----
	@Bean
	Bar packagePrivateBar() {
		return new Bar();
	}

	public Bar reproBar() {
		return packagePrivateBar();
	}

	// ---- workaround ----
	@Bean
	protected Bar protectedBar() {
		return new Bar();
	}

	public Bar workaroundBar() {
		return protectedBar();
	}
}