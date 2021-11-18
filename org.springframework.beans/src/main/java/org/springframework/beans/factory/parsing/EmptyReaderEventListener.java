/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.parsing;

/**
 * Empty implementation of the {@link ReaderEventListener} interface,
 * providing no-op implementations of all callback methods.
 *
 * @author Juergen Hoeller
 * @since 2.0
 */
public class EmptyReaderEventListener implements ReaderEventListener {

	public void defaultsRegistered(DefaultsDefinition defaultsDefinition) {
		// no-op
	}

	public void componentRegistered(ComponentDefinition componentDefinition) {
		// no-op
	}

	public void aliasRegistered(AliasDefinition aliasDefinition) {
		// no-op
	}

	public void importProcessed(ImportDefinition importDefinition) {
		// no-op
	}

}
