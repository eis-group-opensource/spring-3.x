/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.support;

import java.util.Set;

import junit.framework.TestCase;

/**
 * @author Rick Evans
 * @author Juergen Hoeller
 */
public class ManagedSetTests extends TestCase {

	public void testMergeSunnyDay() {
		ManagedSet parent = new ManagedSet();
		parent.add("one");
		parent.add("two");
		ManagedSet child = new ManagedSet();
		child.add("three");
		child.setMergeEnabled(true);
		Set mergedSet = (Set) child.merge(parent);
		assertEquals("merge() obviously did not work.", 3, mergedSet.size());
	}

	public void testMergeWithNullParent() {
		ManagedSet child = new ManagedSet();
		child.add("one");
		child.setMergeEnabled(true);
		assertSame(child, child.merge(null));
	}

	public void testMergeNotAllowedWhenMergeNotEnabled() {
		ManagedSet child = new ManagedSet();
		try {
			child.merge(null);
			fail("Must have failed by this point (cannot merge() when the mergeEnabled property is false.");
		}
		catch (IllegalStateException expected) {
		}
	}

	public void testMergeWithNonCompatibleParentType() {
		ManagedSet child = new ManagedSet();
		child.add("one");
		child.setMergeEnabled(true);
		try {
			child.merge("hello");
			fail("Must have failed by this point.");
		}
		catch (IllegalArgumentException expected) {
		}
	}

	public void testMergeEmptyChild() {
		ManagedSet parent = new ManagedSet();
		parent.add("one");
		parent.add("two");
		ManagedSet child = new ManagedSet();
		child.setMergeEnabled(true);
		Set mergedSet = (Set) child.merge(parent);
		assertEquals("merge() obviously did not work.", 2, mergedSet.size());
	}

	public void testMergeChildValuesOverrideTheParents() {
		// asserts that the set contract is not violated during a merge() operation...
		ManagedSet parent = new ManagedSet();
		parent.add("one");
		parent.add("two");
		ManagedSet child = new ManagedSet();
		child.add("one");
		child.setMergeEnabled(true);
		Set mergedSet = (Set) child.merge(parent);
		assertEquals("merge() obviously did not work.", 2, mergedSet.size());
	}

}
