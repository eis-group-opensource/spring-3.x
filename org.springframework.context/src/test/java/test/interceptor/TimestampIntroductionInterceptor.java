/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package test.interceptor;

import org.springframework.aop.support.DelegatingIntroductionInterceptor;

import test.util.TimeStamped;

public class TimestampIntroductionInterceptor extends DelegatingIntroductionInterceptor
	implements TimeStamped {

	private long ts;

	public TimestampIntroductionInterceptor() {
	}

	public TimestampIntroductionInterceptor(long ts) {
		this.ts = ts;
	}
	
	public void setTime(long ts) {
		this.ts = ts;
	}

	public long getTimeStamp() {
		return ts;
	}

}
