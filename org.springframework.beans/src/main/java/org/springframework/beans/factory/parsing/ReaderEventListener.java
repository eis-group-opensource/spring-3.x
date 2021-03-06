/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.parsing;

import java.util.EventListener;

/**
 * Interface that receives callbacks for component, alias and import
 * registrations during a bean definition reading process.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 2.0
 * @see ReaderContext
 */
public interface ReaderEventListener extends EventListener {

	/**
	 * Notification that the given defaults has been registered.
	 * @param defaultsDefinition a descriptor for the defaults
	 * @see org.springframework.beans.factory.xml.DocumentDefaultsDefinition
	 */
	void defaultsRegistered(DefaultsDefinition defaultsDefinition);

	/**
	 * Notification that the given component has been registered.
	 * @param componentDefinition a descriptor for the new component
	 * @see BeanComponentDefinition
	 */
	void componentRegistered(ComponentDefinition componentDefinition);

	/**
	 * Notification that the given alias has been registered.
	 * @param aliasDefinition a descriptor for the new alias
	 */
	void aliasRegistered(AliasDefinition aliasDefinition);

	/**
	 * Notification that the given import has been processed.
	 * @param importDefinition a descriptor for the import
	 */
	void importProcessed(ImportDefinition importDefinition);

}
