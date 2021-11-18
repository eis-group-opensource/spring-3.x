/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scripting.support;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.TestCase;
import org.easymock.MockControl;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

/**
 * @author Rick Evans
 * @author Juergen Hoeller
 */
public class ResourceScriptSourceTests extends TestCase {

	public void testCtorWithNullResource() throws Exception {
		try {
			new ResourceScriptSource(null);
			fail("Must have thrown exception by this point.");
		}
		catch (IllegalArgumentException expected) {
		}
	}

	public void testDoesNotPropagateFatalExceptionOnResourceThatCannotBeResolvedToAFile() throws Exception {
		MockControl mock = MockControl.createControl(Resource.class);
		Resource resource = (Resource) mock.getMock();
		resource.lastModified();
		mock.setThrowable(new IOException());
		mock.replay();

		ResourceScriptSource scriptSource = new ResourceScriptSource(resource);
		long lastModified = scriptSource.retrieveLastModifiedTime();
		assertEquals(0, lastModified);
		mock.verify();
	}

	public void testBeginsInModifiedState() throws Exception {
		MockControl mock = MockControl.createControl(Resource.class);
		Resource resource = (Resource) mock.getMock();
		mock.replay();

		ResourceScriptSource scriptSource = new ResourceScriptSource(resource);
		assertTrue(scriptSource.isModified());
		mock.verify();
	}

	public void testLastModifiedWorksWithResourceThatDoesNotSupportFileBasedReading() throws Exception {
		MockControl mock = MockControl.createControl(Resource.class);
		Resource resource = (Resource) mock.getMock();
		// underlying File is asked for so that the last modified time can be checked...
		resource.lastModified();
		mock.setReturnValue(100, 2);
		// does not support File-based reading; delegates to InputStream-style reading...
		//resource.getFile();
		//mock.setThrowable(new FileNotFoundException());
		resource.getInputStream();
		mock.setReturnValue(new ByteArrayInputStream(new byte[0]));
		// And then mock the file changing; i.e. the File says it has been modified
		resource.lastModified();
		mock.setReturnValue(200);
		mock.replay();

		ResourceScriptSource scriptSource = new ResourceScriptSource(resource);
		assertTrue("ResourceScriptSource must start off in the 'isModified' state (it obviously isn't).", scriptSource.isModified());
		scriptSource.getScriptAsString();
		assertFalse("ResourceScriptSource must not report back as being modified if the underlying File resource is not reporting a changed lastModified time.", scriptSource.isModified());
		// Must now report back as having been modified
		assertTrue("ResourceScriptSource must report back as being modified if the underlying File resource is reporting a changed lastModified time.", scriptSource.isModified());
		mock.verify();
	}

	public void testLastModifiedWorksWithResourceThatDoesNotSupportFileBasedAccessAtAll() throws Exception {
		Resource resource = new ByteArrayResource(new byte[0]);
		ResourceScriptSource scriptSource = new ResourceScriptSource(resource);
		assertTrue("ResourceScriptSource must start off in the 'isModified' state (it obviously isn't).", scriptSource.isModified());
		scriptSource.getScriptAsString();
		assertFalse("ResourceScriptSource must not report back as being modified if the underlying File resource is not reporting a changed lastModified time.", scriptSource.isModified());
		// Must now continue to report back as not having been modified 'cos the Resource does not support access as a File (and so the lastModified date cannot be determined).
		assertFalse("ResourceScriptSource must not report back as being modified if the underlying File resource is not reporting a changed lastModified time.", scriptSource.isModified());
	}

}
