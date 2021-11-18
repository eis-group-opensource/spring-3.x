/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

/**
 *
 * Bean post-processors for use in ApplicationContexts to simplify AOP usage
 * by automatically creating AOP proxies without the need to use a ProxyFactoryBean.
 * 
 * <p>The various post-processors in this package need only be added to an ApplicationContext
 * (typically in an XML bean definition document) to automatically proxy selected beans.
 * 
 * <p><b>NB</b>: Automatic auto-proxying is not supported for BeanFactory implementations,
 * as post-processors beans are only automatically detected in application contexts.
 * Post-processors can be explicitly registered on a ConfigurableBeanFactory instead.
 *
 */
package org.springframework.aop.framework.autoproxy;

