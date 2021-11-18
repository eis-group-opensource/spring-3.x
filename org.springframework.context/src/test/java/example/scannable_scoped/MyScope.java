/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package example.scannable_scoped;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ScopedProxyMode;

public @interface MyScope {
	String value() default BeanDefinition.SCOPE_SINGLETON;
	ScopedProxyMode proxyMode() default ScopedProxyMode.DEFAULT;
}