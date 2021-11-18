/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.instrument.classloading;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Enumeration;

import org.junit.Test;

/**
 * @author Rod Johnson
 * @author Chris Beams
 * @since 2.0
 */
public class ResourceOverridingShadowingClassLoaderTests {
	
	private static final String EXISTING_RESOURCE = "org/springframework/instrument/classloading/testResource.xml";
	
	private ClassLoader thisClassLoader = getClass().getClassLoader();
	
	private ResourceOverridingShadowingClassLoader overridingLoader = new ResourceOverridingShadowingClassLoader(thisClassLoader);
	
	
	@Test
	public void testFindsExistingResourceWithGetResourceAndNoOverrides() {
		assertNotNull(thisClassLoader.getResource(EXISTING_RESOURCE));
		assertNotNull(overridingLoader.getResource(EXISTING_RESOURCE));
	}
	
	@Test
	public void testDoesNotFindExistingResourceWithGetResourceAndNullOverride() {
		assertNotNull(thisClassLoader.getResource(EXISTING_RESOURCE));
		overridingLoader.override(EXISTING_RESOURCE, null);
		assertNull(overridingLoader.getResource(EXISTING_RESOURCE));
	}
	
	@Test
	public void testFindsExistingResourceWithGetResourceAsStreamAndNoOverrides() {
		assertNotNull(thisClassLoader.getResourceAsStream(EXISTING_RESOURCE));
		assertNotNull(overridingLoader.getResourceAsStream(EXISTING_RESOURCE));
	}
	
	@Test
	public void testDoesNotFindExistingResourceWithGetResourceAsStreamAndNullOverride() {
		assertNotNull(thisClassLoader.getResourceAsStream(EXISTING_RESOURCE));
		overridingLoader.override(EXISTING_RESOURCE, null);
		assertNull(overridingLoader.getResourceAsStream(EXISTING_RESOURCE));
	}
	
	@Test
	public void testFindsExistingResourceWithGetResourcesAndNoOverrides() throws IOException {
		assertNotNull(thisClassLoader.getResources(EXISTING_RESOURCE));
		assertNotNull(overridingLoader.getResources(EXISTING_RESOURCE));
		assertEquals(1, countElements(overridingLoader.getResources(EXISTING_RESOURCE)));
	}
	
	@Test
	public void testDoesNotFindExistingResourceWithGetResourcesAndNullOverride() throws IOException {
		assertNotNull(thisClassLoader.getResources(EXISTING_RESOURCE));
		overridingLoader.override(EXISTING_RESOURCE, null);
		assertEquals(0, countElements(overridingLoader.getResources(EXISTING_RESOURCE)));
	}

	private int countElements(Enumeration<?> e) {
		int elts = 0;
		while (e.hasMoreElements()) {
			e.nextElement();
			++elts;
		}
		return elts;
	}
}
