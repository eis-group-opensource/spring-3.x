/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.parsing;

import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * Class that models an arbitrary location in a {@link Resource resource}.
 *
 * <p>Typically used to track the location of problematic or erroneous
 * metadata in XML configuration files. For example, a
 * {@link #getSource() source} location might be 'The bean defined on
 * line 76 of beans.properties has an invalid Class'; another source might
 * be the actual DOM Element from a parsed XML {@link org.w3c.dom.Document};
 * or the source object might simply be <code>null</code>.
 *
 * @author Rob Harrop
 * @since 2.0
 */
public class Location {

	private final Resource resource;

	private final Object source;


	/**
	 * Create a new instance of the {@link Location} class.
	 * @param resource the resource with which this location is associated
	 */
	public Location(Resource resource) {
		this(resource, null);
	}

	/**
	 * Create a new instance of the {@link Location} class.
	 * @param resource the resource with which this location is associated
	 * @param source the actual location within the associated resource
	 * (may be <code>null</code>)
	 */
	public Location(Resource resource, Object source) {
		Assert.notNull(resource, "Resource must not be null");
		this.resource = resource;
		this.source = source;
	}


	/**
	 * Get the resource with which this location is associated.
	 */
	public Resource getResource() {
		return this.resource;
	}

	/**
	 * Get the actual location within the associated {@link #getResource() resource}
	 * (may be <code>null</code>).
	 * <p>See the {@link Location class level javadoc for this class} for examples
	 * of what the actual type of the returned object may be.
	 */
	public Object getSource() {
		return this.source;
	}

}
