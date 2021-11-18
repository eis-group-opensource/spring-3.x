/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.context.support;

import org.springframework.context.Lifecycle;

/**
 * @author Mark Fisher
 */
public class LifecycleTestBean implements Lifecycle {

	private static int startCounter;

	private static int stopCounter;


	private int startOrder;

	private int stopOrder;

	private boolean running;


	public int getStartOrder() {
		return startOrder;
	}

	public int getStopOrder() {
		return stopOrder;
	}

	public boolean isRunning() {
		return this.running;
	}

	public void start() {
		this.startOrder = ++startCounter;
		this.running = true;
	}

	public void stop() {
		this.stopOrder = ++stopCounter;
		this.running = false;		
	}

}
