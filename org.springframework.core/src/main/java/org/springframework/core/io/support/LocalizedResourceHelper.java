/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.io.support;

import java.util.Locale;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

/**
 * Helper class for loading a localized resource,
 * specified through name, extension and current locale.
 *
 * @author Juergen Hoeller
 * @since 1.2.5
 */
public class LocalizedResourceHelper {

	/** The default separator to use inbetween file name parts: an underscore */
	public static final String DEFAULT_SEPARATOR = "_";


	private final ResourceLoader resourceLoader;

	private String separator = DEFAULT_SEPARATOR;


	/**
	 * Create a new LocalizedResourceHelper with a DefaultResourceLoader.
	 * @see org.springframework.core.io.DefaultResourceLoader
	 */
	public LocalizedResourceHelper() {
		this.resourceLoader = new DefaultResourceLoader();
	}

	/**
	 * Create a new LocalizedResourceHelper with the given ResourceLoader.
	 * @param resourceLoader the ResourceLoader to use
	 */
	public LocalizedResourceHelper(ResourceLoader resourceLoader) {
		Assert.notNull(resourceLoader, "ResourceLoader must not be null");
		this.resourceLoader = resourceLoader;
	}

	/**
	 * Set the separator to use inbetween file name parts.
	 * Default is an underscore ("_").
	 */
	public void setSeparator(String separator) {
		this.separator = (separator != null ? separator : DEFAULT_SEPARATOR);
	}


	/**
	 * Find the most specific localized resource for the given name,
	 * extension and locale:
	 * <p>The file will be searched with locations in the following order,
	 * similar to <code>java.util.ResourceBundle</code>'s search order:
	 * <ul>
	 * <li>[name]_[language]_[country]_[variant][extension]
	 * <li>[name]_[language]_[country][extension]
	 * <li>[name]_[language][extension]
	 * <li>[name][extension]
	 * </ul>
	 * <p>If none of the specific files can be found, a resource
	 * descriptor for the default location will be returned.
	 * @param name the name of the file, without localization part nor extension
	 * @param extension the file extension (e.g. ".xls")
	 * @param locale the current locale (may be <code>null</code>)
	 * @return the most specific localized resource found
	 * @see java.util.ResourceBundle
	 */
	public Resource findLocalizedResource(String name, String extension, Locale locale) {
		Assert.notNull(name, "Name must not be null");
		Assert.notNull(extension, "Extension must not be null");

		Resource resource = null;

		if (locale != null) {
			String lang = locale.getLanguage();
			String country = locale.getCountry();
			String variant = locale.getVariant();

			// Check for file with language, country and variant localization.
			if (variant.length() > 0) {
				String location =
						name + this.separator + lang + this.separator + country + this.separator + variant + extension;
				resource = this.resourceLoader.getResource(location);
			}

			// Check for file with language and country localization.
			if ((resource == null || !resource.exists()) && country.length() > 0) {
				String location = name + this.separator + lang + this.separator + country + extension;
				resource = this.resourceLoader.getResource(location);
			}

			// Check for document with language localization.
			if ((resource == null || !resource.exists()) && lang.length() > 0) {
				String location = name + this.separator + lang + extension;
				resource = this.resourceLoader.getResource(location);
			}
		}

		// Check for document without localization.
		if (resource == null || !resource.exists()) {
			String location = name + extension;
			resource = this.resourceLoader.getResource(location);
		}

		return resource;
	}

}
