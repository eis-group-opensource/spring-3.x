/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
/**
 * Support classes for integrating a JSR-303 Bean Validation provider
 * (such as Hibernate Validator 4.0) into a Spring ApplicationContext
 * and in particular with Spring's data binding and validation APIs.
 *
 * <p>The central class is {@link
 * org.springframework.validation.beanvalidation.LocalValidatorFactoryBean}
 * which defines a shared ValidatorFactory/Validator setup for availability
 * to other Spring components.
 */
package org.springframework.validation.beanvalidation;
