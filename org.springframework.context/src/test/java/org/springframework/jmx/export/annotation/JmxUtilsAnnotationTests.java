/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export.annotation;

import javax.management.MXBean;

import junit.framework.TestCase;

import org.springframework.core.JdkVersion;
import org.springframework.jmx.support.JmxUtils;

/**
 * @author Juergen Hoeller
 */
public class JmxUtilsAnnotationTests extends TestCase {

	public void testNotMXBean() throws Exception {
		if (JdkVersion.getMajorJavaVersion() < JdkVersion.JAVA_16) {
			return;
		}
		FooNotX foo = new FooNotX();
		assertFalse("MXBean annotation not detected correctly", JmxUtils.isMBean(foo.getClass()));
	}

	public void testAnnotatedMXBean() throws Exception {
		if (JdkVersion.getMajorJavaVersion() < JdkVersion.JAVA_16) {
			return;
		}
		FooX foo = new FooX();
		assertTrue("MXBean annotation not detected correctly", JmxUtils.isMBean(foo.getClass()));
	}


	@MXBean(false)
	public static interface FooNotMXBean {

		String getName();
	}


	public static class FooNotX implements FooNotMXBean {

		public String getName() {
			return "Rob Harrop";
		}
	}


	@MXBean(true)
	public static interface FooIfc {

		String getName();
	}


	public static class FooX implements FooIfc {

		public String getName() {
			return "Rob Harrop";
		}
	}

}
