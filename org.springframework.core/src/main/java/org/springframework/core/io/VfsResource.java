/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import org.springframework.core.NestedIOException;
import org.springframework.util.Assert;

/**
 * VFS based {@link Resource} implementation.
 * Supports the corresponding VFS API versions on JBoss AS 5.x as well as 6.x and 7.x.
 *
 * @author Ales Justin
 * @author Juergen Hoeller
 * @author Costin Leau
 * @since 3.0
 * @see org.jboss.virtual.VirtualFile
 * @see org.jboss.vfs.VirtualFile
 */
public class VfsResource extends AbstractResource {

	private final Object resource;


	public VfsResource(Object resources) {
		Assert.notNull(resources, "VirtualFile must not be null");
		this.resource = resources;
	}


	public InputStream getInputStream() throws IOException {
		return VfsUtils.getInputStream(this.resource);
	}

	@Override
	public boolean exists() {
		return VfsUtils.exists(this.resource);
	}

	@Override
	public boolean isReadable() {
		return VfsUtils.isReadable(this.resource);
	}

	@Override
	public URL getURL() throws IOException {
		try {
			return VfsUtils.getURL(this.resource);
		}
		catch (Exception ex) {
			throw new NestedIOException("Failed to obtain URL for file " + this.resource, ex);
		}
	}

	@Override
	public URI getURI() throws IOException {
		try {
			return VfsUtils.getURI(this.resource);
		}
		catch (Exception ex) {
			throw new NestedIOException("Failed to obtain URI for " + this.resource, ex);
		}
	}

	@Override
	public File getFile() throws IOException {
		return VfsUtils.getFile(this.resource);
	}

	@Override
	public long contentLength() throws IOException {
		return VfsUtils.getSize(this.resource);
	}

	@Override
	public long lastModified() throws IOException {
		return VfsUtils.getLastModified(this.resource);
	}

	@Override
	public Resource createRelative(String relativePath) throws IOException {
		if (!relativePath.startsWith(".") && relativePath.contains("/")) {
			try {
				return new VfsResource(VfsUtils.getChild(this.resource, relativePath));
			}
			catch (IOException ex) {
				// fall back to getRelative
			}
		}

		return new VfsResource(VfsUtils.getRelative(new URL(getURL(), relativePath)));
	}

	@Override
	public String getFilename() {
		return VfsUtils.getName(this.resource);
	}

	public String getDescription() {
		return this.resource.toString();
	}

	@Override
	public boolean equals(Object obj) {
		return (obj == this || (obj instanceof VfsResource && this.resource.equals(((VfsResource) obj).resource)));
	}

	@Override
	public int hashCode() {
		return this.resource.hashCode();
	}

}
