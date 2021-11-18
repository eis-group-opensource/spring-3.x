/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

/**
 *
 * AspectJ integration package. Includes Spring AOP advice implementations for AspectJ 5
 * annotation-style methods, and an AspectJExpressionPointcut: a Spring AOP Pointcut
 * implementation that allows use of the AspectJ pointcut expression language with the Spring AOP
 * runtime framework.
 * 
 * <p>Note that use of this package does <i>not</i> require the use of the <code>ajc</code> compiler
 * or AspectJ load-time weaver. It is intended to enable the use of a valuable subset of AspectJ
 * functionality, with consistent semantics, with the proxy-based Spring AOP framework.
 *
 */
package org.springframework.aop.aspectj;

