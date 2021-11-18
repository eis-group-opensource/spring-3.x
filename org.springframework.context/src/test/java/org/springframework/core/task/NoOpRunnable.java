/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.task;

/**
 * A no-op Runnable implementation.
 *
 * @author Rick Evans
 */
public class NoOpRunnable implements Runnable {

	public void run() {
		// explicit no-op
		System.out.println("Running");
	}

}
