/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.parsing;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Rob Harrop
 * @author Chris Beams
 * @since 2.0
 */
public class ParseStateTests {

	@Test
	public void testSimple() throws Exception {
		MockEntry entry = new MockEntry();

		ParseState parseState = new ParseState();
		parseState.push(entry);
		assertEquals("Incorrect peek value.", entry, parseState.peek());
		parseState.pop();
		assertNull("Should get null on peek()", parseState.peek());
	}

	@Test
	public void testNesting() throws Exception {
		MockEntry one = new MockEntry();
		MockEntry two = new MockEntry();
		MockEntry three = new MockEntry();

		ParseState parseState = new ParseState();
		parseState.push(one);
		assertEquals(one, parseState.peek());
		parseState.push(two);
		assertEquals(two, parseState.peek());
		parseState.push(three);
		assertEquals(three, parseState.peek());

		parseState.pop();
		assertEquals(two, parseState.peek());
		parseState.pop();
		assertEquals(one, parseState.peek());
	}

	@Test
	public void testSnapshot() throws Exception {
		MockEntry entry = new MockEntry();

		ParseState original = new ParseState();
		original.push(entry);

		ParseState snapshot = original.snapshot();
		original.push(new MockEntry());
		assertEquals("Snapshot should not have been modified.", entry, snapshot.peek());
	}


	private static class MockEntry implements ParseState.Entry {

	}

}
