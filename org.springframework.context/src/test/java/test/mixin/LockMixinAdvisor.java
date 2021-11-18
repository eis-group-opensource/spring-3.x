/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package test.mixin;

import org.springframework.aop.support.DefaultIntroductionAdvisor;

/**
 * Advisor for use with a LockMixin. Applies to all classes.
 *
 * @author Rod Johnson
 */
public class LockMixinAdvisor extends DefaultIntroductionAdvisor {
	
	public LockMixinAdvisor() {
		super(new LockMixin(), Lockable.class);
	}

}
