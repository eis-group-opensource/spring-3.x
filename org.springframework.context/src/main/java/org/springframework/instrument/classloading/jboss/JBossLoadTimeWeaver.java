/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.instrument.classloading.jboss;

import java.lang.instrument.ClassFileTransformer;

import org.springframework.instrument.classloading.LoadTimeWeaver;
import org.springframework.instrument.classloading.SimpleThrowawayClassLoader;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * {@link LoadTimeWeaver} implementation for JBoss's instrumentable ClassLoader.
 * Autodetects the specific JBoss version at runtime: currently supports
 * JBoss AS 5, 6 and 7 (as of Spring 3.1).
 *
 * <p><b>NOTE:</b> On JBoss 6.0, to avoid the container loading the classes before the
 * application actually starts, one needs to add a <tt>WEB-INF/jboss-scanning.xml</tt>
 * file to the application archive - with the following content:
 * <pre>&lt;scanning xmlns="urn:jboss:scanning:1.0"/&gt;</pre>
 *
 * <p>Thanks to Ales Justin and Marius Bogoevici for the initial prototype.
 *
 * @author Costin Leau
 * @author Juergen Hoeller
 * @since 3.0
 */
public class JBossLoadTimeWeaver implements LoadTimeWeaver {

	private final JBossClassLoaderAdapter adapter;


	/**
	 * Create a new instance of the {@link JBossLoadTimeWeaver} class using
	 * the default {@link ClassLoader class loader}.
	 * @see org.springframework.util.ClassUtils#getDefaultClassLoader()
	 */
	public JBossLoadTimeWeaver() {
		this(ClassUtils.getDefaultClassLoader());
	}

	/**
	 * Create a new instance of the {@link JBossLoadTimeWeaver} class using
	 * the supplied {@link ClassLoader}.
	 * @param classLoader the <code>ClassLoader</code> to delegate to for weaving
	 * (must not be <code>null</code>)
	 */
	public JBossLoadTimeWeaver(ClassLoader classLoader) {
		Assert.notNull(classLoader, "ClassLoader must not be null");
		if (classLoader.getClass().getName().startsWith("org.jboss.modules")) {
			// JBoss AS 7
			this.adapter = new JBossModulesAdapter(classLoader);
		}
		else {
			// JBoss AS 5 or JBoss AS 6
			this.adapter = new JBossMCAdapter(classLoader);
		}
	}


	public void addTransformer(ClassFileTransformer transformer) {
		this.adapter.addTransformer(transformer);
	}

	public ClassLoader getInstrumentableClassLoader() {
		return this.adapter.getInstrumentableClassLoader();
	}

	public ClassLoader getThrowawayClassLoader() {
		return new SimpleThrowawayClassLoader(getInstrumentableClassLoader());
	}

}
