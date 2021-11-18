/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

/**
 *
 * Core Spring AOP interfaces, built on AOP Alliance AOP interoperability interfaces.
 * 
 * <br>Any AOP Alliance MethodInterceptor is usable in Spring.
 * 
 * <br>Spring AOP also offers:
 * <ul>
 * 	<li>Introduction support
 * 	<li>A Pointcut abstraction, supporting "static" pointcuts
 * 		(class and method-based) and "dynamic" pointcuts (also considering method arguments).
 * 		There are currently no AOP Alliance interfaces for pointcuts.
 * 	<li>A full range of advice types, including around, before, after returning and throws advice.
 * 	<li>Extensibility allowing arbitrary custom advice types to
 * 		be plugged in without modifying the core framework.
 * </ul>
 * 
 * <br>
 * Spring AOP can be used programmatically or (preferably)
 * integrated with the Spring IoC container.
 *
 */
package org.springframework.aop;

