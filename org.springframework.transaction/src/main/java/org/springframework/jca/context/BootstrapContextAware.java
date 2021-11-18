/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jca.context;

import javax.resource.spi.BootstrapContext;

import org.springframework.beans.factory.Aware;

/**
 * Interface to be implemented by any object that wishes to be
 * notified of the BootstrapContext (typically determined by the
 * {@link ResourceAdapterApplicationContext}) that it runs in.
 *
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 2.5
 * @see javax.resource.spi.BootstrapContext
 */
public interface BootstrapContextAware extends Aware {

	/**
	 * Set the BootstrapContext that this object runs in.
	 * <p>Invoked after population of normal bean properties but before an init
	 * callback like InitializingBean's <code>afterPropertiesSet</code> or a
	 * custom init-method. Invoked after ApplicationContextAware's
	 * <code>setApplicationContext</code>.
	 * @param bootstrapContext BootstrapContext object to be used by this object
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext
	 */
	void setBootstrapContext(BootstrapContext bootstrapContext);

}
