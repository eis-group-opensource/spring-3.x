/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.annotation;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;

import java.lang.instrument.ClassFileTransformer;

import org.junit.Test;
import org.springframework.context.annotation.EnableLoadTimeWeaving.AspectJWeaving;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.instrument.classloading.LoadTimeWeaver;

/**
 * Unit tests for @EnableLoadTimeWeaving
 *
 * @author Chris Beams
 * @since 3.1
 */
public class EnableLoadTimeWeavingTests {

	@Test
	public void control() {
		GenericXmlApplicationContext ctx =
			new GenericXmlApplicationContext(getClass(), "EnableLoadTimeWeavingTests-context.xml");
		ctx.getBean("loadTimeWeaver");
	}

	@Test
	public void enableLTW_withAjWeavingDisabled() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(EnableLTWConfig_withAjWeavingDisabled.class);
		ctx.refresh();
		ctx.getBean("loadTimeWeaver");
	}

	@Test
	public void enableLTW_withAjWeavingAutodetect() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(EnableLTWConfig_withAjWeavingAutodetect.class);
		ctx.refresh();
		ctx.getBean("loadTimeWeaver");
	}

	@Test
	public void enableLTW_withAjWeavingEnabled() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(EnableLTWConfig_withAjWeavingEnabled.class);
		ctx.refresh();
		ctx.getBean("loadTimeWeaver");
	}

	@Configuration
	@EnableLoadTimeWeaving(aspectjWeaving=AspectJWeaving.DISABLED)
	static class EnableLTWConfig_withAjWeavingDisabled implements LoadTimeWeavingConfigurer {
		public LoadTimeWeaver getLoadTimeWeaver() {
			LoadTimeWeaver mockLTW = createMock(LoadTimeWeaver.class);
			// no expectations -> a class file transformer should NOT be added
			replay(mockLTW);
			return mockLTW;
		}
	}

	@Configuration
	@EnableLoadTimeWeaving(aspectjWeaving=AspectJWeaving.AUTODETECT)
	static class EnableLTWConfig_withAjWeavingAutodetect implements LoadTimeWeavingConfigurer {
		public LoadTimeWeaver getLoadTimeWeaver() {
			LoadTimeWeaver mockLTW = createMock(LoadTimeWeaver.class);
			// no expectations -> a class file transformer should NOT be added
			// because no META-INF/aop.xml is present on the classpath
			replay(mockLTW);
			return mockLTW;
		}
	}

	@Configuration
	@EnableLoadTimeWeaving(aspectjWeaving=AspectJWeaving.ENABLED)
	static class EnableLTWConfig_withAjWeavingEnabled implements LoadTimeWeavingConfigurer {
		public LoadTimeWeaver getLoadTimeWeaver() {
			LoadTimeWeaver mockLTW = createMock(LoadTimeWeaver.class);
			mockLTW.addTransformer(isA(ClassFileTransformer.class));
			expectLastCall();
			replay(mockLTW);
			return mockLTW;
		}
	}
}
