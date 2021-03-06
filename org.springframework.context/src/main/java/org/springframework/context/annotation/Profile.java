/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Indicates that a component is eligible for registration when one or more {@linkplain #value
 * specified profiles} are active.
 *
 * <p>A <em>profile</em> is a named logical grouping that may be activated programatically via
 * {@link ConfigurableEnvironment#setActiveProfiles} or declaratively through setting the
 * {@link AbstractEnvironment#ACTIVE_PROFILES_PROPERTY_NAME spring.profiles.active} property,
 * usually through JVM system properties, as an environment variable, or for web applications
 * as a Servlet context parameter in {@code web.xml}.
 *
 * <p>The {@code @Profile} annotation may be used in any of the following ways:
 * <ul>
 *   <li>as a type-level annotation on any class directly or indirectly annotated with
 *   {@code @Component}, including {@link Configuration @Configuration} classes
 *   <li>as a meta-annotation, for the purpose of composing custom stereotype annotations
 * </ul>
 *
 * <p>If a {@code @Configuration} class is marked with {@code @Profile}, all of the
 * {@code @Bean} methods and {@link Import @Import} annotations associated with that class will
 * be bypassed unless one or more the specified profiles are active.  This is very similar to
 * the behavior in Spring XML: if the {@code profile} attribute of the {@code beans} element is
 * supplied e.g., {@code <beans profile="p1,p2">}, the {@code beans} element will not be parsed unless
 * profiles 'p1' and/or 'p2' have been activated.  Likewise, if a {@code @Component} or
 * {@code @Configuration} class is marked with <code>@Profile({"p1", "p2"})</code>, that class will
 * not be registered/processed unless profiles 'p1' and/or 'p2' have been activated.
 *
 * <p>If the {@code @Profile} annotation is omitted, registration will occur, regardless of which,
 * if any, profiles are active.
 *
 * <p>When defining Spring beans via XML, the {@code "profile"} attribute of the {@code <beans>}
 * element may be used. See the documentation in {@code spring-beans-3.1.xsd} for details.
 *
 * @author Chris Beams
 * @since 3.1
 * @see ConfigurableEnvironment#setActiveProfiles
 * @see ConfigurableEnvironment#setDefaultProfiles
 * @see AbstractEnvironment#ACTIVE_PROFILES_PROPERTY_NAME
 * @see AbstractEnvironment#DEFAULT_PROFILES_PROPERTY_NAME
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Profile {

	/**
	 * The set of profiles for which this component should be registered.
	 */
	String[] value();

}
