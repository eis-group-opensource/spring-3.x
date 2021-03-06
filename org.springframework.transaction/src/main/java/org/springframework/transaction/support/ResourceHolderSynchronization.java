/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.transaction.support;

/**
 * {@link TransactionSynchronization} implementation that manages a
 * {@link ResourceHolder} bound through {@link TransactionSynchronizationManager}.
 *
 * @author Juergen Hoeller
 * @since 2.5.5
 */
public abstract class ResourceHolderSynchronization<H extends ResourceHolder, K>
		implements TransactionSynchronization {

	private final H resourceHolder;

	private final K resourceKey;

	private volatile boolean holderActive = true;


	/**
	 * Create a new ResourceHolderSynchronization for the given holder.
	 * @param resourceHolder the ResourceHolder to manage
	 * @param resourceKey the key to bind the ResourceHolder for
	 * @see TransactionSynchronizationManager#bindResource
	 */
	public ResourceHolderSynchronization(H resourceHolder, K resourceKey) {
		this.resourceHolder = resourceHolder;
		this.resourceKey = resourceKey;
	}


	public void suspend() {
		if (this.holderActive) {
			TransactionSynchronizationManager.unbindResource(this.resourceKey);
		}
	}

	public void resume() {
		if (this.holderActive) {
			TransactionSynchronizationManager.bindResource(this.resourceKey, this.resourceHolder);
		}
	}

	public void flush() {
		flushResource(this.resourceHolder);
	}

	public void beforeCommit(boolean readOnly) {
	}

	public void beforeCompletion() {
		if (shouldUnbindAtCompletion()) {
			TransactionSynchronizationManager.unbindResource(this.resourceKey);
			this.holderActive = false;
			if (shouldReleaseBeforeCompletion()) {
				releaseResource(this.resourceHolder, this.resourceKey);
			}
		}
	}

	public void afterCommit() {
		if (!shouldReleaseBeforeCompletion()) {
			processResourceAfterCommit(this.resourceHolder);
		}
	}

	public void afterCompletion(int status) {
		if (shouldUnbindAtCompletion()) {
			boolean releaseNecessary = false;
			if (this.holderActive) {
				// The thread-bound resource holder might not be available anymore,
				// since afterCompletion might get called from a different thread.
				this.holderActive = false;
				TransactionSynchronizationManager.unbindResourceIfPossible(this.resourceKey);
				this.resourceHolder.unbound();
				releaseNecessary = true;
			}
			else {
				releaseNecessary = shouldReleaseAfterCompletion(this.resourceHolder);
			}
			if (releaseNecessary) {
				releaseResource(this.resourceHolder, this.resourceKey);
			}
		}
		else {
			// Probably a pre-bound resource...
			cleanupResource(this.resourceHolder, this.resourceKey, (status == STATUS_COMMITTED));
		}
		this.resourceHolder.reset();
	}


	/**
	 * Return whether this holder should be unbound at completion
	 * (or should rather be left bound to the thread after the transaction).
	 * <p>The default implementation returns <code>true</code>.
	 */
	protected boolean shouldUnbindAtCompletion() {
		return true;
	}

	/**
	 * Return whether this holder's resource should be released before
	 * transaction completion (<code>true</code>) or rather after
	 * transaction completion (<code>false</code>).
	 * <p>Note that resources will only be released when they are
	 * unbound from the thread ({@link #shouldUnbindAtCompletion()}).
	 * <p>The default implementation returns <code>true</code>.
	 * @see #releaseResource
	 */
	protected boolean shouldReleaseBeforeCompletion() {
		return true;
	}

	/**
	 * Return whether this holder's resource should be released after
	 * transaction completion (<code>true</code>).
	 * <p>The default implementation returns <code>!shouldReleaseBeforeCompletion()</code>,
	 * releasing after completion if no attempt was made before completion.
	 * @see #releaseResource
	 */
	protected boolean shouldReleaseAfterCompletion(H resourceHolder) {
		return !shouldReleaseBeforeCompletion();
	}

	/**
	 * Flush callback for the given resource holder.
	 * @param resourceHolder the resource holder to flush
	 */
	protected void flushResource(H resourceHolder) {
	}

	/**
	 * After-commit callback for the given resource holder.
	 * Only called when the resource hasn't been released yet
	 * ({@link #shouldReleaseBeforeCompletion()}).
	 * @param resourceHolder the resource holder to process
	 */
	protected void processResourceAfterCommit(H resourceHolder) {
	}

	/**
	 * Release the given resource (after it has been unbound from the thread).
	 * @param resourceHolder the resource holder to process
	 * @param resourceKey the key that the ResourceHolder was bound for
	 */
	protected void releaseResource(H resourceHolder, K resourceKey) {
	}

	/**
	 * Perform a cleanup on the given resource (which is left bound to the thread).
	 * @param resourceHolder the resource holder to process
	 * @param resourceKey the key that the ResourceHolder was bound for
	 * @param committed whether the transaction has committed (<code>true</code>)
	 * or rolled back (<code>false</code>)
	 */
	protected void cleanupResource(H resourceHolder, K resourceKey, boolean committed) {
	}

}
