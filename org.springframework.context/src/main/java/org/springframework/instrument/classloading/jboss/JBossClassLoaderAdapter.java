/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.instrument.classloading.jboss;

import java.lang.instrument.ClassFileTransformer;

/**
 * Simple interface used for handling the different JBoss class loader adapters.
 *
 * @author Costin Leau
 * @since 3.1
 */
interface JBossClassLoaderAdapter {

	void addTransformer(ClassFileTransformer transformer);

	ClassLoader getInstrumentableClassLoader();

}
