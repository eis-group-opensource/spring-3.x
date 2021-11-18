/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core;

/**
 * Class that exposes the Spring version. Fetches the
 * "Implementation-Version" manifest attribute from the jar file.
 *
 * <p>Note that some ClassLoaders do not expose the package metadata,
 * hence this class might not be able to determine the Spring version
 * in all environments. Consider using a reflection-based check instead:
 * For example, checking for the presence of a specific Spring 2.0
 * method that you intend to call.
 *
 * @author Juergen Hoeller
 * @since 1.1
 */
public class SpringVersion {

	/**
	 * Return the full version string of the present Spring codebase,
	 * or <code>null</code> if it cannot be determined.
	 * @see java.lang.Package#getImplementationVersion()
	 */
	public static String getVersion() {
		Package pkg = SpringVersion.class.getPackage();
		return (pkg != null ? pkg.getImplementationVersion() : null);
	}

}
