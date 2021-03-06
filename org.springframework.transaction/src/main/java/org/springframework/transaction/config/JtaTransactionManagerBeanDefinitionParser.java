/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.transaction.config;

import org.w3c.dom.Element;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.ClassUtils;

/**
 * Parser for the &lt;tx:jta-transaction-manager/&gt; element,
 * autodetecting BEA WebLogic, IBM WebSphere and Oracle OC4J.
 *
 * @author Juergen Hoeller
 * @author Christian Dupuis
 * @since 2.5
 */
public class JtaTransactionManagerBeanDefinitionParser extends AbstractSingleBeanDefinitionParser  {

	private static final String WEBLOGIC_JTA_TRANSACTION_MANAGER_CLASS_NAME =
			"org.springframework.transaction.jta.WebLogicJtaTransactionManager";

	private static final String WEBSPHERE_TRANSACTION_MANAGER_CLASS_NAME =
			"org.springframework.transaction.jta.WebSphereUowTransactionManager";

	private static final String OC4J_TRANSACTION_MANAGER_CLASS_NAME =
			"org.springframework.transaction.jta.OC4JJtaTransactionManager";

	private static final String JTA_TRANSACTION_MANAGER_CLASS_NAME =
			"org.springframework.transaction.jta.JtaTransactionManager";


	private static final boolean weblogicPresent = ClassUtils.isPresent(
			"weblogic.transaction.UserTransaction", JtaTransactionManagerBeanDefinitionParser.class.getClassLoader());

	private static final boolean webspherePresent = ClassUtils.isPresent(
			"com.ibm.wsspi.uow.UOWManager", JtaTransactionManagerBeanDefinitionParser.class.getClassLoader());

	private static final boolean oc4jPresent = ClassUtils.isPresent(
			"oracle.j2ee.transaction.OC4JTransactionManager", JtaTransactionManagerBeanDefinitionParser.class.getClassLoader());


	@Override
	protected String getBeanClassName(Element element) {
		if (weblogicPresent) {
			return WEBLOGIC_JTA_TRANSACTION_MANAGER_CLASS_NAME;
		}
		else if (webspherePresent) {
			return WEBSPHERE_TRANSACTION_MANAGER_CLASS_NAME;
		}
		else if (oc4jPresent) {
			return OC4J_TRANSACTION_MANAGER_CLASS_NAME;
		}
		else {
			return JTA_TRANSACTION_MANAGER_CLASS_NAME;
		}
	}

	@Override
	protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) {
		return TxNamespaceHandler.DEFAULT_TRANSACTION_MANAGER_BEAN_NAME;
	}

}
