/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

/**
 *
 * <p>Base classes to make implementing EJB 2.x beans simpler and less error-prone,
 * as well as guaranteeing a Spring BeanFactory is available to such EJBs.
 * This promotes good practice, with EJB services used for transaction
 * management, thread management, and (possibly) remoting, while
 * business logic is implemented in easily testable POJOs.</p>
 * 
 * <p>In this model, the EJB is a facade, with as many POJO helpers
 * behind the BeanFactory as required.</p>
 * 
 * <p>Note that the default behavior is to look for an EJB enviroment variable
 * with name <code>ejb/BeanFactoryPath</code> that specifies the
 * location <i>on the classpath</i> of an XML bean factory definition
 * file (such as <code>/com/mycom/mypackage/mybeans.xml</code>).
 * If this JNDI key is missing, your EJB subclass won't successfully
 * initialize in the container.</p>
 * 
 * <p><b>Check out the <code>org.springframework.ejb.interceptor</code>
 * package for equivalent support for the EJB 3 component model</b>,
 * providing annotation-based autowiring using an EJB 3 interceptor.</p>
 *
 */
package org.springframework.ejb.support;

