/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * When used as a type-level annotation in conjunction with the {@link Component}
 * annotation, indicates the name of a scope to use for instances of the annotated
 * type.
 * 
 * <p>When used as a method-level annotation in conjunction with the
 * {@link Bean} annotation, indicates the name of a scope to use for
 * the instance returned from the method.
 *
 * <p>In this context, scope means the lifecycle of an instance, such as
 * {@code singleton}, {@code prototype}, and so forth. Scopes provided out of the box in
 * Spring may be referred to using the {@code SCOPE_*} constants available in
 * via {@link ConfigurableBeanFactory} and {@code WebApplicationContext} interfaces.
 *
 * <p>To register additional custom scopes, see
 * {@link org.springframework.beans.factory.config.CustomScopeConfigurer CustomScopeConfigurer}.
 *
 * @author Mark Fisher
 * @author Chris Beams
 * @since 2.5
 * @see Component
 * @see Bean
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {

	/**
	 * Specifies the scope to use for the annotated component/bean.
	 * @see ConfigurableBeanFactory#SCOPE_SINGLETON
	 * @see ConfigurableBeanFactory#SCOPE_PROTOTYPE
	 * @see org.springframework.web.context.WebApplicationContext#SCOPE_REQUEST
	 * @see org.springframework.web.context.WebApplicationContext#SCOPE_SESSION
	 */
	String value() default ConfigurableBeanFactory.SCOPE_SINGLETON;

	/**
	 * Specifies whether a component should be configured as a scoped proxy
	 * and if so, whether the proxy should be interface-based or subclass-based.
	 * <p>Defaults to {@link ScopedProxyMode#NO}, indicating that no scoped
	 * proxy should be created.
	 * <p>Analogous to {@code <aop:scoped-proxy/>} support in Spring XML.
	 */
	ScopedProxyMode proxyMode() default ScopedProxyMode.DEFAULT;

}
