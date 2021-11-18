/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.ejb.support;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

/**
 * Interface to be implemented by Session Beans that want
 * to expose important state to cooperating classes.
 *
 * <p>Implemented by Spring's AbstractSessionBean class and hence by
 * all of Spring's specific Session Bean support classes, such as
 * {@link AbstractStatelessSessionBean} and {@link AbstractStatefulSessionBean}.
 *
 * @author Juergen Hoeller
 * @since 1.2
 * @see AbstractStatelessSessionBean
 * @see AbstractStatefulSessionBean
 */
public interface SmartSessionBean extends SessionBean {

	/**
	 * Return the SessionContext that was passed to the Session Bean
	 * by the EJB container. Can be used by cooperating infrastructure
	 * code to get access to the user credentials, for example.
	 */
	SessionContext getSessionContext();

}
