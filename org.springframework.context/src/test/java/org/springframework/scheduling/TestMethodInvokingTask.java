/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scheduling;

/**
 * @author Juergen Hoeller
 * @since 09.10.2004
 */
public class TestMethodInvokingTask {

	public int counter = 0;

	private Object lock = new Object();

	public void doSomething() {
		this.counter++;
	}

	public void doWait() {
		this.counter++;
		// wait until stop is called
		synchronized (this.lock) {
			try {
				this.lock.wait();
			}
			catch (InterruptedException e) {
				// fall through
			}
		}
	}

	public void stop() {
		synchronized(this.lock) {
			this.lock.notify();
		}
	}

}