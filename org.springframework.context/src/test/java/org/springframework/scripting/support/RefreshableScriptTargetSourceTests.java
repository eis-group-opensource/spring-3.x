/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scripting.support;

import junit.framework.TestCase;
import org.easymock.MockControl;

import org.springframework.beans.factory.BeanFactory;

/**
 * @author Rick Evans
 */
public class RefreshableScriptTargetSourceTests extends TestCase {

	public void testCreateWithNullScriptSource() throws Exception {
		MockControl mockFactory = MockControl.createNiceControl(BeanFactory.class);
		mockFactory.replay();
		try {
			new RefreshableScriptTargetSource((BeanFactory) mockFactory.getMock(), "a.bean", null, null, false);
			fail("Must have failed when passed a null ScriptSource.");
		}
		catch (IllegalArgumentException expected) {
		}
		mockFactory.verify();
	}

}
