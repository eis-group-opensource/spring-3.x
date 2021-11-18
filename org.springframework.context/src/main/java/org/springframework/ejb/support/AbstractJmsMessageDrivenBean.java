/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.ejb.support;

import javax.jms.MessageListener;

/** 
 * Convenient base class for JMS-based EJB 2.x MDBs. Requires subclasses
 * to implement the JMS <code>javax.jms.MessageListener</code> interface.
 *
 * @author Rod Johnson
 */
public abstract class AbstractJmsMessageDrivenBean extends AbstractMessageDrivenBean implements MessageListener {

	// Empty: The purpose of this class is to ensure
	// that subclasses implement <code>javax.jms.MessageListener</code>.

} 
