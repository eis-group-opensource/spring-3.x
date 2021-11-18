/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.transaction.annotation;

import java.lang.reflect.AnnotatedElement;

import org.springframework.transaction.interceptor.TransactionAttribute;

/**
 * Strategy interface for parsing known transaction annotation types.
 * {@link AnnotationTransactionAttributeSource} delegates to such
 * parsers for supporting specific annotation types such as Spring's own
 * {@link Transactional} or EJB3's {@link javax.ejb.TransactionAttribute}.
 *
 * @author Juergen Hoeller
 * @since 2.5
 * @see AnnotationTransactionAttributeSource
 * @see SpringTransactionAnnotationParser
 * @see Ejb3TransactionAnnotationParser
 */
public interface TransactionAnnotationParser {

	/**
	 * Parse the transaction attribute for the given method or class,
	 * based on a known annotation type.
	 * <p>This essentially parses a known transaction annotation into Spring's
	 * metadata attribute class. Returns <code>null</code> if the method/class
	 * is not transactional.
	 * @param ae the annotated method or class
	 * @return TransactionAttribute the configured transaction attribute,
	 * or <code>null</code> if none was found
	 * @see AnnotationTransactionAttributeSource#determineTransactionAttribute
	 */
	TransactionAttribute parseTransactionAnnotation(AnnotatedElement ae);

}
