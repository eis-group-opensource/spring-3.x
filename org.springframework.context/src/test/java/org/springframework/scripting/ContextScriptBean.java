/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scripting;

import org.springframework.beans.TestBean;
import org.springframework.context.ApplicationContext;

/**
 * @author Juergen Hoeller
 * @since 08.08.2006
 */
public interface ContextScriptBean extends ScriptBean {

	TestBean getTestBean();

	ApplicationContext getApplicationContext();

}
