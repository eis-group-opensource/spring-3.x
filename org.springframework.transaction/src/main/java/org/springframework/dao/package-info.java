/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

/**
 *
 * Exception hierarchy enabling sophisticated error handling independent
 * of the data access approach in use. For example, when DAOs and data
 * access frameworks use the exceptions in this package (and custom
 * subclasses), calling code can detect and handle common problems such
 * as deadlocks without being tied to a particular data access strategy,
 * such as JDBC.
 * 
 * <p>All these exceptions are unchecked, meaning that calling code can
 * leave them uncaught and treat all data access exceptions as fatal.
 * 
 * <p>The classes in this package are discussed in Chapter 9 of
 * <a href="http://www.amazon.com/exec/obidos/tg/detail/-/0764543857/">Expert One-On-One J2EE Design and Development</a>
 * by Rod Johnson (Wrox, 2002).
 *
 */
package org.springframework.dao;

